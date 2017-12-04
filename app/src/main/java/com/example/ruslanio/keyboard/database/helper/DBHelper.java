package com.example.ruslanio.keyboard.database.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ruslanio on 04.12.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int STATUS_SERVER = 1;
    public static final int STATUS_CLIENT = 0;

    private static final int VERSION = 1;
    private static final String DB_NAME = "main_db";

    public static final String TEXT_ENTITY_TABLE_NAME = "table_text_entity";

    public static class TextEntityTable{
        public static final String TEXT_ENTITY_ID = "text_entity_id";
        public static final String TEXT_ENTITY_TEXT = "text_entity_text";
        public static final String TEXT_ENTITY_STATUS = "text_entity_status";
    }

    public DBHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TEXT_ENTITY_TABLE_NAME + " ("
                + TextEntityTable.TEXT_ENTITY_ID  + " integer primary key autoincrement,"
                + TextEntityTable.TEXT_ENTITY_TEXT + " text, "
                + TextEntityTable.TEXT_ENTITY_STATUS + " integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
