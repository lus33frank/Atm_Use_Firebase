package com.frankchang.atm_use_firebase;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    // 畫面元件
    private EditText etUserId;
    private EditText etPassWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();    // 畫面元件連結
    }

    // 畫面元件連結
    private void findViews() {
        etUserId = findViewById(R.id.etUserId);
        etPassWord = findViewById(R.id.etPassWord);
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
        closePage(RESULT_CANCELED);
    }

    // 設定回傳值，關閉頁面
    private void closePage(int resultCanceled) {
        setResult(resultCanceled);
        finish();
    }
}
