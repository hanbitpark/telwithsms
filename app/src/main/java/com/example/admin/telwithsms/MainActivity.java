package com.example.admin.telwithsms;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText mNum;
    EditText mText;
    TextView mSend;

    final static String ACTION_SENT =  "ACTION_MESSAGE_SENT";
    final static String ACTION_DELIVERY =  "ACTION_MESSAGE_DELIVERY";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionCheck();

        mNum = (EditText)findViewById(R.id.editText);
        mText = (EditText)findViewById(R.id.editText2);
        mSend = (TextView)findViewById(R.id.textview2);

        findViewById(R.id.button2).setOnClickListener(mClickListener);

    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mSendBR, new IntentFilter(ACTION_SENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mSendBR);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.button2:
                    SmsManager sms = SmsManager.getDefault();
                    String num = mNum.getText().toString();
                    String text = mText.getText().toString();

                    if (num.length() == 0 || text.length() == 0) {
                        return;
                    }

                    mSend.setText("송신 대기...");
//                    mDelivery.setText("상대방 수신 대기...");

                    PendingIntent SentIntent = PendingIntent.getBroadcast(
                            MainActivity.this, 0, new Intent(ACTION_SENT), 0);
                    sms.sendTextMessage(num, null, text, SentIntent, null);
                    sms.sendMultimediaMessage(getApplicationContext(), uri, String url, ,bundle, SentIntent);
                    break;
            }
        }
    };

    // 송신 확인
    BroadcastReceiver mSendBR = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (getResultCode() == Activity.RESULT_OK) {
                mSend.setText("메시지 송신 성공");
            } else {
                mSend.setText("메시지 송신 실패");
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1000){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){}
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED){}
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){}
            }
        }else{
            Toast.makeText(getApplicationContext(), "권한 요청을 거부하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void PermissionCheck(){
        //통화수신권한체크

        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        //권한 미승인
        if ((permissionCheck1 == PackageManager.PERMISSION_DENIED)
                || (permissionCheck2 == PackageManager.PERMISSION_DENIED)) {

            Toast.makeText(this, "미승인되어서 앱 실행이 중지되었습니다.", Toast.LENGTH_SHORT).show();

            if(shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)){
                //권한을 한번이상 미승인 되었을때 재실행
                requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, 1000);

            }else{
                //승인안된 권한 실행
                requestPermissions(new String [] {Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, 1000);
            }

        }else{
            //권한 승인
            Toast.makeText(this, "승인되어서 앱이 실행이 시작되었습니다.", Toast.LENGTH_SHORT).show();
        }

    }

//    정적 리시버
    public void sendBroadcast(View v){
        Intent intent = new Intent("com.example.admin.broadcast");
        intent.putExtra("phoneNumber", "01044890885");
        sendBroadcast(intent);
    }
}
