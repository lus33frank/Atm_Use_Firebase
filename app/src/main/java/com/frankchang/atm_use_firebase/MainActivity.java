package com.frankchang.atm_use_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    // 畫面元件
    private RecyclerView recycler;
    // 常數
    private static final int REQUEST_LOGIN = 123;
    // 變數
    private boolean isLogon = false;
    private String[] functions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 檢查是否登入
        if (!isLogon) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(loginIntent, REQUEST_LOGIN);
        }

        findViews();    // 畫面元件連結
        initData();     // 初始化資料
    }

    // 畫面元件連結
    private void findViews() {
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Recycler
        recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);     // 是不是固定大小
        recycler.setLayoutManager(new LinearLayoutManager(this));   // 內部使用的容器類型
    }

    // 初始化資料
    private void initData() {
        // 取得陣列資料
        functions = getResources().getStringArray(R.array.functions);
        // 設定 RecyclerView
        FunctionAdapter adapter = new FunctionAdapter(this);
        recycler.setAdapter(adapter);
    }

    // 轉頁回傳值處理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 判斷登入結果
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                // 成功
                Toast.makeText(this, "登入成功！", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // 失敗
                Toast.makeText(this, "登入失敗！", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                // 結束
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
