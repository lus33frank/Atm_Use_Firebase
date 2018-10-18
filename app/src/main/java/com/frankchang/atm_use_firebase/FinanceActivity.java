package com.frankchang.atm_use_firebase;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FinanceActivity extends AppCompatActivity {

    private RecyclerView list;
    private ExpenseAdapter adapter;
    private ExpenseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        findViews();
        initData();
    }

    private void initData() {
        helper = new ExpenseHelper(this);
        Cursor cursor = helper.getReadableDatabase().query("expense", null,
                null, null, null, null, null);
        adapter = new ExpenseAdapter(cursor);
        list.setAdapter(adapter);
    }

    private void findViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FinanceActivity.this, AddActivity.class));
            }
        });

        // RecyclerView
        list = findViewById(R.id.recyclerFinance);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor cursor = helper.getReadableDatabase().query("expense", null,
                null, null, null, null, null);
        adapter = new ExpenseAdapter(cursor);
        list.setAdapter(adapter);
    }

    public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseHolder> {

        private Cursor cursor;


        public ExpenseAdapter(Cursor cursor) {
            this.cursor = cursor;
        }

        @NonNull
        @Override
        public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.item_expense, viewGroup, false);
            return new ExpenseHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExpenseHolder expenseHolder, int i) {
            cursor.moveToPosition(i);
            String date = cursor.getString(cursor.getColumnIndex("cdate"));
            String info = cursor.getString(cursor.getColumnIndex("info"));
            int amount = cursor.getInt(cursor.getColumnIndex("amount"));

            expenseHolder.tvItemDate.setText(date);
            expenseHolder.tvItemInfo.setText(info);
            expenseHolder.tvItemAmount.setText(String.valueOf(amount));
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }


        public class ExpenseHolder extends RecyclerView.ViewHolder {

            TextView tvItemDate;
            TextView tvItemInfo;
            TextView tvItemAmount;


            public ExpenseHolder(@NonNull View itemView) {
                super(itemView);

                tvItemDate = itemView.findViewById(R.id.tvItemDate);
                tvItemInfo = itemView.findViewById(R.id.tvItemInfo);
                tvItemAmount= itemView.findViewById(R.id.tvItemAmount);
            }
        }

    }
}
