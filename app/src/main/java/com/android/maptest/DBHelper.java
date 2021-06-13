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
    // 시간정보 없이 데이터베이스를 호출할 때 사용하는 함수
    public Cursor getDayUsersBySQL(String scheduleYear, String scheduleMonth, String scheduleDay) {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME +" WHERE year= '" + scheduleYear+"' AND Month= '" + scheduleMonth+"' AND Day= '" + scheduleDay+"'";
        //"SELECT * FROM ORDER_T WHERE NAME='ppotta'" ;
        return getReadableDatabase().rawQuery(sql,null);
    }
    // 시간정보를 포함하여 데이터베이스를 호출할 때 사용하는 함수
    public Cursor getHourUsersBySQL(String scheduleYear, String scheduleMonth, String scheduleDay, String scheduleHour) {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME +" WHERE year= '"
                + scheduleYear+"' AND Month= '" + scheduleMonth+"' AND Day= '" + scheduleDay+"' AND StartTime= '" + scheduleHour+"'";
        //"SELECT * FROM ORDER_T WHERE NAME='ppotta'" ;
        return getReadableDatabase().rawQuery(sql,null);
    }
    // 스케줄 액티비티에 적힌 내용을 데이터베이스에 저장하는 함수
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
    //스케줄 액티비티에 적힌 내용과 일치하는 데이터를 삭제하는 함수
    public long deleteUserByMethod(String _id) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = UserContract.Users._ID +" = ?";
        String[] whereArgs ={_id};
        return db.delete(UserContract.Users.TABLE_NAME, whereClause, whereArgs);
    }

}