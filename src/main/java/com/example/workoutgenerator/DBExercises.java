package com.example.workoutgenerator;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBExercises extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Exercises.db";
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "exercise_table";


    private final String COLUMN_ID = "id";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_LEVEL = "level";
    private final String COLUMN_MOVEMENT_TYPE = "movement_type";
    private final String COLUMN_BODYPART_TYPE = "bodypart_type";
    private final String COLUMN_SYMMETRY = "symmetry";


    public DBExercises(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_LEVEL + " INTEGER," +
                COLUMN_MOVEMENT_TYPE + " INTEGER," +
                COLUMN_BODYPART_TYPE + " INTEGER," +
                COLUMN_SYMMETRY + " INTEGER" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // ===== GET FUNCTIONS =====
    public Cursor getAllExercisesCursor(){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC", null);
    }
    public Cursor getLevelExercisesCursor(int levelInt) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LEVEL + " = ?",
                new String[]{String.valueOf(levelInt)});
    }
    public Cursor getSpecificExercisesCursor(int levelInt, int movementInt, int bodypartInt){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_LEVEL + " = ? AND " +
                COLUMN_MOVEMENT_TYPE + " = ? AND " +
                COLUMN_BODYPART_TYPE + " = ?",
                new String[]{
                        String.valueOf(levelInt),
                        String.valueOf(movementInt),
                        String.valueOf(bodypartInt)
                });
    }

    @SuppressLint("Range")
    public List<Exercise> getAllExercises(){
        List<Exercise> exerciseList = new ArrayList<>();
        Cursor cursor = getAllExercisesCursor();

        if(cursor != null && cursor.moveToFirst()){
            do{
                int exerciseId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String exerciseName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));

                int exerciseLevelInt = cursor.getInt(cursor.getColumnIndex(COLUMN_LEVEL));
                Exercise.Level exerciseLevel = Exercise.Level.fromInt(exerciseLevelInt);

                int exerciseMovementTypeInt = cursor.getInt(cursor.getColumnIndex(COLUMN_MOVEMENT_TYPE));
                Exercise.MovementType exerciseMovementType = Exercise.MovementType.fromInt(exerciseMovementTypeInt);

                int exerciseBodypartTypeInt = cursor.getInt(cursor.getColumnIndex(COLUMN_BODYPART_TYPE));
                Exercise.BodypartType exerciseBodypartType = Exercise.BodypartType.fromInt(exerciseBodypartTypeInt);

                int exerciseSymmetryInt = cursor.getInt(cursor.getColumnIndex(COLUMN_SYMMETRY));
                Exercise.Symmetry exerciseSymmetry = Exercise.Symmetry.fromInt(exerciseSymmetryInt);

                Exercise exercise = new Exercise();
                exercise.setId(exerciseId);
                exercise.setName(exerciseName);
                exercise.setLevel(exerciseLevel);
                exercise.setMovementType(exerciseMovementType);
                exercise.setBodypartType(exerciseBodypartType);
                exercise.setSymmetry(exerciseSymmetry);

                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }

        if(exerciseList.isEmpty()){
            Log.e("DBExercises", "Exercise List is empty");
        }

        return exerciseList;
    }
    @SuppressLint("Range")
    public List<Exercise> getAllLevelExercises(Exercise.Level level){
        List<Exercise> exerciseList = new ArrayList<>();
        Cursor cursor = getLevelExercisesCursor(level.getValue());

        if(cursor != null && cursor.moveToFirst()){
            do{
                int exerciseId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String exerciseName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));

                int exerciseLevelInt = cursor.getInt(cursor.getColumnIndex(COLUMN_LEVEL));
                Exercise.Level exerciseLevel = Exercise.Level.fromInt(exerciseLevelInt);

                int exerciseMovementTypeInt = cursor.getInt(cursor.getColumnIndex(COLUMN_MOVEMENT_TYPE));
                Exercise.MovementType exerciseMovementType = Exercise.MovementType.fromInt(exerciseMovementTypeInt);

                int exerciseBodypartTypeInt = cursor.getInt(cursor.getColumnIndex(COLUMN_BODYPART_TYPE));
                Exercise.BodypartType exerciseBodypartType = Exercise.BodypartType.fromInt(exerciseBodypartTypeInt);

                Exercise exercise = new Exercise();
                exercise.setId(exerciseId);
                exercise.setName(exerciseName);
                exercise.setLevel(exerciseLevel);
                exercise.setMovementType(exerciseMovementType);
                exercise.setBodypartType(exerciseBodypartType);

                exerciseList.add(exercise);
            }while(cursor.moveToNext());

        }

        if(exerciseList.isEmpty()){
            Log.e("DBExercises", "Exercise List is empty");
        }

        return exerciseList;
    }

    @SuppressLint("Range")
    public List<Exercise> getAllSpecificExercises(Exercise.Level level, Exercise.MovementType movementType, Exercise.BodypartType bodypartType){
        List<Exercise> exerciseList = new ArrayList<>();
        Cursor cursor = getSpecificExercisesCursor(level.getValue(), movementType.getValue(), bodypartType.getValue());

        if(cursor != null && cursor.moveToFirst()){
            do{
                 int exerciseId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String exerciseName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));

                int exerciseLevelInt = cursor.getInt(cursor.getColumnIndex(COLUMN_LEVEL));
                Exercise.Level exerciseLevel = Exercise.Level.fromInt(exerciseLevelInt);

                int exerciseMovementTypeInt = cursor.getInt(cursor.getColumnIndex(COLUMN_MOVEMENT_TYPE));
                Exercise.MovementType exerciseMovementType = Exercise.MovementType.fromInt(exerciseMovementTypeInt);

                int exerciseBodypartTypeInt = cursor.getInt(cursor.getColumnIndex(COLUMN_BODYPART_TYPE));
                Exercise.BodypartType exerciseBodypartType = Exercise.BodypartType.fromInt(exerciseBodypartTypeInt);

                Exercise exercise = new Exercise();
                exercise.setId(exerciseId);
                exercise.setName(exerciseName);
                exercise.setLevel(exerciseLevel);
                exercise.setMovementType(exerciseMovementType);
                exercise.setBodypartType(exerciseBodypartType);

                exerciseList.add(exercise);
            }while(cursor.moveToNext());

        }

        if(exerciseList.isEmpty()){
            Log.e("DBExercises", "Exercise List is empty");
        }

        return exerciseList;
    }

    public boolean isExerciseNameTaken(String name){
        List<Exercise> exerciseList = getAllExercises();
        for(Exercise exercise: exerciseList){
            if(exercise.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    public boolean exerciseExists(Exercise exercise){
        List<Exercise> exerciseList = getAllExercises();
        return exerciseList.contains(exercise);
    }

    // ===== FUNCTIONS =====
    public void saveExercise(Exercise exercise){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, exercise.getName());
        values.put(COLUMN_LEVEL, exercise.getLevel().getValue());
        values.put(COLUMN_MOVEMENT_TYPE, exercise.getMovementType().getValue());
        values.put(COLUMN_BODYPART_TYPE, exercise.getBodypartType().getValue());
        values.put(COLUMN_SYMMETRY, exercise.getSymmetry().getValue());
        long id = database.insert(TABLE_NAME, null, values);
        database.close();
    }

    public void resetDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);  // Deletes all rows from the table
        Log.d("DBExercises", "Database was reset successfully");
        db.close();
    }


} // DBExercises ends here =========================================================================
