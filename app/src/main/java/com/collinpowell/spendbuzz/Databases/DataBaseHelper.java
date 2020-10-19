package com.collinpowell.spendbuzz.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.collinpowell.spendbuzz.Models.IEModel;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "UserBudget.db";
    public static final String TABLE_NAME = "budget";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "type";
    public static final String COL_3 = "amount";
    public static final String COL_4 = "name";
    public static final String COL_5 = "period";
    public static final String COL_6 = "amount_received";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table budget " +
                        "(id text primary key, type text,amount text,name text, period text,amount_received text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS budget");
        onCreate(db);
    }

    public boolean insertContact(String Id, String name, String type, String amount, String period, String amount_received) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("ID", Id);
        contentValues.put("type", type);
        contentValues.put("amount", amount);
        contentValues.put("period", period);
        contentValues.put("amount_received", amount_received);
        db.insert("budget", null, contentValues);
        //this.mListener.onChange();
        return true;
    }

    public Cursor getData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from budget where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateContact(String id, String name, String type, String amount, String period, String amount_received) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("type", type);
        contentValues.put("amount", amount);
        contentValues.put("period", period);
        contentValues.put("amount_received", amount_received);
        db.update("budget", contentValues, "id = ? ", new String[]{id});
        //this.mListener.onChange();
        return true;
    }

    public Integer deleteContact(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete("budget",
                "id = ? ",
                new String[]{id});
    }

    public ArrayList<IEModel> getAllContacts() {
        ArrayList<IEModel> array_list = new ArrayList<IEModel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from budget", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            IEModel ieModel = new IEModel(res.getString(res.getColumnIndex(COL_4)),
                    res.getString(res.getColumnIndex(COL_2)),
                    Integer.parseInt(res.getString(res.getColumnIndex(COL_3))),
                    res.getString(res.getColumnIndex(COL_5)),
                    Integer.parseInt(res.getString(res.getColumnIndex(COL_6))));
            array_list.add(ieModel);
            res.moveToNext();
        }
        return array_list;
    }
}
