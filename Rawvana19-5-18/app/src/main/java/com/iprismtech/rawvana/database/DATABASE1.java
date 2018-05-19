package com.iprismtech.rawvana.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by udaykumar on 12-03-2018.
 */

public class DATABASE1 {
    private static final String DATABASE_NAME = "DataBase1";
    private static final int DATABASE_VERSION = 1;
    private DbHelper1 ourHelper;
    private Context ourContext;
    private SQLiteDatabase ourDatabase;
    private String STORAGE_PATH = "";
    private String Table1CreateQuery = "CREATE TABLE IF NOT EXISTS Table1 ( PrimID INTEGER PRIMARY KEY AUTOINCREMENT, ProductID INTEGER NOT NULL,  ProductName NVARCHAR(500) NOT NULL, Ingredients NVARCHAR(115000) NOT NULL);";


    private class DbHelper1 extends SQLiteOpenHelper {

        public DbHelper1(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
