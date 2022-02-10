package com.example.myapplication;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyXAxisValueFormatter implements IAxisValueFormatter {

    public static String getXValue(String dateInMillisecons, int index, ViewPortHandler viewPortHandler) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
            return sdf.format(new Date(Long.parseLong(dateInMillisecons)));

        } catch (Exception e) {

            return dateInMillisecons;
        }
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return null;
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
