package com.frankchang.atm_use_firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class Transaction {

    String account;
    String date;
    int amount;
    int type;


    public Transaction(String account, String date, int amount, int type) {
        this.account = account;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public Transaction() {

    }

    public Transaction(JSONObject jObject) {
        try {
            account = jObject.getString("account");
            date = jObject.getString("date");
            amount = jObject.getInt("amount");
            type = jObject.getInt("type");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
