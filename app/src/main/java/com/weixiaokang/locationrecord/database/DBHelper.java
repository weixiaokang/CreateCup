package com.weixiaokang.locationrecord.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.weixiaokang.locationrecord.util.Constants;
import com.weixiaokang.locationrecord.util.LogUtil;

import java.util.ArrayList;

/**
 * coder:weixiaokang
 * time: 2014/10/12 23:40
 * creat a database
 */
public class DBHelper {

    /**
     * DATABASE_NAME the name of database
     * DATABASE_VERSION the version of database
     * TABLE_NAME the name of table created for this database
     */
    private static final String DATABASE_NAME = "datastorage.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "locationdata";
    private static final String ID = "_id";
    private static final String TIME = "time";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private SQLiteDatabase database;
    private static class DBOpenHelper extends SQLiteOpenHelper {
        private static final String CREATE_TABLE = "create table " + TABLE_NAME + " (" + ID + " integer primary key autoincrement, " + TIME + " text not null, " + LONGITUDE + " text not null, " + LATITUDE + " text not null);";
        public DBOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TABLE_NAME);
            onCreate(db);
        }
    }

    public DBHelper(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context);
        database = helper.getWritableDatabase();
    }

    /**
     * insert data, uesing content-value store key-value
     * @param locationData the data needed insert
     */
    public void add(LocationData locationData) {
        ContentValues values = new ContentValues();
        values.put(TIME, locationData.getTime());
        values.put(LONGITUDE, locationData.getLongitude());
        values.put(LATITUDE, locationData.getLatitude());
        database.insert(TABLE_NAME, null, values);
    }

    public ArrayList<LocationData> queryAll() {
        ArrayList<LocationData> list = null;
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null,null, null);
        if (cursor != null) {
            list = new ArrayList<LocationData>();
            while (cursor.moveToNext()) {
                LocationData locationData = new LocationData();
                locationData.setID(cursor.getInt(0));
                locationData.setTime(cursor.getString(1));
                locationData.setLongitude(cursor.getString(2));
                locationData.setLatitude(cursor.getString(3));
                list.add(locationData);
            }
        }
        return list;
    }
    /**
     * find the data by id
     * @param id the number of data
     * @return the data when found
     */
    public LocationData query(int id) {
        LocationData locationData = new LocationData();
        Cursor cursor = database.query(TABLE_NAME, null, ID + "=?", new String[] { String.valueOf(id) }, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            locationData.setID(cursor.getInt(0));
            locationData.setTime(cursor.getString(1));
            locationData.setLongitude(cursor.getString(2));
            locationData.setLatitude(cursor.getString(3));
            cursor.close();
            return locationData;
        }
        cursor.close();
        return null;
    }

    /**
     * delete the column of table by ids
     * @param ids the ids needed to delete
     */
    public void delete(Integer... ids) {
        if (ids.length > 0) {
            for (int i : ids) {
                database.delete(TABLE_NAME, ID + "=?", new String[] {Integer.toString(i)});
            }
        }
    }

    /**
     * get the number of data
     * @return the number
     */
    public long getCount() {
        Cursor cursor = database.rawQuery("select count "+ "(" + ID + ") " + "form " + TABLE_NAME, null);
        if (cursor.moveToNext()) {
            return cursor.getLong(0);
        }
        cursor.close();
        return 0;
    }

    /**
     * get the last data
     * @return the last data
     */
    public LocationData getLastDate() {
        LocationData locationData = new LocationData();
        Cursor cursor = database.query(DBHelper.TABLE_NAME,null,null,null,null,null,null);

        if (cursor.moveToLast()) {
            locationData.setID(cursor.getInt(0));
            locationData.setTime(cursor.getString(1));
            locationData.setLongitude(cursor.getString(2));
            locationData.setLatitude(cursor.getString(3));
        }
        cursor.close();
        return locationData;
    }

    /**
     * update data
     * @param locationData the data needed update
     */
    public void update(LocationData locationData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, locationData.getID());
        contentValues.put(TIME, locationData.getTime());
        contentValues.put(LONGITUDE, locationData.getLongitude());
        contentValues.put(LATITUDE, locationData.getLatitude());
        database.update(TABLE_NAME, contentValues, ID + "=?", new String[] {String.valueOf(locationData.getID())});
    }

    /**
     * destroy table
     * @param context the context of DBHelper
     */
    public void destroyForm(Context context) {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
        dbOpenHelper.onUpgrade(database, 1, 1);
    }
}
