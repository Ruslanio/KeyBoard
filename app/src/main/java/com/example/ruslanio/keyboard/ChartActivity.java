package com.example.ruslanio.keyboard;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PathEffect;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.example.ruslanio.keyboard.database.helper.DBHelper;
import com.example.ruslanio.keyboard.network.ApiManager;
import com.example.ruslanio.keyboard.network.pojo.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartActivity extends AppCompatActivity {

    private static final String TITLE_YESTERDAY = "YESTERDAY";
    private static final String TITLE_TODAY = "TODAY";

    private ApiManager mApiManager;
    private DBHelper mDBHelper;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        mDBHelper = new DBHelper(getApplicationContext());
        mApiManager = ApiManager.getInstance();
        mViewPager = (ViewPager) findViewById(R.id.vp_charts);
        getData();
    }

    private void getData() {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();


        mApiManager.getData()
                .subscribe(serverResponce -> {
                    db.delete(DBHelper.EMOTION_ENTITY_TABLE_NAME, null, null);
                    List<Result> results = serverResponce.getResult();
                    for (Result result : results) {
                        double value = 0;
                        if (result.getValue().equals("NaN")) {
                            continue;
                        } else {
                            try {
                                value = Double.valueOf(result.getValue());
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        ContentValues cv = new ContentValues();
                        cv.put(DBHelper.EmotionEntityTable.EMOTION_VALUE, value);
                        cv.put(DBHelper.EmotionEntityTable.EMOTION_DATE, result.getDate());
                        db.insert(DBHelper.EMOTION_ENTITY_TABLE_NAME, null, cv);
                    }
                    new Handler(Looper.getMainLooper()).post(() -> {
                        mViewPager.setAdapter(new ChartPagerAdapter(getSupportFragmentManager()));
                    });

                }, throwable -> {
                    Toast.makeText(ChartActivity.this, "Connection problems!", Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDBHelper.close();
    }

    class ChartPagerAdapter extends FragmentPagerAdapter {

        public ChartPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ChartFragment.getInstance(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return TITLE_YESTERDAY;
            else
                return TITLE_TODAY;
        }
    }
}
