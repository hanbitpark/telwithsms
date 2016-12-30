package com.example.admin.telwithsms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;


public class ServiceReceiver extends BroadcastReceiver {


    private static final String TAG = "ServiceReceiver";
    //통화화면으로 넘어가면서 Activity가 onDestory되므로 , Receiver도 초기화

    static int currentState = -1;
    static String idle;


    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        EditText editText;


        if(intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){

            final String state = extras.getString(TelephonyManager.EXTRA_STATE);

            if(state.equals("IDLE")){

                Log.d(TAG, "number : " +idle);
                Intent numberIntent = new Intent(context, MainActivity.class);
                numberIntent.putExtra("phoneNumber", idle);
                numberIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(numberIntent);

            }else if(state.equals("OFFHOOK")){
                idle = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d(TAG, "OFFHOKK : " + idle);

            }else if(state.equals("RINGING")){
                idle = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d(TAG, "RINGING : " +idle);
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





