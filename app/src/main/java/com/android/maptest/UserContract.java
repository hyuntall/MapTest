package com.android.maptest;

import android.location.Location;
import android.provider.BaseColumns;

public final class UserContract {
    public static final String DB_NAME="user.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UserContract() {}

    /* Inner class that defines the table contents */
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME="Users";
        public static final String SCHEDULE_YEAR = "Year";
        public static final String SCHEDULE_MONTH = "Month";
        public static final String SCHEDULE_DAY = "DAY";
        public static final String SCHEDULE_TITLE = "Title";
        public static final String START_TIME = "StartTIme";
        public static final String END_TIME = "EndTime";
        public static final String LAT = "lat";
        public static final String LNG = "lng";
        public static final String MEMO = "Memo";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                SCHEDULE_YEAR + TEXT_TYPE + COMMA_SEP +
                SCHEDULE_MONTH + TEXT_TYPE + COMMA_SEP +
                SCHEDULE_DAY + TEXT_TYPE + COMMA_SEP +
                SCHEDULE_TITLE + TEXT_TYPE + COMMA_SEP +
                START_TIME + TEXT_TYPE + COMMA_SEP +
                END_TIME + TEXT_TYPE + COMMA_SEP +
                LAT + TEXT_TYPE + COMMA_SEP +
                LNG + TEXT_TYPE + COMMA_SEP +
                MEMO + TEXT_TYPE +  " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}