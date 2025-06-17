package com.example.workoutgen_v3;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WorkoutGenerator {
    public static List<Exercise> generateWorkout(Context context, WorkoutFilter workoutFilter) {

        List<Exercise> exerciseList = DBHelper.getInstance(context).getAllExercises();
        if(exerciseList == null){
            Log.d("WorkoutGenerator", "ExerciseList loaded from database returned NULL");
            exerciseList = new ArrayList<>();
        }



        if(workoutFilter == null){
            Log.e("WorkoutGenerator", "workoutFilter is NULL");
            if(!exerciseList.isEmpty()){
                setExerciseTimeOrReps(exerciseList);
            }else{
                Toast.makeText(context, "No added exercises", Toast.LENGTH_SHORT).show();
            }
            return exerciseList;
        }




        List<Category> filterCategoryList = workoutFilter.getCategoryList();
        List<MuscleGroup> filterMuscleGroupList = workoutFilter.getMuscleGroupList();
        List<SkillLevel> filterSkillLevelList = workoutFilter.getSkillLevelList();
        List<Movement> filterMovementList = workoutFilter.getMovementList();
        boolean filterSetsRepsMetricState = workoutFilter.getSetsRepsMetricState();
        boolean filterTimeMetricState = workoutFilter.getTimeMetricState();




        // Filter list state
        boolean skipThisCheck = true;
        boolean containsCategory, containsMuscleGroup, containsSkillLevel, containsMovement;


        List<Exercise> filteredExerciseList = new ArrayList<>();


        for(Exercise exercise: exerciseList){


            containsCategory = false;
            containsMuscleGroup = false;
            containsSkillLevel = false;
            containsMovement = false;




            if(!filterCategoryList.isEmpty()){
                for(Category filterCategory: filterCategoryList){
                    if(exercise.getCategoryList().contains(filterCategory) || exercise.getCategoryList().isEmpty()){
                        containsCategory = true;
                    }
                }
            }else{
                containsCategory = skipThisCheck;
            }




            if(!filterMuscleGroupList.isEmpty()){
                for(MuscleGroup filterMuscleGroup: filterMuscleGroupList){
                    if(exercise.getMuscleGroupList().contains(filterMuscleGroup) || exercise.getMuscleGroupList().isEmpty()){
                        containsMuscleGroup = true;
                    }
                }
            }else{
                containsMuscleGroup = skipThisCheck;
            }




            if(!filterSkillLevelList.isEmpty()) {
                for(SkillLevel filterSkillLevel: filterSkillLevelList){
                    if(exercise.getSkillLevelList().contains(filterSkillLevel) || exercise.getSkillLevelList().isEmpty()){
                        containsSkillLevel = true;
                    }
                }
            }else {
                containsSkillLevel = skipThisCheck;
            }




            if(!filterMovementList.isEmpty()){
                for(Movement filterMovement: filterMovementList){
                    if(exercise.getMovementList().contains(filterMovement) || exercise.getMovementList().isEmpty()){
                        containsMovement = true;
                    }
                }
            }else{
                containsMovement = skipThisCheck;
            }




            if(containsCategory && containsMuscleGroup && containsSkillLevel && containsMovement){

                // ADD EXERCISE WITH ANY METRIC (as long as filter metric states both are true or both are false)
                if((filterSetsRepsMetricState && filterTimeMetricState) || (!filterSetsRepsMetricState && !filterTimeMetricState)){
                    System.out.println(exercise.getName() + " added to list\n");
                    filteredExerciseList.add(exercise);
                }
                // ADD EXERCISE DEPENDING OF FILTER METRIC STATE + EXERCISE METRIC
                else if(filterSetsRepsMetricState && exercise.getMetric() == Metric.REPS){
                    System.out.println(exercise.getName() + " added to list\n");
                    filteredExerciseList.add(exercise);
                }else if (filterTimeMetricState && exercise.getMetric() == Metric.TIME){
                    System.out.println(exercise.getName() + " added to list\n");
                    filteredExerciseList.add(exercise);
                }
            }
        }

        if(filteredExerciseList.isEmpty()){
            Toast.makeText(context, "No found exercises for this filter", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }

        setExerciseTimeOrReps(filteredExerciseList);

        return filteredExerciseList;
    }

    private static void setExerciseTimeOrReps(List<Exercise> exerciseList){
        Random random = new Random();
        for(Exercise exercise: exerciseList){
            List<SkillLevel> skillLevels = exercise.getSkillLevelList();
            if(skillLevels == null || skillLevels.isEmpty()){
                Log.e("WorkoutGenerator", "Exercise has no skill levels");
                continue;
            }

            Collections.shuffle(skillLevels);

            SkillLevel skillLevel = skillLevels.get(0);

            int repsMin = 0, repsMax = 0;
            int setsMin = 0, setsMax = 0;
            int minsMin = 0, minsMax = 0;
            int secsMin = 0, secsMax = 0;

            switch (skillLevel) {
                case BEGINNER:
                    repsMin = 10; repsMax = 20;
                    setsMin = 3; setsMax = 5;
                    minsMin = 1; minsMax = 2;
                    secsMin = 0; secsMax = 30;
                    break;
                case INTERMEDIATE:
                    repsMin = 10; repsMax = 15;
                    setsMin = 2; setsMax = 4;
                    minsMin = 2; minsMax = 3;
                    secsMin = 15; secsMax = 45;
                    break;
                case ADVANCED:
                    repsMin = 8; repsMax = 12;
                    setsMin = 2; setsMax = 4;
                    minsMin = 3; minsMax = 5;
                    secsMin = 0; secsMax = 30;
                    break;
                case PRO:
                    repsMin = 5; repsMax = 8;
                    setsMin = 1; setsMax = 3;
                    minsMin = 4; minsMax = 6;
                    secsMin = 15; secsMax = 59;
                    break;
                default:
                    Log.e("WorkoutGenerator", "Invalid skill level.");
                    continue;
            }

            if(exercise.getMetric() == Metric.REPS){
                int reps = random.nextInt((repsMax - repsMin) + 1) + repsMin;
                int sets = random.nextInt((setsMax - setsMin) + 1) + setsMin;
                exercise.setReps(reps);
                exercise.setSets(sets);
            } else if (exercise.getMetric() == Metric.TIME) {
                int sets = random.nextInt((setsMax - setsMin) + 1) + setsMin;
                int minutes = random.nextInt((minsMax - minsMin) + 1) + minsMin;
                int seconds = random.nextInt((secsMax - secsMin) + 1) + secsMin;
                exercise.setSets(sets);
                exercise.setMinutes(minutes);
                exercise.setSeconds(seconds);
            }else{
                Log.e("WorkoutGenerator", "Unknown metric for exercise");
            }
        }
    }

}
