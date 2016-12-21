package com.example.admin.telwithsms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import org.w3c.dom.Text;

public class ServiceReceiver extends BroadcastReceiver {


    private static final String TAG = "ServiceReceiver";
    static Context mContext;

    //통화화면으로 넘어가면서 Activity가 onDestory되므로 , Receiver도 초기화
    static int currentState = -1;
    static String idle = null;



    @Override
    public void onReceive(Context context, Intent intent) {
//        mContext = context;

        Bundle extras = intent.getExtras();


        if(intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){

            final String state = extras.getString(TelephonyManager.EXTRA_STATE);

            //6.0이상 일때 가능
            final String incomingNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if(state.equals("IDLE")){
                if(currentState == TelephonyManager.CALL_STATE_OFFHOOK) {
                    //발신종료
//                    Toast.makeText(context, "발신 종료 전화번호 : " + idle, Toast.LENGTH_SHORT).show();

                    //전화번호 입력
                    ((EditText)((Activity)context).findViewById(R.id.editText)).setText(idle);
                }else if(currentState == TelephonyManager.CALL_STATE_RINGING){
                    //착신 종료
//                    Toast.makeText(context, "착신 종료 전화번호 : " + idle, Toast.LENGTH_SHORT).show();
                    //전화번호 입력
                    ((EditText)((Activity)context).findViewById(R.id.editText)).setText(idle);
                }

//                Toast.makeText(context, "착신종료 전화번호 : " + idle, Toast.LENGTH_SHORT).show();
                currentState = TelephonyManager.CALL_STATE_IDLE;
            }
            if(state.equals("OFFHOOK")){
                //발신중
//                Toast.makeText(context, "발신중 전화번호 : " + incomingNumber, Toast.LENGTH_SHORT).show();
                currentState = TelephonyManager.CALL_STATE_OFFHOOK;
                //6.0이하 버전 값저장
                idle = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            }
            if(state.equals("RINGING")){
//                Toast.makeText(context, "착신중 전화번호 : " + incomingNumber, Toast.LENGTH_SHORT).show();
                currentState = TelephonyManager.CALL_STATE_RINGING;
                idle = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            }
        }

        if(intent.getStringExtra("phoneNumber") != null) {
            Toast.makeText(context, intent.getStringExtra("phoneNumber"), Toast.LENGTH_SHORT).show();
        }



//        PhoneStateRead phoneListener = new PhoneStateRead();
//        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//
        //네트워크 상태변경
//        telephony.listen(new PhoneState(mContext), PhoneStateListener.LISTEN_SERVICE_STATE);

//        telephony.listen(new PhoneStateListener(){
//
//            @Override
//            public void onCallStateChanged(int state, String incomingNumber) {
//
//                String TAG = "callstate";
//
//                switch (state){
//                    //전화대기중
//                    case TelephonyManager.CALL_STATE_IDLE:
//                        Log.d(TAG, "CALL_STATE_IDLE : " + incomingNumber);
//                        Toast.makeText(mContext , "전화번호는 : " + incomingNumber, Toast.LENGTH_SHORT).show();
//
//                        ((TextView)((Activity)mContext).findViewById(R.id.textview)).setText(incomingNumber);
//                        break;
//
//                    //전화종료
//                    case TelephonyManager.CALL_STATE_OFFHOOK:
//                        Log.d(TAG, "CALL_STATE_OFFHOK : " + incomingNumber);
//                        break;
//
//                    case TelephonyManager.CALL_STATE_RINGING:
//                        Log.d(TAG, "CALL_STATE_RINGING : " +incomingNumber);
//                        break;
//
//                    default:
//                        Log.d(TAG, "default : " + Integer.toString(state));
//                        break;
//                }
//
//            }
//
//        }, PhoneStateListener.LISTEN_SERVICE_STATE);
//



    }



}





