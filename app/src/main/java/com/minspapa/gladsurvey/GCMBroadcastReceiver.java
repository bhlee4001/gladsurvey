package com.minspapa.gladsurvey;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "GCMBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {		//상대방이 메시지 보낼때  intent의 부가적인 정보로 사용
        String action = intent.getAction();
        Log.d(TAG, "action : " + action);

        if (action != null) {
            if (action.equals("com.google.android.c2dm.intent.RECEIVE")) { // 푸시 메시지 수신 시
                String sQtProperty = intent.getStringExtra("QT_PROPERTY");
                String sQtCheck = intent.getStringExtra("QT_CHECK");
                String sQtSeq = intent.getStringExtra("QT_SEQ");
                String sQtLang = intent.getStringExtra("QT_LANG");
                String sQtDevice = intent.getStringExtra("QT_DEVICE");
                String sQtReqGb = intent.getStringExtra("QT_REQ_GB");
                String sQtSurNo = intent.getStringExtra("QT_SUR_NO");

                /*
                String ralData = "";
                try {
                    ralData = URLDecoder.decode(rawData, "UTF-8");
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                */

                Log.d(TAG, "===> sQtProperty : " + sQtProperty + ", sQtCheck : " + sQtCheck + ", sQtSeq : " + sQtSeq + ", sQtLang : " + sQtLang + ", sQtDevice : " + sQtDevice + ", sQtReqGb : " + sQtReqGb + ", sQtSurNo : " + sQtSurNo);

                // 액티비티로 전달
                sendToActivity(context, sQtProperty, sQtCheck, sQtSeq, sQtLang, sQtDevice, sQtReqGb, sQtSurNo);

            } else {
                Log.d(TAG, "===> action unknown : " + action);
            }
        } else {
            Log.d(TAG, "===> action is null.");
        }

    }

    /**
     * 메인 액티비티로 수신된 푸시 메시지 데이터 전달
     */
    private void sendToActivity(Context context, String sQtProperty, String sQtCheck, String sQtSeq, String sQtLang, String sQtDevice, String sQtReqGb, String sQtSurNo) {
        //Intent intent = new Intent(context, MainActivity.class);
        Intent intent = new Intent(context, SurveyActivity.class);

        intent.putExtra("sQtProperty", sQtProperty);
        intent.putExtra("sQtCheck", sQtCheck);
        intent.putExtra("sQtSeq", sQtSeq);
        intent.putExtra("sQtLang", sQtLang);
        intent.putExtra("sQtDevice", sQtDevice);
        intent.putExtra("sQtReqGb", sQtReqGb);
        intent.putExtra("sQtSurNo", sQtSurNo);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Log.d(TAG, "===> sendToActivity ===> sQtProperty : " + sQtProperty + ", sQtCheck : " + sQtCheck + ", sQtSeq : " + sQtSeq + ", sQtLang : " + sQtLang + ", sQtDevice : " + sQtDevice + ", sQtReqGb : " + sQtReqGb + ", sQtSurNo : " + sQtSurNo);

        context.startActivity(intent);
    }

}
