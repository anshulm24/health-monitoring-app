package com.example.healthmonitoringapp;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SecondaryActivity extends Activity {

    MainActivity mainactivity = new MainActivity();
    DBSQLiteConnection dbSQLiteConnection;
    HashMap<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signs);
        Intent intent = getIntent();
        String sessionId = intent.getStringExtra("sessionVariableName");
        String heartRate = intent.getStringExtra("sessionHeartRate");
        String respRate = intent.getStringExtra("sessionRespiratoryRate");

        Spinner symptomsSpinner = findViewById(R.id.symptoms_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.symptoms, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        symptomsSpinner.setAdapter(adapter);
        String[] array = getResources().getStringArray(R.array.symptoms);

        for (String element : array) {
            map.put(element.replace(" ", "_"), "0");
        }

        map.put("Heart_Rate", heartRate);
        map.put("Respiratory_Rate", respRate);

        RatingBar symptomsRatingBar = findViewById(R.id.rating_bar);

        symptomsRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String ratingString = String.valueOf(symptomsRatingBar.getRating());
                String symptom = symptomsSpinner.getSelectedItem().toString();
                map.put(symptom.replace(" ", "_"), ratingString);
                TextView temp = findViewById(R.id.text_view);
                temp.setText(symptom + " " + ratingString);
            }
        });

        Button uploadButton = findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbSQLiteConnection = new DBSQLiteConnection(SecondaryActivity.this);
                dbSQLiteConnection.uploadData(map, sessionId);
                Toast.makeText(SecondaryActivity.this, "Symptoms data saved to database", Toast.LENGTH_LONG).show();
            }
        });
    }
}
