package com.example.healthmonitoringapp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStreamReader;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.opencsv.CSVReader;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout topBar;
    private TextView appNameTextView;
    private Button symptomsBtn;
    private Button uploadSignsBtn;
    private ImageButton imageButton2;
    private Button measureHeartRateBtn;
    private Button measureRespiratoryRateBtn;
    public String heartRate;
    public String respiratoryRate;
    String sessionId = String.valueOf(new Date().getTime());
    MeasureHeartRate measureHeartRate = new MeasureHeartRate();
    MeasureRespRate measureRespRate = new MeasureRespRate();
    DBSQLiteConnection dbSQLiteConnection;
    HashMap<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topBar = findViewById(R.id.top_bar);
        appNameTextView = topBar.findViewById(R.id.appNameTextView);
        symptomsBtn = findViewById(R.id.symptomsBtn);
        uploadSignsBtn = findViewById(R.id.uploadSignsBtn);
        imageButton2 = findViewById(R.id.imageButton2);
        measureHeartRateBtn = findViewById(R.id.measureHeartRateBtn);
        measureRespiratoryRateBtn = findViewById(R.id.measureRespiratoryRateBtn);

        symptomsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondaryActivity.class);
                intent.putExtra("sessionVariableName", sessionId);
                intent.putExtra("sessionHeartRate", heartRate);
                intent.putExtra("sessionRespiratoryRate", respiratoryRate);
                startActivity(intent);
            }
        });

        uploadSignsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.put("Heart_Rate", heartRate);
                map.put("Respiratory_Rate", respiratoryRate);
                dbSQLiteConnection = new DBSQLiteConnection(MainActivity.this);
                dbSQLiteConnection.uploadData(map, sessionId);
                Toast.makeText(MainActivity.this, "Signs data saved to database", Toast.LENGTH_LONG).show();
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Placeholder for camera", Toast.LENGTH_LONG).show();
            }
        });

        measureHeartRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView heartRateText = findViewById(R.id.heartRateTextView);
                List<Bitmap> frameList = new ArrayList<>();
                Toast.makeText(MainActivity.this,"Calculating Heart Rate...",  Toast.LENGTH_LONG).show();
                Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.heartrate);
                MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
                metadataRetriever.setDataSource(MainActivity.this, path);
                String duration = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT);
                try {
                    int videoDuration = Integer.parseInt(duration);
                    int i = 10;
                    while (i < videoDuration) {
                        Bitmap frameBitmap = metadataRetriever.getFrameAtIndex(i);
                        frameList.add(frameBitmap);
                        i += 5;
                    }
                    Toast.makeText(MainActivity.this, "Generating Heart Rate in a moment..", Toast.LENGTH_LONG).show();
                    metadataRetriever.release();
                    heartRate = measureHeartRate.measureHRate(frameList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                heartRateText.setText(heartRate + "beats per minute");
            }
        });

        measureRespiratoryRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView respiratoryRateText = findViewById(R.id.respRateTextView);
                Toast.makeText(MainActivity.this,"Calculating Respiratory rate..",  Toast.LENGTH_LONG).show();
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(getResources().openRawResource(R.raw.resprate));
                    CSVReader reader = new CSVReader(inputStreamReader);
                    List<String[]> respRate = reader.readAll();
                    List<String[]> listX = respRate.subList (1,1288);
                    List<String[]> listY = respRate.subList (1281, 2560);
                    List<String[]> listZ = respRate.subList (2561, respRate.size());
                    respiratoryRate = measureRespRate.measureRRate(listX, listY, listZ);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                respiratoryRateText.setText(respiratoryRate + " breaths per minute");
            }
        });
    }
}