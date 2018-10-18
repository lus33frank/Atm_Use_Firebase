package com.frankchang.atm_use_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    // 畫面元件
    private RecyclerView recycler;
    // 常數
    private static final int REQUEST_LOGIN = 123;
    // 變數
    private boolean isLogon = false;
    private List<Function> functions;


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
        // 內部使用的容器類型
        //recycler.setLayoutManager(new LinearLayoutManager(this));   // 條列式
        recycler.setLayoutManager(new GridLayoutManager(this, 3));  // 格狀3欄
    }

    // 初始化資料
    private void initData() {
        // 取得陣列資料
        setupFunctions();
        // 設定 RecyclerView
        //FunctionAdapter adapter = new FunctionAdapter(this);  // 外部類別
        IconAdapter adapter = new IconAdapter();    // 內部類別
        recycler.setAdapter(adapter);
    }

    // 取得陣列資料
    private void setupFunctions() {
        functions = new ArrayList<>();
        // 陣列資料
        String[] funcs = getResources().getStringArray(R.array.functions);
        // 設定圖檔資料
        functions.add(new Function(funcs[0], R.drawable.func_transaction));
        functions.add(new Function(funcs[1], R.drawable.func_balance));
        functions.add(new Function(funcs[2], R.drawable.func_finance));
        functions.add(new Function(funcs[3], R.drawable.func_contacts));
        functions.add(new Function(funcs[4], R.drawable.func_exit));
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

    // 點擊動作
    private void itemClicked(Function function) {
        switch (function.getIcon()) {
            case R.drawable.func_transaction:
                break;
            case R.drawable.func_balance:
                break;
            case R.drawable.func_finance:
                startActivity(new Intent(this, FinanceActivity.class));
                break;
            case R.drawable.func_contacts:
                startActivity(new Intent(this, ContactActivity.class));
                break;
            case R.drawable.func_exit:
                finish();
                break;
        }
    }


    // 內部類別
    public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconHolder> {

        @NonNull
        @Override
        public IconHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.item_icon, viewGroup, false);
            return new IconHolder(view);
        }

        // 設定每列所顯示的資料
        @Override
        public void onBindViewHolder(@NonNull IconHolder iconHolder, int i) {
            final Function function = functions.get(i);
            iconHolder.imgIcon.setImageResource(function.getIcon());
            iconHolder.tvIconName.setText(function.getName());

            iconHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 點擊動作
                    itemClicked(function);
                }
            });
        }

        @Override
        public int getItemCount() {
            return functions.size();
        }


        public class IconHolder extends RecyclerView.ViewHolder{

            ImageView imgIcon;
            TextView tvIconName;


            public IconHolder(@NonNull View itemView) {
                super(itemView);

                imgIcon = itemView.findViewById(R.id.imgItemIcon);
                tvIconName = itemView.findViewById(R.id.tvItemName);
            }

        }

    }

}
