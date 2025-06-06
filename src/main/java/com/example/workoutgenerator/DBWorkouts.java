package com.example.workoutgenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;

public class DBWorkouts extends SQLiteOpenHelper {
    private final Context CONTEXT;
    private static final String DATABASE_NAME = "Workouts.db";
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "workout_table";
    private final String COLUMN_ID = "id";
    private final String COLUMN_WORKOUT = "workout";


    public DBWorkouts(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        CONTEXT = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_WORKOUT + " TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void resetDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);  // Deletes all rows from the table
        db.close();
    }
    public void saveWorkout(Workout workout){
        Map<Exercise, Map<String, Integer>> workoutMap = workout.getWorkourMap();
        if(workoutMap.isEmpty()){
            Log.e("Workout", "Workout Map is empty");
        }
        SQLiteDatabase database = this.getWritableDatabase();
        Gson gson = new Gson();
        String workoutJson = gson.toJson(workoutMap);
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORKOUT, workoutJson);
        database.insert(TABLE_NAME, null, values);
        database.close();
    }
}
