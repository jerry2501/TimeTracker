package com.example.utkarsh.timetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String Database_name="time.db";
    public static final String table_name="data";

    public static final String col_1="NAME";
    public static final String col_2="VALUE";


    public DatabaseHelper(Context context) {
        super(context,Database_name,null,1);//creates Database
        //  SQLiteDatabase db = this.getWritableDatabase(); //creates database and table
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + table_name + " (NAME TEXT ,VALUE LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ table_name);
        onCreate(db);
    }
    public  boolean insertData(String name,long value)
    {
        SQLiteDatabase db = this.getWritableDatabase();//creates database and table
        ContentValues contentValues=new ContentValues();
        contentValues.put(col_1,name);
        contentValues.put(col_2,value);
        long result=db.insert(table_name,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }
    public Cursor getAlldata()
    {
        SQLiteDatabase db = this.getWritableDatabase();//creates database and table
        Cursor res=db.rawQuery("select * from "+table_name,null);
        return res;

    }
    public boolean updateData(String name,long value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(col_1,name);
        contentValues.put(col_2,value);
        db.update(table_name,contentValues,"NAME = ? ",new String[] {name});
        return true;
    }
    public Long getLimit(String pkg){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select VALUE from " + table_name + " where NAME='" + pkg + "';", null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("VALUE");
        if(cursor != null && cursor.getCount()>0){
            Long lim = cursor.getLong(index);
            cursor.close();
            return lim;
        }
        return -1L;

    }
    public Integer deleteData(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table_name,"NAME = ?",new String[] {name});
    }
}

