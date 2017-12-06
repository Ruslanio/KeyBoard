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

public class TransmitterService extends Service {
    private static final int AWAIT_TIME = 5000;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private DBHelper mDBHelper;
    private ApiManager mApiManager;
    private boolean mIsRunning;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("onCreate()");
        mDBHelper = new DBHelper(getApplicationContext());
        mApiManager = ApiManager.getInstance();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Observable.<Integer>fromCallable(() -> {
            if (!mIsRunning) {
                mIsRunning = true;
                while (mIsRunning) {
                    try {
                        SQLiteDatabase db = mDBHelper.getWritableDatabase();
                        Cursor cursor = db.query(DBHelper.TEXT_ENTITY_TABLE_NAME, null,
                                DBHelper.TextEntityTable.TEXT_ENTITY_STATUS + " = " + DBHelper.STATUS_CLIENT,
                                null, null, null, null);


                        if (cursor.moveToFirst()) {
                            int id = cursor.getColumnIndex(DBHelper.TextEntityTable.TEXT_ENTITY_ID);
                            int text = cursor.getColumnIndex(DBHelper.TextEntityTable.TEXT_ENTITY_TEXT);

                            do {
                                int currentId = cursor.getInt(id);
                                String currentText = cursor.getString(text);

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(DBHelper.TextEntityTable.TEXT_ENTITY_ID, currentId);
                                contentValues.put(DBHelper.TextEntityTable.TEXT_ENTITY_TEXT, currentText);
                                contentValues.put(DBHelper.TextEntityTable.TEXT_ENTITY_STATUS, DBHelper.STATUS_SERVER);


//                        AddDataRequestBody requestBody = new AddDataRequestBody(currentText);
                                Call<EmptyResult> call = mApiManager.postData(currentText);
                                Response response = call.execute();
                                if (response.isSuccessful()) {

                                    System.out.println(response.code());
                                    db.update(DBHelper.TEXT_ENTITY_TABLE_NAME, contentValues,
                                            DBHelper.TextEntityTable.TEXT_ENTITY_ID + " = " + currentId, null);
                                } else
                                    System.out.println("SOMETHING WITH INTERNET");

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
            return 1;
        })
                .subscribeOn(Schedulers.newThread())
                .subscribe();
        return START_STICKY;
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
}
