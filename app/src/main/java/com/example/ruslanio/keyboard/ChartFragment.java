package com.example.ruslanio.keyboard;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ruslanio.keyboard.database.helper.DBHelper;
import com.example.ruslanio.keyboard.network.pojo.Result;
import com.example.ruslanio.keyboard.util.DateManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Ruslanio on 06.12.2017.
 */

public class ChartFragment extends Fragment {

    private static final int YESTERDAY = 0;
    private static final int TODAY = 1;

    private DBHelper mDBHelper;
    private int pageNumber;
    private LineChartView mLineChartView;
    private static final String ARGUMENT_PAGE_NUMBER = "page_number";

    public static ChartFragment getInstance(int page) {
        ChartFragment chartFragment = new ChartFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        chartFragment.setArguments(arguments);
        return chartFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        mDBHelper = new DBHelper(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLineChartView = (LineChartView) getView().findViewById(R.id.main_line_chart);

        List<Result> resultList = new ArrayList<>();
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.EMOTION_ENTITY_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex(DBHelper.EmotionEntityTable.EMOTION_ID);
            int value = cursor.getColumnIndex(DBHelper.EmotionEntityTable.EMOTION_VALUE);
            int date = cursor.getColumnIndex(DBHelper.EmotionEntityTable.EMOTION_DATE);

            do {
                Double currentValue = cursor.getDouble(value);
                String currentDate = cursor.getString(date);
                resultList.add(new Result(currentDate, currentValue));
            } while (cursor.moveToNext());

        } else {
            Toast.makeText(getContext(), "no data", Toast.LENGTH_SHORT).show();
        }

        setUpChart(resultList);
    }

    @SuppressLint("DefaultLocale")
    private void setUpChart(List<Result> resultList) {
        if (resultList != null) {

            List<Result> preparedData = new ArrayList<>();
            switch (pageNumber) {
                case TODAY:
                    for (Result result : resultList) {
                        if (DateManager.isToday(result.getDate()))
                            preparedData.add(result);
                    }
                    break;
                case YESTERDAY:
                    for (Result result : resultList) {
                        if (DateManager.isYesterday(result.getDate()))
                            preparedData.add(result);
                    }
                    break;
            }

            List<PointValue> values = new ArrayList<>();
            Collections.sort(preparedData, (o1, o2) -> DateManager.compareDate(o1.getDate(), o2.getDate()));

            LinkedHashSet<AxisValue> axisValuesX = new LinkedHashSet<>();
            LinkedHashSet<AxisValue> axisValuesY = new LinkedHashSet<>();
            float count = 0;
            for (int i = 0; i < preparedData.size(); i++) {
                Result result = preparedData.get(i);
                float value = (float) (result.getValue().doubleValue());
                float dateValue = count + ((float) i / 10);
                values.add(new PointValue(dateValue, value));
                AxisValue axisValueX = new AxisValue(dateValue);

                //06.12 12.01
                DateManager.DateObject dateObject = DateManager.parseDate(result.getDate());
                StringBuilder builder = new StringBuilder();
                builder.append(dateObject.getHour()).append(":").append(dateObject.getMinute());
                axisValueX.setLabel(builder.toString());

                axisValuesX.add(axisValueX);
            }
            for (float i = -1; i <= 1; i += 0.1f) {
                AxisValue axisValueY = new AxisValue(i);
                axisValueY.setLabel(String.format("%.1f", i));
                axisValuesY.add(axisValueY);
            }

            List<AxisValue> axisValueListX = new ArrayList<>();
            axisValueListX.addAll(axisValuesX);
            List<AxisValue> axisValueListY = new ArrayList<>();
            axisValueListY.addAll(axisValuesY);

            Axis axisX = new Axis();
            axisX.setHasLines(true);
            axisX.setHasSeparationLine(true);
            axisX.setValues(axisValueListX);
            axisX.setName("date");

            Axis axisY = new Axis();
            axisY.setValues(axisValueListY);
            axisY.setHasLines(true);
            axisY.setHasSeparationLine(true);
            axisY.setName("value");

            Line line = new Line(values);
            line.setColor(getResources().getColor(R.color.colorPrimary));
            line.setFilled(true);

            List<Line> lines = new ArrayList<>();
            lines.add(line);

            LineChartData data = new LineChartData();
            data.setLines(lines);
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);

            mLineChartView.setLineChartData(data);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBHelper.close();
    }

}
