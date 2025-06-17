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

            int repsMultiplier = 0;
            int setsMultiplier = 0;
            int minutesMultiplier = 0;
            int secondsMultiplier = 0;

            switch (skillLevel) {
                case BEGINNER:
                    repsMultiplier = 5;
                    setsMultiplier = 3;
                    minutesMultiplier = 1;
                    secondsMultiplier = 2;
                    break;
                case INTERMEDIATE:
                    repsMultiplier = 5;
                    setsMultiplier = 3;
                    minutesMultiplier = 1;
                    secondsMultiplier = 3;
                    break;
                case ADVANCED:
                    repsMultiplier = 4;
                    setsMultiplier = 3;
                    minutesMultiplier = 2;
                    secondsMultiplier = 4;
                    break;
                case PRO:
                    repsMultiplier = 5;
                    setsMultiplier = 3;
                    minutesMultiplier = 2;
                    secondsMultiplier = 5;
                    break;
                default:
                    Log.e("WorkoutGenerator", "Invalid skill level.");
                    continue;
            }

            if(exercise.getMetric() == Metric.REPS){
                int reps = (random.nextInt(repsMultiplier) + 2) * 5;
                int sets = random.nextInt(setsMultiplier) + 2;
                exercise.setReps(reps);
                exercise.setSets(sets);
            } else if (exercise.getMetric() == Metric.TIME) {
                int sets = random.nextInt(setsMultiplier) + 1;
                int minutes = 0;
                int seconds = random.nextInt(8) + 3;

                if(!exercise.getSkillLevelList().contains(SkillLevel.PRO)){
                    minutes = random.nextInt( minutesMultiplier) + 1;
                    seconds = (random.nextInt(secondsMultiplier) + 1) * 10;
                }

                exercise.setSets(sets);
                exercise.setMinutes(minutes);
                exercise.setSeconds(seconds);
            }else{
                Log.e("WorkoutGenerator", "Unknown metric for exercise");
            }
        }
    }

}
