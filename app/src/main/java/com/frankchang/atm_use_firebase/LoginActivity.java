package com.frankchang.atm_use_firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


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
        String userID = etUserId.getText().toString();
        String userPW = etPassWord.getText().toString();

        // 判斷是否登入成功
        if ("frank".equals(userID) && "123".equals(userPW)) {
            closePage(RESULT_OK);

        } else {
            closePage(RESULT_CANCELED);
        }
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
