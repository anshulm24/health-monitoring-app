package com.example.healthmonitoringapp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBSQLiteConnection extends SQLiteOpenHelper {
    private static final String SymptomsDB_Name = "SymptomsDB";
    private static final String SymptomsDB_Table = "SymptomsTable";
    private static final int SymptomsDB_Version = 1;

    public DBSQLiteConnection(Context context) {
        super(context, SymptomsDB_Name, null, SymptomsDB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqldb) {
        String SQLLiteQuery = "CREATE TABLE " + SymptomsDB_Table + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "Created_On TEXT, "
                + "Session_ID TEXT, "
                + "Heart_Rate TEXT, "
                + "Respiratory_Rate TEXT, "
                + "Nausea TEXT, "
                + "Headache TEXT, "
                + "Diarrhea TEXT, "
                + "Soar_Throat TEXT, "
                + "Fever TEXT, "
                + "Muscle_Ache TEXT, "
                + "Loss_of_Smell_and_Taste TEXT, "
                + "Cough TEXT, "
                + "Shortness_of_Breath TEXT, "
                + "Feeling_Tired TEXT) ";
        sqldb.execSQL(SQLLiteQuery);
    }

    public void uploadData(HashMap<String, String> symptomsData, String sessionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        values.put("Created_On", String.valueOf(timestamp));
        values.put("Session_ID", sessionId);
        values.put("Heart_Rate", (symptomsData.get("Heart_Rate") != null ? symptomsData.get("Heart_Rate") : "0"));
        values.put("Respiratory_Rate", (symptomsData.get("Respiratory_Rate") != null ? symptomsData.get("Respiratory_Rate") : "0"));
        values.put("Nausea", (symptomsData.get("Nausea") != null ? symptomsData.get("Nausea") : "0"));
        values.put("Headache", (symptomsData.get("Headache") != null ? symptomsData.get("Headache") : "0"));
        values.put("Diarrhea", (symptomsData.get("Diarrhea") != null ? symptomsData.get("Diarrhea") : "0"));
        values.put("Soar_Throat", (symptomsData.get("Soar_Throat") != null ? symptomsData.get("Soar_Throat") : "0"));
        values.put("Fever", (symptomsData.get("Fever") != null ? symptomsData.get("Fever") : "0"));
        values.put("Muscle_Ache", (symptomsData.get("Muscle_Ache") != null ? symptomsData.get("Muscle_Ache") : "0"));
        values.put("Loss_of_Smell_and_Taste", (symptomsData.get("Loss_of_Smell_and_Taste") != null ? symptomsData.get("Loss_of_Smell_and_Taste") : "0"));
        values.put("Cough", (symptomsData.get("Cough") != null ? symptomsData.get("Cough") : "0"));
        values.put("Shortness_of_Breath", (symptomsData.get("Shortness_of_Breath") != null ? symptomsData.get("Shortness_of_Breath") : "0"));
        values.put("Feeling_Tired", (symptomsData.get("Feeling_Tired") != null ? symptomsData.get("Feeling_Tired") : "0"));

        Cursor cursor = db.query(SymptomsDB_Table, new String[]{"ID"}, "Session_ID = ?" , new String[]{sessionId}, null, null, null);
        if (cursor.moveToFirst()) {
            db.update(SymptomsDB_Table, values, "Session_ID = ?", new String[]{sessionId});
        } else {
            db.insert(SymptomsDB_Table, null, values);
        }
        cursor.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SymptomsDB_Table);
        onCreate(db);
    }
}