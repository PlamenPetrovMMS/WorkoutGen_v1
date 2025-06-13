package com.example.workoutgen_v3;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WorkoutGen.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "exercise_table";


    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CATEGORIES = "categories";
    private static final String COLUMN_MUSCLE_GROUPS = "muscle_groups";
    private static final String COLUMN_SKILL_LEVEL = "skill_level";



    private static DBHelper instance;
    private SQLiteDatabase mDatabase;

    private DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static synchronized DBHelper getInstance(Context context) {
        try{
            if (instance == null) {
                instance = new DBHelper(context);
                instance.openDatabase(); // Ensure database is opened for read/write
            }
        }catch (NullPointerException e){
            Log.e("DBHelper", "Context passed to DBHelper.getInstance is NULL");
            e.printStackTrace();
        }
        return instance;
    }

    private void openDatabase() {
        if (mDatabase == null || !mDatabase.isOpen()) {
            mDatabase = getWritableDatabase(); // Opens for both read and write
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " VARCHAR," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_CATEGORIES + " TEXT," +
                COLUMN_MUSCLE_GROUPS + " TEXT," +
                COLUMN_SKILL_LEVEL + " TEXT" +
                ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void saveExercise(Exercise exercise){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, exercise.getName());
        values.put(COLUMN_DESCRIPTION, exercise.getDescription());
        values.put(COLUMN_CATEGORIES, exercise.getItemListAsString(exercise.getCategoryList(), Category.class));
        values.put(COLUMN_MUSCLE_GROUPS, exercise.getItemListAsString(exercise.getMuscleGroupList(), MuscleGroup.class));
        values.put(COLUMN_SKILL_LEVEL, exercise.getSkillLevel().name());

        database.insert(TABLE_NAME, null, values);
        database.close();
    }
    @SuppressLint("Range")
    public List<Exercise> getAllExercises(){
        List<Exercise> exerciseList = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                int exerciseId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String exerciseName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String exerciseDescription = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                List<Category> exerciseCategories = getEnumItemsListFromCursor(cursor, COLUMN_CATEGORIES, Category.class);
                List<MuscleGroup> exerciseMuscleGroups = getEnumItemsListFromCursor(cursor, COLUMN_MUSCLE_GROUPS, MuscleGroup.class);
                SkillLevel exerciseSkillLevel = SkillLevel.fromString(cursor.getString(cursor.getColumnIndex(COLUMN_SKILL_LEVEL)));

                Exercise exercise = new Exercise(
                        exerciseId,
                        exerciseName,
                        exerciseDescription,
                        exerciseCategories,
                        exerciseMuscleGroups,
                        exerciseSkillLevel
                );

                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }else{
            Log.e("DBHelper", "getAllExercises() -> Cursor is empty or NULL");
            database.close();
            return null;
        }

        database.close();
        return exerciseList;
    }

    @SuppressLint("Range")
    public Exercise getExerciseWithId(int id){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = ?", new String[]{String.valueOf(id)});

        if(cursor != null && cursor.moveToFirst()){
            int exerciseId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String exerciseName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String exerciseDescription = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            List<Category> exerciseCategories = getEnumItemsListFromCursor(cursor, COLUMN_CATEGORIES, Category.class);
            List<MuscleGroup> exerciseMuscleGroups = getEnumItemsListFromCursor(cursor, COLUMN_MUSCLE_GROUPS, MuscleGroup.class);
            SkillLevel exerciseSkillLevel = SkillLevel.fromString(cursor.getString(cursor.getColumnIndex(COLUMN_SKILL_LEVEL)));

            database.close();
            return new Exercise(
                    exerciseId,
                    exerciseName,
                    exerciseDescription,
                    exerciseCategories,
                    exerciseMuscleGroups,
                    exerciseSkillLevel
            );
        }else{
            Log.e("DBHelper", "getExerciseWithId() -> Cursor is empty or NULL");
        }

        database.close();
        return null;
    }

    @SuppressLint("Range")
    private List<Category> getExerciseCategoriesFromCursor(Cursor cursor){
        List<Category> categoryList = new ArrayList<>();
        String exerciseCategoriesString = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIES));
        String[] categoriesStringArray = exerciseCategoriesString.split(",");
        for(String categoryName: categoriesStringArray){
            if(!categoryName.trim().isBlank()){
                categoryList.add(Category.fromString(categoryName));
            }
        }
        return categoryList;
    }

    @SuppressLint("Range")
    private <T extends Enum<T>> List<T> getEnumItemsListFromCursor(Cursor cursor, String columnName, Class<T> enumClass){
        List<T> enumItemsList = new ArrayList<>();
        String cursorItemString = cursor.getString(cursor.getColumnIndex(columnName));
        String[] stringArray = cursorItemString.split(",");
        for(String itemName: stringArray){
            if(!itemName.trim().isBlank()){
                enumItemsList.add(T.valueOf(enumClass, itemName));
            }
        }
        return enumItemsList;
    }

    public void resetDatabase(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
        database.close();
    }
}
