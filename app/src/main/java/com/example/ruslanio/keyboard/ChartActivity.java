package com.example.ruslanio.keyboard;

import android.annotation.SuppressLint;
import android.graphics.PathEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.annimon.stream.Stream;
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

import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartActivity extends AppCompatActivity {
    private LineChartView mLineChartView;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT;
    private static final SimpleDateFormat SIMPLE_DATE_VIEW_FORMAT;

    static {
        SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        SIMPLE_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        SIMPLE_DATE_VIEW_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        SIMPLE_DATE_VIEW_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mLineChartView = (LineChartView) findViewById(R.id.cv_main_chart);
        List<Result> results = new ArrayList<>();
        results.add(new Result("2017-12-06T12:01:12.152Z", 0.1));
        results.add(new Result("2017-12-07T12:01:12.152Z", 0.25));
        results.add(new Result("2017-12-08T12:01:12.152Z", -0.1));
        results.add(new Result("2017-12-08T12:11:12.152Z", 0.1));
        results.add(new Result("2017-12-05T12:01:12.152Z", 0.3666));
        results.add(new Result("2017-12-04T12:01:12.152Z", 0.12));
        results.add(new Result("2017-12-03T12:01:12.152Z", 0.92));
        results.add(new Result("2017-12-02T12:01:12.152Z", -0.98));
        setUpChart(results);
    }

    @SuppressLint("DefaultLocale")
    private void setUpChart(List<Result> resultList) {

        if (resultList != null) {
            List<PointValue> values = new ArrayList<>();
            Collections.sort(resultList, (o1, o2) -> {
                try {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(SIMPLE_DATE_FORMAT.parse(o1.getDate()));
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(SIMPLE_DATE_FORMAT.parse(o2.getDate()));
                    return calendar2.compareTo(calendar1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            });
            LinkedHashSet<AxisValue> axisValuesX = new LinkedHashSet<>();
            LinkedHashSet<AxisValue> axisValuesY = new LinkedHashSet<>();
            float count = 0;
            for (int i = 0; i < resultList.size(); i++) {
                Result result = resultList.get(i);
                float value = (float) (result.getValue().doubleValue());
                float dateValue = count + ((float) i / 10);
                values.add(new PointValue(dateValue, value));
                AxisValue axisValueX = new AxisValue(dateValue);
                try {
                    axisValueX.setLabel(SIMPLE_DATE_VIEW_FORMAT.format(SIMPLE_DATE_FORMAT.parse(result.getDate())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                axisValuesX.add(axisValueX);
            }
            for(float i=-1;i<=1;i+=0.1f){
                AxisValue axisValueY=new AxisValue(i);
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

    private List<PointValue> generateValues() {
        Random random = new Random();
        random.nextFloat();

        return new ArrayList<>();
    }
}
