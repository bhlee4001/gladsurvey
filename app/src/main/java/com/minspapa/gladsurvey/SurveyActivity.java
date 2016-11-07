package com.minspapa.gladsurvey;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class SurveyActivity extends AppCompatActivity {
    public static final String TAG = "SurveyActivity";

    private ActionBar actionBar;

    private WebView webView;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

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

        //WEBVIEW
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebChromeClient(new SurveyActivity.WebBrowseClient());
        webView.addJavascriptInterface(new SurveyActivity.JavaScriptMethods(), "serveyJs");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            String sQtProperty = intent.getStringExtra("sQtProperty");
            String sQtCheck = intent.getStringExtra("sQtCheck");
            String sQtSeq = intent.getStringExtra("sQtSeq");
            String sQtLang = intent.getStringExtra("sQtLang");
            String sQtDevice = intent.getStringExtra("sQtDevice");
            String sQtReqGb = intent.getStringExtra("sQtReqGb");
            String sQtSurNo = intent.getStringExtra("sQtSurNo");

            String sUrl = GCMInfo.SERVER_URL;
            if("A".equalsIgnoreCase(sQtReqGb)){
                sUrl += GCMInfo.RESOURCE_2;
            }else if("B".equalsIgnoreCase(sQtReqGb)){
                sUrl += GCMInfo.RESOURCE_2;
            }else if("C".equalsIgnoreCase(sQtReqGb)){
                sUrl += GCMInfo.RESOURCE_3;
            }

            sUrl += "?QT_PROPERTY=" + sQtProperty;
            sUrl += "&QT_CHECK=" + sQtCheck;
            sUrl += "&QT_LANG=" + sQtLang;
            sUrl += "&QT_SEQ=" + sQtSeq;
            sUrl += "&QT_DEVICE=" + sQtDevice;
            sUrl += "&QT_REQ_GB=" + sQtReqGb;
            sUrl += "&QT_SUR_NO=" + sQtSurNo;

            Log.d(TAG, "===> sQtProperty : " + sQtProperty + ", sQtCheck : " + sQtCheck + ", sQtSeq : " + sQtSeq + ", sQtLang : " + sQtLang + ", sQtDevice : " + sQtDevice + ", sQtReqGb : " + sQtReqGb + ", sQtSurNo : " + sQtSurNo);
            Log.d(TAG, "===> sUrl : " + sUrl);

            webView.loadUrl(sUrl);
        }
    }

    /**
     * 스탠바이에서 요청시
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "===> onNewIntent() called.");

        processIntent(intent);

        super.onNewIntent(intent);
    }

    /**
     * 수신자로부터 전달받은 Intent 처리
     *
     * @param intent
     */
    private void processIntent(Intent intent) {
        if (intent != null) {
            String sQtProperty = intent.getStringExtra("sQtProperty");
            String sQtCheck = intent.getStringExtra("sQtCheck");
            String sQtSeq = intent.getStringExtra("sQtSeq");
            String sQtLang = intent.getStringExtra("sQtLang");
            String sQtDevice = intent.getStringExtra("sQtDevice");
            String sQtReqGb = intent.getStringExtra("sQtReqGb");
            String sQtSurNo = intent.getStringExtra("sQtSurNo");

            String sUrl = GCMInfo.SERVER_URL;
            if("A".equalsIgnoreCase(sQtReqGb)){
                sUrl += GCMInfo.RESOURCE_2;
            }else if("B".equalsIgnoreCase(sQtReqGb)){
                sUrl += GCMInfo.RESOURCE_2;
            }else if("C".equalsIgnoreCase(sQtReqGb)){
                sUrl += GCMInfo.RESOURCE_3;
            }

            sUrl += "?QT_PROPERTY=" + sQtProperty;
            sUrl += "&QT_CHECK=" + sQtCheck;
            sUrl += "&QT_LANG=" + sQtLang;
            sUrl += "&QT_SEQ=" + sQtSeq;
            sUrl += "&QT_DEVICE=" + sQtDevice;
            sUrl += "&QT_REQ_GB=" + sQtReqGb;
            sUrl += "&QT_SUR_NO=" + sQtSurNo;

            Log.d(TAG, "===> sQtProperty : " + sQtProperty + ", sQtCheck : " + sQtCheck + ", sQtSeq : " + sQtSeq + ", sQtLang : " + sQtLang + ", sQtDevice : " + sQtDevice + ", sQtReqGb : " + sQtReqGb + ", sQtSurNo : " + sQtSurNo);
            Log.d(TAG, "===> sUrl : " + sUrl);

            webView.loadUrl(sUrl);
        }

        String sQtProperty = intent.getStringExtra("sQtProperty");
        if (sQtProperty == null) {
            Log.d(TAG, "===> sQtProperty is null.");
            return;
        }

        Log.d(TAG, "===> processIntent() called.");
    }

    /**
     * 상태바 선택시
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "===> onOptionsItemSelected() called " + item.getItemId());

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();

        //return true;
        return super.onOptionsItemSelected(item);
    }

    /**
     * 서버사이드 스크립트
     */
    public class JavaScriptMethods{
        JavaScriptMethods(){
        }

        @android.webkit.JavascriptInterface
        public void EndOnSurvey(){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    finish();
                }
            });
        }

        @android.webkit.JavascriptInterface
        public void EndGoHome(){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    finish();
                }
            });
        }
    }

    final class WebBrowseClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }

}
