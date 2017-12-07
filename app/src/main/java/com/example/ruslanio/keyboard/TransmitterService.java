package com.example.ruslanio.keyboard;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.ruslanio.keyboard.database.helper.DBHelper;
import com.example.ruslanio.keyboard.network.ApiManager;
import com.example.ruslanio.keyboard.network.body.AddDataRequestBody;
import com.example.ruslanio.keyboard.network.pojo.EmptyResult;

import java.sql.SQLClientInfoException;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ruslanio on 30.11.2017.
 */

public class TransmitterService extends IntentService {
    private static final int AWAIT_TIME = 5000;

    private DBHelper mDBHelper;
    private ApiManager mApiManager;
    private boolean mIsRunning;

    public TransmitterService() {
        super("KeyService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("onCreate()");
        mDBHelper = new DBHelper(getApplicationContext());
        mApiManager = ApiManager.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBHelper.close();
        mIsRunning = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while (mIsRunning) {
            mIsRunning = true;
            try {
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                Cursor cursor = db.query(DBHelper.TEXT_ENTITY_TABLE_NAME, null,
                        DBHelper.TextEntityTable.TEXT_ENTITY_STATUS + " = " + DBHelper.STATUS_CLIENT,
                        null, null, null, null);


                if (cursor.moveToFirst()) {
                    int id = cursor.getColumnIndex(DBHelper.TextEntityTable.TEXT_ENTITY_ID);
                    int text = cursor.getColumnIndex(DBHelper.TextEntityTable.TEXT_ENTITY_TEXT);
                    int date = cursor.getColumnIndex(DBHelper.TextEntityTable.TEXT_ENTITY_DATE);

                    do {
                        int currentId = cursor.getInt(id);
                        String currentText = cursor.getString(text);
                        String currentDate = cursor.getString(date);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.TextEntityTable.TEXT_ENTITY_ID, currentId);
                        contentValues.put(DBHelper.TextEntityTable.TEXT_ENTITY_TEXT, currentText);
                        contentValues.put(DBHelper.TextEntityTable.TEXT_ENTITY_DATE, currentDate);
                        contentValues.put(DBHelper.TextEntityTable.TEXT_ENTITY_STATUS, DBHelper.STATUS_SERVER);


                        if (currentText.equals("")) {
//                        AddDataRequestBody requestBody = new AddDataRequestBody(currentText);
                            Call<EmptyResult> call = mApiManager.postData(currentText);
                            Response response = call.execute();
                            if (response.isSuccessful()) {

                                System.out.println(response.code());
                                db.update(DBHelper.TEXT_ENTITY_TABLE_NAME, contentValues,
                                        DBHelper.TextEntityTable.TEXT_ENTITY_ID + " = " + currentId, null);
                            } else
                                System.out.println("SOMETHING WITH INTERNET");
                        }
                    } while (cursor.moveToNext());
                }
                Thread.sleep(AWAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("SOMETHING WITH THREADS");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("SOMETHING WITH CODE");
            }
        }
    }
}
