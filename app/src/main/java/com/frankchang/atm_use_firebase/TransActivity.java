package com.frankchang.atm_use_firebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TransActivity extends AppCompatActivity {

    private List<Transaction> transactions;
    private RecyclerView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        findViews();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://atm201605.appspot.com/h").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 錯誤處理
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //parseJSON(body);
                        parseGSON(body);
                    }
                });
            }
        });

    }

    private void parseGSON(String json) {
        Gson gson = new Gson();
        transactions = gson.fromJson(json,
                new TypeToken<ArrayList<Transaction>>(){}.getType());

        TransAdapter adapter = new TransAdapter();
        list.setAdapter(adapter);
    }

    private void findViews() {
        list = findViewById(R.id.recyclerTrans);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    private void parseJSON(String json) {
        transactions = new ArrayList<>();

        try {
            JSONArray jArray = new JSONArray(json);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                transactions.add(new Transaction(jObject));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TransAdapter adapter = new TransAdapter();
        list.setAdapter(adapter);
    }


    public class TransAdapter extends RecyclerView.Adapter<TransAdapter.TransHolder> {

        @NonNull
        @Override
        public TransHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.item_trans, viewGroup, false);
            return new TransHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TransHolder transHolder, int i) {
            Transaction transaction = transactions.get(i);
            transHolder.tvDate.setText(transaction.getDate());
            transHolder.tvAmount.setText(String.valueOf(transaction.getAmount()));
            transHolder.tvType.setText(String.valueOf(transaction.getType()));
        }

        @Override
        public int getItemCount() {
            return transactions.size();
        }


        public class TransHolder extends RecyclerView.ViewHolder {

            TextView tvDate;
            TextView tvAmount;
            TextView tvType;


            public TransHolder(@NonNull View itemView) {
                super(itemView);

                tvDate = itemView.findViewById(R.id.tvTransDate);
                tvAmount = itemView.findViewById(R.id.tvTransAmount);
                tvType = itemView.findViewById(R.id.tvTransType);
            }
        }
    }

}
