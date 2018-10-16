package com.frankchang.atm_use_firebase;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
    private EditText etUserId;
    private EditText etPassWord;
    private CheckBox chkRemember;
    private SharedPreferences sp;
    private boolean isInit = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences("ATM", MODE_PRIVATE);

        findViews();    // 畫面元件連結
        initData();     // 初始化資料
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
