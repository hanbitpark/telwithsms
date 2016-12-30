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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    EditText mNum;
    EditText mText;
    TextView mSend;

    final static String ACTION_SENT =  "ACTION_MESSAGE_SENT";
    final static String ACTION_DELIVERY =  "ACTION_MESSAGE_DELIVERY";
    static final String TAG = MainActivity.class.getSimpleName();

    private Uri mCapturedImageURI;
    static final int FILECHOOSER_LOLLIPOP_REQ_CODE = 2;
    static final int FILECHOOSER_NORMAL_REQ_CODE =  1;
    private ValueCallback<Uri> filePathCallbackNormal;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PermissionCheck();

        tv = new TextView(this);
        tv.setText("최상위뷰이다");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextColor(Color.BLUE);

        //최상위뷰
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,  // 최상위
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, //터치인식
                PixelFormat.TRANSLUCENT); //투명

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE); //윈도우매니저
//        wm.addView(tv, params);


        mNum = (EditText)findViewById(R.id.editText);
        mText = (EditText)findViewById(R.id.editText2);
        mSend = (TextView)findViewById(R.id.textview2);

        findViewById(R.id.button2).setOnClickListener(mClickListener);

        Intent phoneNumberIntent = getIntent();
        String phoneNumber  = phoneNumberIntent.getStringExtra("phoneNumber");
        Log.d(TAG, "메인number : " + phoneNumber);
        mNum.setText(phoneNumber);



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

                case R.id.button3:

                    Log.d(TAG, "BUTTON3");
//                    String fileString = "...";
//                    String num2 = mNum.getText().toString();
//                    String text2 = mText.getText().toString();
//                    Intent mmsIntent = new Intent(Intent.ACTION_SEND);
//                    mmsIntent.putExtra("sms_body", text2);
//                    mmsIntent.putExtra("address", num2);
//                    mmsIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(fileString)));
//                    mmsIntent.setType("image/jpeg");
//                    startActivity(Intent.createChooser(mmsIntent, "Send"));

//                    // Create AndroidExampleFolder at sdcard
//                    File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AndroidExampleFolder");
//                    if (!imageStorageDir.exists()) {
//                        // Create AndroidExampleFolder at sdcard
//                        imageStorageDir.mkdirs();
//                    }

                    // Create camera captured image file path and name
//                    File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    mCapturedImageURI = Uri.fromFile(file);

                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    // Create file chooser intent
                    Intent chooserIntent = Intent.createChooser(captureIntent, "작업선택");
//                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, galleryIntent);
                    // Set camera intent to file chooser
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{galleryIntent});


                    // On select image call onActivityResult method of activity
                    startActivityForResult(chooserIntent, 1);
//                    startActivityForResult(chooserIntent2, FILECHOOSER_LOLLIPOP_REQ_CODE);
                    break;

                case R.id.button2:

                    Log.d(TAG, "BUTTON2");
                    SmsManager sms = SmsManager.getDefault();
                    String num = mNum.getText().toString();
                    String text = mText.getText().toString();

                    if (num.length() == 0 || text.length() == 0) {
                            return;
                        }

                    mSend.setText("송신 대기...");
    //                    mDelivery.setText("상대방 수신 대기...");

                    //단문메세지 80글자이하

                    PendingIntent SentIntent = PendingIntent.getBroadcast(
                                MainActivity.this, 0, new Intent(ACTION_SENT), 0);
                    if(text.length() > 80){
                            //80글자이상 장문일경수 실행
                            ArrayList<String> parts = sms.divideMessage(text);
                            ArrayList<PendingIntent> SentPendingIntent = new ArrayList<PendingIntent>();

                            for (int i = 0; i < parts.size(); i++) {
                                SentPendingIntent.add(SentIntent);
                            }

                            sms.sendMultipartTextMessage(num, null, parts, SentPendingIntent, null);


                    }else{
                        //80글자이하 단문이하

                        sms.sendTextMessage(num, null, text, SentIntent, null);
                    }


//                        sms.sendMultimediaMessage();
                    break;

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri[] result = new Uri[0];
            if(resultCode == RESULT_OK){
//                result = (data == null) ? new Uri[]{mCapturedImageURI} : WebChromeClient.FileChooserParams.parseResult(resultCode, data);
//                mText.setImageBitmap((Bitmap)data.getExtras().get("data"));
//                mText.set
            }
//            filePathCallbackLollipop.onReceiveValue(result);

    }

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
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){}
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED){}
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
//        int permissionCheck3 = ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW);

        //권한 미승인
        if ((permissionCheck1 == PackageManager.PERMISSION_DENIED) ||
                (permissionCheck2 == PackageManager.PERMISSION_DENIED)) {

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
