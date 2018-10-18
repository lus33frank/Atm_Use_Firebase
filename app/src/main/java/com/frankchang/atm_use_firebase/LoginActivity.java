package com.frankchang.atm_use_firebase;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private static final String USER_ID = "UserId";
    private static final String REMEMBER_ID = "RememberId";
    private static final int REQUEST_CODE_CAMERA = 5;
    private EditText etUserId;
    private EditText etPassWord;
    private CheckBox chkRemember;
    private SharedPreferences sp;
    private boolean isInit = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentNews, NewsFragment.getInstance()).commit();

        // 檢查是否有相機權限
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // 開啟相機
            takePhoto();

        } else {
            // 詢問開啟權限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }

        sp = getSharedPreferences("ATM", MODE_PRIVATE);

        findViews();    // 畫面元件連結
        initData();     // 初始化資料
    }

    // 開啟相機
    private void takePhoto() {
        // 有！呼叫開啟相機
        //startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
    }

    // 詢問授權回覆處理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 開啟相機
                takePhoto();
            }
        }
    }

    // 畫面元件連結
    private void findViews() {
        etUserId = findViewById(R.id.etUserId);
        etPassWord = findViewById(R.id.etPassWord);
        chkRemember = findViewById(R.id.chkRemember);
        chkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isInit) {
                    sp.edit().putBoolean(REMEMBER_ID, isChecked).apply();
                }
            }
        });
    }

    // 初始化資料
    private void initData() {
        boolean isCkeck = sp.getBoolean(REMEMBER_ID, true);
        chkRemember.setChecked(isCkeck);

        if (isCkeck) {
            String userid = sp.getString(USER_ID, "");
            etUserId.setText(userid);
        } else {
            etUserId.setText("");
        }

        isInit = false;
    }

    // Login
    public void loginOnClick(View view) {
        // 取值
        final String userID = etUserId.getText().toString();
        final String userPW = etPassWord.getText().toString();

        // 判斷是否登入成功
        FirebaseDatabase.getInstance().getReference("users").child(userID).child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String pw = (String) dataSnapshot.getValue();
                        assert pw != null;
                        if (pw.equals(userPW)) {
                            // 成功
                            sp.edit().putString(USER_ID, userID).commit();
                            closePage(RESULT_OK);

                        } else {
                            // 失敗
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setTitle("登入")
                                    .setMessage("登入失敗！")
                                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            closePage(RESULT_CANCELED);
                                        }
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 未使用
                    }
                });
    }

    // Quit
    public void quitOnClick(View view) {
        closePage(RESULT_FIRST_USER);
    }

    // 設定回傳值，關閉頁面
    private void closePage(int resultCanceled) {
        setResult(resultCanceled);
        finish();
    }
}
