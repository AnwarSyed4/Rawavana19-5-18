package com.iprismtech.rawvana.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.iprismtech.rawvana.R;
import com.iprismtech.rawvana.others.HelperObj;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by AapsTech It Solutions on 27-Jan-16.
 */
public class DataBase {

    private static final String DATABASE_NAME = "DataBase";
    private static final int DATABASE_VERSION = 1;
    private DbHelper ourHelper;
    private Context ourContext;
    private SQLiteDatabase ourDatabase;
    private String STORAGE_PATH = "";
    private String CartTableCreateQuery = "CREATE TABLE IF NOT EXISTS CartTable ( PrimID INTEGER PRIMARY KEY AUTOINCREMENT, ProductID INTEGER NOT NULL,  ProductName NVARCHAR(500) NOT NULL, Ingredients NVARCHAR(115000) NOT NULL);";
    private String FavouriteTableCreateQuery = "CREATE TABLE IF NOT EXISTS FavoriteTable ( PrimID INTEGER PRIMARY KEY AUTOINCREMENT, ProductID INTEGER NOT NULL, ProductName NVARCHAR(500) NOT NULL, Imagepath NVARCHAR(500) NOT NULL,InArray NVARCHAR(50000) NOT NULL,MethodArray NVARCHAR(50000) NOT NULL,Type NVARCHAR(500) NOT NULL,Des NVARCHAR(500) NOT NULL, Url NVARCHAR(500) NOT NULL );";



    private class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context, STORAGE_PATH+DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CartTableCreateQuery);
            db.execSQL(FavouriteTableCreateQuery);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + "CartTable");
            db.execSQL("DROP TABLE IF EXISTS " + "FavoriteTable");
            onCreate(db);
        }
    }
    public DataBase(Context c) {
        ourContext = c;
    }
    public DataBase open() throws SQLException {
        String appName = ourContext.getResources().getString(R.string.app_name);
        STORAGE_PATH = ourContext.getApplicationInfo().dataDir+"/"+appName+"/Database/";
        //STORAGE_PATH = Environment.getExternalStorageDirectory()+"/"+appName+"/Database/";
        File dir  = new File(STORAGE_PATH);
        if(!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        ourHelper.close();
    }

    public long Createfavorite(String id, String name, String image, String inArray, String methodArray, String type, String des,String url) {
        ContentValues cv = new ContentValues();
        if(!CheckProductExist(id)){
            cv.put("ProductID", id);
            cv.put("ProductName", name);
            cv.put("Imagepath", image);
            cv.put("InArray", inArray);
            cv.put("MethodArray", methodArray);
            cv.put("Type", type);
            cv.put("Des", des);
            cv.put("Url", url);
            return ourDatabase.insert("FavoriteTable", null, cv);
        } else {
            return 0;
        }
    }
    public long CartTable(String ProductID,String ProductName,String Ingredients) {
        ContentValues cv = new ContentValues();
        if(!CheckProductExistCart(ProductID)){
            cv.put("ProductID", ProductID);
            cv.put("ProductName", ProductName);
            cv.put("Ingredients", Ingredients);
            return ourDatabase.insert("CartTable", null, cv);
        } else {
            return 0;
        }
    }
    public JSONArray GetDataFavoriteTableAll() {
        String searchQuery;
        searchQuery = "SELECT * FROM FavoriteTable";
        Cursor cursor = ourDatabase.rawQuery(searchQuery,null);
        return HelperObj.getInstance().GetJSONArray(cursor);
    }
    public JSONArray GetDataCartTableAll() {
        String searchQuery;
        searchQuery = "SELECT * FROM CartTable";
        Cursor cursor = ourDatabase.rawQuery(searchQuery,null);
        return HelperObj.getInstance().GetJSONArray(cursor);
    }

    public boolean CheckProductExist(String ProductID) {
        String searchQuery = "SELECT * FROM FavoriteTable WHERE ProductID = '" + ProductID +"'";
        Cursor cursor = ourDatabase.rawQuery(searchQuery, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public boolean CheckProductExistCart(String ProductID) {
        String searchQuery = "SELECT * FROM CartTable WHERE ProductID = '" + ProductID +"'";
        Cursor cursor = ourDatabase.rawQuery(searchQuery, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public long DeleteFavoriteTableRow(String ProductID) {
        long result;
        result = ourDatabase.delete("FavoriteTable", "ProductID = ?", new String[]{ProductID});
        return result;
    }
    public long DeleteCartTableRow(String ProductID) {
        long result;
        result = ourDatabase.delete("CartTable", "ProductID = ?", new String[]{ProductID});
        return result;
    }
    public void deleteAll()
    {
        SQLiteDatabase db = ourHelper.getWritableDatabase();
        db.execSQL("delete from "+ "CartTable");
        db.close();
    }
}
