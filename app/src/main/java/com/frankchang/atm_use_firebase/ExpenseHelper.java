package com.frankchang.atm_use_firebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ExpenseHelper extends SQLiteOpenHelper {

    public ExpenseHelper(Context context) {
        this(context, "ATM", null, 1);
    }

    private ExpenseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                         int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE expense (_id INTEGER PRIMARY KEY NOT NULL, " +
                "cdate VARCHAR NOT NULL, info VARCHAR, amount INTEGRE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
