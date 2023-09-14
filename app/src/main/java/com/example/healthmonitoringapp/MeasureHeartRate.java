package com.example.healthmonitoringapp;

import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;

public class MeasureHeartRate extends Activity {

    public String measureHRate (List<Bitmap> framelist) {
        int rate = 0;
        try {
            long redBucket;
            List<Long> a = new ArrayList<>();

            for (Bitmap frame : framelist) {
                redBucket = 0;

                int height = frame.getHeight();
                int width = frame.getWidth();

                for (int y = height - 100; y < frame.getHeight() - 1; y++) {
                    for (int x = width - 100; x < frame.getWidth() - 1; x++) {
                        int c = frame.getPixel(x, y);
                        redBucket += Color.red(c) + Color.blue(c) + Color.green(c);
                    }
                }
                a.add(redBucket);
            }

            List<Long> b = new ArrayList<>();
            for (int i = 0; i < a.size() - 5; i++) {
                long temp = (a.get(i) + a.get(i + 1) + a.get(i + 2) + a.get(i + 3) + a.get(i + 4)) / 4;
                b.add(temp);
            }

            long x = b.get(0);
            int count = 0;
            for (int i = 1; i < b.size(); i++) {
                long p = b.get(i);
                if ((p - x) > 3500) {
                    count++;
                }
                x = b.get(i);
                rate = (int) ((count / 45.0f)*60);
            }
        }
        catch(Exception exp){
            exp.printStackTrace();
        }
        return String.valueOf(rate/2);
    }
}