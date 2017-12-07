package com.example.ruslanio.keyboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruslanio.keyboard.database.helper.DBHelper;
import com.example.ruslanio.keyboard.network.ApiManager;
import com.example.ruslanio.keyboard.network.pojo.Result;
import com.example.ruslanio.keyboard.network.pojo.ServerResponce;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private TextView mResult;
    private View mMain;
    private Button mGetBb;
    private Button mStartService;
    private Button mGetRecords;
    private Button mNext;
    private ApiManager mApiManager;

    private Button mCharts;

    private DBHelper mDBHelper;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBHelper = new DBHelper(getApplicationContext());
        mApiManager = ApiManager.getInstance();

        mCharts = (Button) findViewById(R.id.charts);

        mCharts.setOnClickListener(btn -> {
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            startActivity(intent);
        });

        mGetBb = (Button) findViewById(R.id.btn_get_db);
        mResult = (TextView) findViewById(R.id.result);
//        mStartService = (Button) findViewById(R.id.btn_start_service);
//        mGetRecords = (Button) findViewById(R.id.btn_get_records);
//        mNext = (Button) findViewById(R.id.btn_next);

//        mStartService.setOnClickListener(btn -> {
//            System.out.println("onReceive()");
//            startService(new Intent(MainActivity.this,TransmitterService.class));
//        });
//
//        mNext.setOnClickListener(btn -> {
//            Intent intent = new Intent(MainActivity.this,ChartActivity.class);
//            startActivity(intent);
//        });
//
        mGetBb.setOnClickListener(btn -> {
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            Cursor cursor = db.query(DBHelper.TEXT_ENTITY_TABLE_NAME, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                int id = cursor.getColumnIndex(DBHelper.TextEntityTable.TEXT_ENTITY_ID);
                int text = cursor.getColumnIndex(DBHelper.TextEntityTable.TEXT_ENTITY_TEXT);
                int status = cursor.getColumnIndex(DBHelper.TextEntityTable.TEXT_ENTITY_STATUS);
                int date = cursor.getColumnIndex(DBHelper.TextEntityTable.TEXT_ENTITY_DATE);

                StringBuilder builder = new StringBuilder();

                do {
                    builder.append("ID = ").append(cursor.getInt(id)).append("\n")
                            .append("TEXT = ").append(cursor.getString(text)).append("\n")
                            .append("DATE = ").append(cursor.getString(date)).append("\n")
                            .append("STATUS = ").append(cursor.getInt(status)).append("\n");
                } while (cursor.moveToNext());

                mResult.setText(builder.toString());
            } else {
                Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
            }
        });
//
//        mGetRecords.setOnClickListener(btn -> {
//            mApiManager.getData()
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(serverResponce -> {
//                        List<Result> results = serverResponce.getResult();
//                        StringBuilder builder = new StringBuilder();
//                        for (Result result: results){
//                            builder.append("value : ").append(result.getValue()).append("\n")
//                                    .append("date : ").append(result.getDate()).append("\n");
//                        }
//                        mResult.setText(builder.toString());
//                    });
//        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDBHelper.close();
    }

    public class MainReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("onReceive()");
            startService(new Intent(MainActivity.this, TransmitterService.class));
        }
    }

}
