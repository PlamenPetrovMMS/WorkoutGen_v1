package com.example.workoutgen_v3;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorkoutGenerator {
    public static List<Exercise> generateWorkout(Context context, WorkoutFilter workoutFilter) {

        List<Exercise> exerciseList = DBHelper.getInstance(context).getAllExercises();
        if(exerciseList == null) exerciseList = new ArrayList<>();

        if(workoutFilter == null){
            Random random = new Random();
            if(!exerciseList.isEmpty()){
                for(Exercise exercise: exerciseList){
                    exercise.setSets(random.nextInt(3) + 2);
                    exercise.setReps(random.nextInt(11) + 10);
                }
            }else{
                Toast.makeText(context, "No found exercises for this filter", Toast.LENGTH_SHORT).show();
            }
            return exerciseList;
        }

        List<Category> filterCategoryList = workoutFilter.getCategoryList();
        List<MuscleGroup> filterMuscleGroupList = workoutFilter.getMuscleGroupList();
        SkillLevel filterSkillLevel = workoutFilter.getSkillLevel();

        // Filter
        boolean skipThisCheck = true;
        boolean containsCategory, containsMuscleGroup, matcheshSkillSGroup;

        for(Exercise exercise: exerciseList){

            containsCategory = false;
            containsMuscleGroup = false;
            matcheshSkillSGroup = false;

            System.out.println("Exercise name: " + exercise.getName());
            System.out.println("Exercise categories: " + Arrays.toString(new List[]{exercise.getCategoryList()}));
            System.out.println("Exercise muscleGroups: " + Arrays.toString(new List[]{exercise.getMuscleGroupList()}));
            if(!filterCategoryList.isEmpty()){
                for(Category filterCategory: filterCategoryList){
                    if(exercise.getCategoryList().contains(filterCategory)){
                        containsCategory = true;
                    }
                }
            }else{
                containsCategory = skipThisCheck;
            }

            if(!filterMuscleGroupList.isEmpty()){
                for(MuscleGroup filterMuscleGroup: filterMuscleGroupList){
                    if(exercise.getMuscleGroupList().contains(filterMuscleGroup)){
                        containsMuscleGroup = true;
                    }
                }
            }else{
                containsMuscleGroup = skipThisCheck;
            }

            if(filterSkillLevel != null) {
                if (exercise.getSkillLevel() == filterSkillLevel) {
                    matcheshSkillSGroup = true;
                }
            }else {
                matcheshSkillSGroup = skipThisCheck;
            }

            if(containsCategory && containsMuscleGroup && matcheshSkillSGroup){
                System.out.println(exercise.getName() + " added to list\n");
                exerciseList.add(exercise);
            }
        }

        // Filter for muscle groups
        Random random = new Random();
        if(!exerciseList.isEmpty()){
            for(Exercise exercise: exerciseList){
                exercise.setSets(random.nextInt(3) + 2);
                exercise.setReps(random.nextInt(11) + 10);
            }
        }else{
            Toast.makeText(context, "No found exercises for this filter", Toast.LENGTH_SHORT).show();
        }

        return exerciseList;
    }

}
