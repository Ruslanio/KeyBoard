package com.example.ruslanio.keyboard;

import android.graphics.PathEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mLineChartView = (LineChartView) findViewById(R.id.cv_main_chart);
        setUpChart();

    }

    private void setUpChart() {
        List<PointValue> values = new ArrayList<>();
        values.add(new PointValue(0, 1));
        values.add(new PointValue(1, 2));
        values.add(new PointValue(2, 4));
        values.add(new PointValue(3, 3));
        values.add(new PointValue(4, 4));
        values.add(new PointValue(5, -3));

        List<AxisValue> axisValuesX = new ArrayList<>();
        for (int i = 0;i < 10;i++){
            axisValuesX.add(new AxisValue(i));
        }

        List<AxisValue> axisValuesY = new ArrayList<>();
        for (float f = -1.0f; f <= 1.0f;f = f + 0.1f){
            axisValuesY.add(new AxisValue(f));
        }

        Axis axisX = new Axis();
        axisX.setHasLines(true);
        axisX.setHasSeparationLine(true);
        axisX.setValues(axisValuesX);
        axisX.setName("date");

        Axis axisY = new Axis();
        axisY.setValues(axisValuesY);
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

    private List<PointValue> generateValues(){
        Random random = new Random();
        random.nextFloat();

        return new ArrayList<>();
    }
}
