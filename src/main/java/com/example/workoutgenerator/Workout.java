package com.example.workoutgenerator;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Workout {
    private final String SETS_TAG = "SETS";
    private final String REPS_TAG = "REPS";
    Map<Exercise, Map<String, Integer>> workoutMap = new HashMap<>();
    private int exerciseCount;
    private final Context CONTEXT;
    public enum Level{
        BEGINNER(0),
        INTERMEDIATE(1),
        PRO(2),
        ANY(3);

        private final int value;
        Level(int value){
            this.value = value;
        }
        public static Level fromString(String string){
            for(Level level: Level.values()){
                if(level.name().equals(string)){
                    return level;
                }
            }
            throw new IllegalArgumentException("Invalid workout level string: " + string);
        }
    }

    // ===== CONSTRUCTOR =====
    public Workout(Context context,
                   Level workoutLevel,
                   Exercise.MovementType exerciseMovementType,
                   Exercise.BodypartType exerciseBodypartType,
                   int exerciseCount){
        if(exerciseCount <= 0){
            throw new IllegalArgumentException("Invalid exercise count: " + exerciseCount);
        }

        this.exerciseCount = exerciseCount;
        CONTEXT = context;

        DBExercises exerciseDB = new DBExercises(context);

        List<Exercise> exercises = new ArrayList<>();
        List<Exercise> easyExercises, mediumExercises, hardExercises;

        Log.d("Workout", "================================================================");
        Log.d("Workout", "Filling workout object's exercise list");
        Log.d("Workout", "Workout level: " + workoutLevel.toString());
        try{
            switch(workoutLevel){
                case BEGINNER:
                    exercises = exerciseDB.getAllLevelExercises(Exercise.Level.EASY);
                    break;
                case INTERMEDIATE:
                    easyExercises = exerciseDB.getAllLevelExercises(Exercise.Level.EASY);
                    exercises.addAll(easyExercises);

                    mediumExercises = exerciseDB.getAllLevelExercises(Exercise.Level.MEDIUM);
                    exercises.addAll(mediumExercises);
                    break;
                case PRO:
                    mediumExercises = exerciseDB.getAllLevelExercises(Exercise.Level.MEDIUM);
                    exercises.addAll(mediumExercises);

                    hardExercises = exerciseDB.getAllLevelExercises(Exercise.Level.HARD);
                    exercises.addAll(hardExercises);
                    break;
            }
        }catch (NullPointerException npe){
            Toast.makeText(context, "No exercises in database", Toast.LENGTH_SHORT).show();
            npe.printStackTrace();
            return;
        }

        exerciseDB.close();

        if(exercises.isEmpty()){
            Log.e("Workout", "Exercises list is empty");
            Toast.makeText(context, "Exercises list is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("Workout", "Total exercises for level: " + workoutLevel + " = " + exercises.size());
        generateWorkoutExercises(exercises, exerciseMovementType, exerciseBodypartType);

    }

    // ===== GET METHODS =====
    public Map<Exercise, Map<String, Integer>> getWorkourMap(){
        return workoutMap;
    }


    // ===== FUNCTIONS =====
    private List<Exercise> populdateExerciseList(List<Exercise> exerciseList){
        Log.d("Workout", "Check for exerciseCount");
        if(exerciseList.isEmpty()){
            Log.d("Workout", "exerciseList is empty");
            return null;
        }
        while(exerciseList.size() <= exerciseCount){
            int index = new Random().nextInt(exerciseList.size());
            Exercise exercise = exerciseList.get(index);
            Log.d("Workout", "Cloning exercise");
            exerciseList.add(cloneExercise(exercise));
        }
        Log.d("Workout", "Exercise list new size: " + exerciseList.size());
        Collections.shuffle(exerciseList);
        Log.d("Workout", "Collection shuffled");
        return exerciseList.subList(0, exerciseCount);
    }
    public void generateWorkoutExercises(List<Exercise> exerciseList, Exercise.MovementType movementType, Exercise.BodypartType bodypartType){
        if(exerciseList.isEmpty()) throw new RuntimeException("Workout exercises list is empty");

        Log.d("Workout", " ");
        Log.d("Workout", "Movement Type: " + movementType.toString());
        Log.d("Workout", "Bodypart Type: " + bodypartType.toString());
        Log.d("Workout", "Exercise List count: " + exerciseList.size());
        List<Exercise> workoutExercises = new ArrayList<>();

        // FIX THIS MESS
        for(Exercise exercise: exerciseList){

            Log.d("Workout", "=====");
            Log.d("Workout", "Exercise name: " + exercise.getName());
            Log.d("Workout", "Exercise movement: " + exercise.getMovementType().toString());
            Log.d("Workout", "Exercise bodypart: " + exercise.getBodypartType().toString());
            Log.d("Workout", "=====");

            if(movementType == Exercise.MovementType.ANY && bodypartType == Exercise.BodypartType.ANY){

                Log.d("Workout", "Added exercise to workoutExercises list: " + exercise.getName());
                workoutExercises.add(exercise);
            }
            else if((exercise.getMovementType() == movementType || movementType == Exercise.MovementType.ANY) &&
                (exercise.getBodypartType() == bodypartType || bodypartType == Exercise.BodypartType.ANY)){

                Log.d("Workout", "Added exercise to workoutExercises list: " + exercise.getName());
                workoutExercises.add(exercise);
            }

            Log.d("Workout", " ");

        }
        Log.d("Workout", " ");
        Log.d("Workout", "Total exercises inside workoutExercises list: " + workoutExercises.size());
        try{
            workoutExercises = populdateExerciseList(workoutExercises);
            Log.d("Workout", "New workoutExercises size = " + workoutExercises.size());
            generateSetsAndReps(workoutExercises);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(CONTEXT, "Unable to generate workout with no exercises", Toast.LENGTH_SHORT).show();
        }

    }
    private void generateSetsAndReps(List<Exercise> exerciseList){
        if(exerciseList.isEmpty()){
            Log.e("Workout", "Workout Map is empty");
        }
        Log.d("Workout", "Generating sets and reps for each exercise");
        Log.d("Workout", "Exercise List size: " + exerciseList.size());
        for(Exercise exercise: exerciseList){
            Log.d("Workout", "Exercise: " + exercise.getName());
            Map<String, Integer> exerciseMap = generateExerciseSetsAndReps(exercise);
            workoutMap.put(exercise, exerciseMap);
        }

        if(workoutMap.isEmpty()) throw new RuntimeException("Workout Map is empty");

        Log.d("Workout", "Workout map size: " + workoutMap.size());

        Log.d("Workout", " ");
    }
    private Map<String, Integer> generateExerciseSetsAndReps(Exercise exercise){
        Random random = new Random();
        int sets, reps = 0;
        switch(exercise.getLevel()){
            case EASY:
                reps = random.nextInt(11) + 15;
                break;
            case MEDIUM:
                reps = random.nextInt(11) + 10;
                break;
            case HARD:
                reps = random.nextInt(11) + 5;
                break;
        }
        sets = random.nextInt(3) + 2;

        Map<String, Integer> exerciseMap = new HashMap<>();
        exerciseMap.put(SETS_TAG, sets);
        exerciseMap.put(REPS_TAG, reps);

        Log.d("Workout", "Exercise sets: " + sets + " // " +
                                    "Exercise reps: " + reps);

        return exerciseMap;
    }
    private Exercise cloneExercise(Exercise exercise){
        Exercise clone = new Exercise();

        clone.setName(exercise.getName());
        clone.setLevel(exercise.getLevel());
        clone.setMovementType(exercise.getMovementType());
        clone.setBodypartType(exercise.getBodypartType());
        clone.setSymmetry(exercise.getSymmetry());

        return clone;
    }


} // Workout class ends here =======================================================================
