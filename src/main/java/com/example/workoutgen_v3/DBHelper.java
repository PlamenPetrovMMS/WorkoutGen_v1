package com.example.workoutgen_v3;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private final Context context;
    private static final String DATABASE_NAME = "WorkoutGen.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "exercise_table";


    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CATEGORIES = "categories";
    private static final String COLUMN_MUSCLE_GROUPS = "muscle_groups";
    private static final String COLUMN_SKILL_LEVEL = "skill_level";
    private static final String COLUMN_METRIC = "metric";
    private static final String COLUMN_MOVEMENT = "movement";



    private static DBHelper instance;
    private SQLiteDatabase mDatabase;

    private DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
                COLUMN_SKILL_LEVEL + " TEXT," +
                COLUMN_METRIC + " TEXT," +
                COLUMN_MOVEMENT + " TEXT" +
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
        values.put(COLUMN_SKILL_LEVEL, exercise.getItemListAsString(exercise.getSkillLevelList(), SkillLevel.class));
        values.put(COLUMN_METRIC, exercise.getMetric().name());
        values.put(COLUMN_MOVEMENT, exercise.getItemListAsString(exercise.getMovementList(), Movement.class));

        long rowId = database.insert(TABLE_NAME, null, values);

        if (rowId != -1) {
            System.out.println("Insert was successful");
        } else {
            System.out.println("Insert failed");
        }

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
                List<SkillLevel> exerciseSkillLevel = getEnumItemsListFromCursor(cursor, COLUMN_SKILL_LEVEL, SkillLevel.class);
                Metric exerciseMetric = Metric.fromString(cursor.getString(cursor.getColumnIndex(COLUMN_METRIC)));
                List<Movement> exerciseMovements = getEnumItemsListFromCursor(cursor, COLUMN_MOVEMENT, Movement.class);

                Exercise exercise = new Exercise(
                        exerciseId,
                        exerciseName,
                        exerciseDescription,
                        exerciseCategories,
                        exerciseMuscleGroups,
                        exerciseSkillLevel,
                        exerciseMetric,
                        exerciseMovements
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
            List<SkillLevel> exerciseSkillLevel = getEnumItemsListFromCursor(cursor, COLUMN_SKILL_LEVEL, SkillLevel.class);
            Metric exerciseMetric = Metric.fromString(cursor.getString(cursor.getColumnIndex(COLUMN_METRIC)));
            List<Movement> exerciseMovements = getEnumItemsListFromCursor(cursor, COLUMN_MOVEMENT, Movement.class);

            database.close();
            return new Exercise(
                    exerciseId,
                    exerciseName,
                    exerciseDescription,
                    exerciseCategories,
                    exerciseMuscleGroups,
                    exerciseSkillLevel,
                    exerciseMetric,
                    exerciseMovements
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

    public void updateItemById(int id, Exercise exercise){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, exercise.getName());
        values.put(COLUMN_DESCRIPTION, exercise.getDescription());
        values.put(COLUMN_CATEGORIES, exercise.getItemListAsString(exercise.getCategoryList(), Category.class));
        values.put(COLUMN_MUSCLE_GROUPS, exercise.getItemListAsString(exercise.getMuscleGroupList(), MuscleGroup.class));
        values.put(COLUMN_SKILL_LEVEL, exercise.getItemListAsString(exercise.getSkillLevelList(), SkillLevel.class));
        values.put(COLUMN_METRIC, exercise.getMetric().name());
        values.put(COLUMN_MOVEMENT, exercise.getItemListAsString(exercise.getMovementList(), Movement.class));

        database.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void removeItemById(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void resetDatabase(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
        database.close();
    }

    public void addColumn(){

        // LAST COLUMN ADDED : MOVEMENT

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_MOVEMENT + " TEXT");
        Toast.makeText(context, "Column " + COLUMN_MOVEMENT + " was added", Toast.LENGTH_SHORT).show();
        database.close();
    }
}
