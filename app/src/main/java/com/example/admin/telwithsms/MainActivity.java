package com.example.admin.telwithsms;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends AppCompatActivity {


    BroadcastReceiver mReceiver;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionCheck();

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1000){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){}
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED){}
            }
        }else{
            Toast.makeText(getApplicationContext(), "권한 요청을 거부하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    public void PermissionCheck(){
        //통화수신권한체크
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS);

        //권한 미승인
        if ((permissionCheck == PackageManager.PERMISSION_DENIED) && (permissionCheck2 == PackageManager.PERMISSION_DENIED)) {
            Toast.makeText(this, "미승인되어서 앱 실행이 중지되었습니다.", Toast.LENGTH_SHORT).show();

            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("전화상태확인 권한이 필요합니다.")
                        .setMessage("전화상태를 확인하기 위해서는 권한 승인이 필요합니다. 계속하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.PROCESS_OUTGOING_CALLS}, 1000);
                            }

                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "기능을 취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create()
                        .show();

            }else{
                //권한을 os에 요청
                requestPermissions(new String [] {Manifest.permission.READ_PHONE_STATE}, 1000);
            }

        }else{
            //권한 승인

            Toast.makeText(this, "승인되어서 앱이 실행이 시작되었습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    //정적 리시버
//    public void sendBroadcast(View v){
//        Intent intent = new Intent("com.example.admin.broadcast");
//        intent.putExtra("phoneNumber", "01044890885");
//        sendBroadcast(intent);
//    }
}
