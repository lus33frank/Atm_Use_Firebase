package com.frankchang.atm_use_firebase;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddActivity extends AppCompatActivity {

    private EditText etDate;
    private EditText etInfo;
    private EditText etAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        findViews();
    }

    private void findViews() {
        etDate = findViewById(R.id.etDate);
        etInfo = findViewById(R.id.etInfo);
        etAmount = findViewById(R.id.etAmount);
    }

    public void addOnClick(View view) {
        String date = etDate.getText().toString();
        String info = etInfo.getText().toString();
        int amount = Integer.parseInt(etAmount.getText().toString());

        ExpenseHelper helper = new ExpenseHelper(this);
        ContentValues values = new ContentValues();
        values.put("cdate", date);
        values.put("info", info);
        values.put("amount", amount);
        long id = helper.getWritableDatabase().insert("expense", null, values);

        if (id > -1) {
            Toast.makeText(this, "新增成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "新增失敗", Toast.LENGTH_SHORT).show();
        }
    }

}
