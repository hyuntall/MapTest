package com.android.maptest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    final static String TAG="SQLiteDBTest";

    public DBHelper(Context context) {
        super(context, UserContract.DB_NAME, null, UserContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(UserContract.Users.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(UserContract.Users.DELETE_TABLE);
        onCreate(db);
    }

    public void insertUserBySQL(String date, String title, String startTime, String endTime, String location, String memo) {
        try {
            String sql = String.format (
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s) VALUES (NULL, '%s', '%s', '%s', '%s', '%s', '%s')",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users._ID,
                    UserContract.Users.SCHEDULE_YEAR,
                    UserContract.Users.SCHEDULE_MONTH,
                    UserContract.Users.SCHEDULE_DAY,
                    UserContract.Users.SCHEDULE_TITLE,
                    UserContract.Users.START_TIME,
                    UserContract.Users.END_TIME,
                    UserContract.Users.LAT,
                    UserContract.Users.LNG,
                    UserContract.Users.MEMO,
                    date,
                    title,
                    startTime,
                    endTime,
                    location,
                    memo);

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in inserting recodes");
        }
    }

    public Cursor getAllUsersBySQL(String scheduleYear) {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME +" WHERE year= '" + scheduleYear+"'";
        //"SELECT * FROM ORDER_T WHERE NAME='ppotta'" ;
        return getReadableDatabase().rawQuery(sql,null);
    }

    public Cursor getMonthUsersBySQL(String scheduleYear, String scheduleMonth) {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME +" WHERE year= '" + scheduleYear+"' AND Month= '" + scheduleMonth+"'";
        //"SELECT * FROM ORDER_T WHERE NAME='ppotta'" ;
        return getReadableDatabase().rawQuery(sql,null);
    }

    public Cursor getDayUsersBySQL(String scheduleYear, String scheduleMonth, String scheduleDay) {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME +" WHERE year= '" + scheduleYear+"' AND Month= '" + scheduleMonth+"' AND Day= '" + scheduleDay+"'";
        //"SELECT * FROM ORDER_T WHERE NAME='ppotta'" ;
        return getReadableDatabase().rawQuery(sql,null);
    }
    public Cursor getHourUsersBySQL(String scheduleYear, String scheduleMonth, String scheduleDay, String scheduleHour) {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME +" WHERE year= '"
                + scheduleYear+"' AND Month= '" + scheduleMonth+"' AND Day= '" + scheduleDay+"' AND StartTime= '" + scheduleHour+"'";
        //"SELECT * FROM ORDER_T WHERE NAME='ppotta'" ;
        return getReadableDatabase().rawQuery(sql,null);
    }


    public void deleteUserBySQL(String _id) {
        try {
            String sql = String.format (
                    "DELETE FROM %s WHERE %s = %s",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users._ID,
                    _id);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in deleting recodes");
        }
    }

    public void updateUserBySQL(String _id, String name, String phone) {
        try {
            String sql = String.format (
                    "UPDATE  %s SET %s = '%s', %s = '%s' WHERE %s = %s",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users.SCHEDULE_TITLE, name,
                    UserContract.Users.SCHEDULE_YEAR, phone,
                    UserContract.Users._ID, _id) ;
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in updating recodes");
        }
    }

    public long insertUserByMethod(String year, String month, String day, String title, String startTime, String endTime, String lat, String lng, String memo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.Users.SCHEDULE_YEAR,year);
        values.put(UserContract.Users.SCHEDULE_MONTH,month);
        values.put(UserContract.Users.SCHEDULE_DAY,day);
        values.put(UserContract.Users.SCHEDULE_TITLE, title);
        values.put(UserContract.Users.START_TIME, startTime);
        values.put(UserContract.Users.END_TIME,endTime);
        values.put(UserContract.Users.LAT,lat);
        values.put(UserContract.Users.LNG,lng);
        values.put(UserContract.Users.MEMO, memo);

        return db.insert(UserContract.Users.TABLE_NAME,null,values);
    }

    public Cursor getAllUsersByMethod(String scheduleDate) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(UserContract.Users.TABLE_NAME,null,UserContract.Users.SCHEDULE_YEAR,null,null,null,null);
    }

    public long deleteUserByMethod(String _id) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = UserContract.Users._ID +" = ?";
        String[] whereArgs ={_id};
        return db.delete(UserContract.Users.TABLE_NAME, whereClause, whereArgs);
    }

    public long updateUserByMethod(String _id, String name, String phone) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserContract.Users.SCHEDULE_TITLE, name);
        values.put(UserContract.Users.SCHEDULE_YEAR,phone);

        String whereClause = UserContract.Users._ID +" = ?";
        String[] whereArgs ={_id};

        return db.update(UserContract.Users.TABLE_NAME, values, whereClause, whereArgs);
    }

}