package com.minspapa.gladsurvey;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private ActionBar actionBar;

    private WebView webView;

    private Handler handler = new Handler();

    private String gDeviceUuid;
    private String gRegId;
    private String gMacAddr;
    private String gIpAddr;

    ArrayList<String> idList = new ArrayList<String>();

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //ACTION BAR
        actionBar = this.getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff38435C));
        //actionBar.hide();
        actionBar.setTitle("");
        actionBar.setIcon(R.drawable.action_title);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //REGISTRATION_ID
        registerDevice();

        //DEVICE_UUID
        gDeviceUuid = getDevicesUUID(getApplicationContext());

        //MAC Addr
        checkAvailableConnection();

        //WEBVIEW
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebChromeClient(new MainActivity.WebBrowseClient());
        webView.addJavascriptInterface(new MainActivity.JavaScriptMethods(), "serveyJs");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(GCMInfo.SERVER_URL + GCMInfo.RESOURCE_1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.action_bar, menu);

        Log.d(TAG, "onCreateOptionsMenu() called");

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 스탠바이에서 요청시
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent() called.");

        processIntent(intent);

        super.onNewIntent(intent);
    }

    /**
     * 수신자로부터 전달받은 Intent 처리
     *
     * @param intent
     */
    private void processIntent(Intent intent) {
        String from = intent.getStringExtra("from");
        if (from == null) {
            Log.d(TAG, "from is null.");
            return;
        }

        String command = intent.getStringExtra("command");
        String type = intent.getStringExtra("type");
        String data = intent.getStringExtra("data");

        Log.d(TAG, "DATA : " + command + ", " + type + ", " + data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String name = data.getStringExtra("name");
            Toast.makeText(getApplicationContext(), "메인에서 전달받은 값 : " + name, Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 서버사이드 스크립트
     */
    public class JavaScriptMethods{
        JavaScriptMethods(){
        }

        @android.webkit.JavascriptInterface
        public void LoadOnSurvey(){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:updateRequestDeviceUuid('" + gDeviceUuid + "','" + gRegId + "','" + gMacAddr + "','" + gIpAddr + "')");
                }
            });
        }
    }

    /**
     * 서버사이트 스크립트(메시지)
     */
    final class WebBrowseClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }

    /**
     * GCM Registration Id
     */
    private void registerDevice() {
        RegisterThread registerObj = new RegisterThread();
        registerObj.start();
    }

    /**
     * GCM Registration Id
     */
    public class RegisterThread extends Thread {
        public void run() {
            try {
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                gRegId = gcm.register(GCMInfo.PROJECT_ID);

                Log.d(TAG, "==> 등록 ID : " + gRegId);

                // 등록 ID 리스트에 추가 (현재는 1개만)
                //idList.clear();
                //idList.add(regId);

            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * DEVICE UUID
     */
    private String getDevicesUUID(Context mContext){
        final TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        final String sDeviceId, sSerialNumber, sAndroidId;
        //sDeviceId = telephonyManager.getDeviceId();
        //sSerialNumber = telephonyManager.getSimSerialNumber();
        sAndroidId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        //UUID sDeviceUuid = new UUID(sAndroidId.hashCode(), ((long)sDeviceId.hashCode() << 32) | sSerialNumber.hashCode());

        return sAndroidId;
    }

    public void checkAvailableConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable()) {
            WifiManager myWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
            gIpAddr = android.text.format.Formatter.formatIpAddress(myWifiInfo.getIpAddress());
            gMacAddr = myWifiInfo.getMacAddress();

            System.out.println("WiFi address is " + gIpAddr);
        } else if (mobile.isAvailable()) {
            gIpAddr = getLocalIpAddress();
            Toast.makeText(this, "3G Available", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No Network Available", Toast.LENGTH_LONG).show();
        }
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            return "ERROR Obtaining IP";
        }
        return "No IP Available";
    }

/*
    public void Button1Click(View v){
        Intent intent = new Intent(getApplicationContext(), SurveyActivity.class);
        String sDeviceUuid = getDevicesUUID(getApplicationContext());

        Log.d("DEVICE_UUID", sDeviceUuid);
        intent.putExtra("DEVICE_UUID", sDeviceUuid);
        intent.putExtra("QT_SEQ", 1);
        //startActivityForResult(intent, 1001);
        //startActivity(intent);
        webView.loadUrl("javascript:changeFace()");
        registerDevice();

        Log.d(TAG, "IP ADDR :" + getLocalIpAddress());
        checkAvailableConnection();
    }
    */
}
