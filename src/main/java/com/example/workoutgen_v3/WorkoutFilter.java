package com.example.workoutgen_v3;

import java.util.ArrayList;
import java.util.List;

public class WorkoutFilter {
    private List<Category> categoryList = new ArrayList<>();
    private List<MuscleGroup> muscleGroupList  = new ArrayList<>();
    private SkillLevel skillLevel;

    public WorkoutFilter(){}

    public void setCategoryList(List<Category> categoryList){
        this.categoryList = categoryList;
    }
    public List<Category> getCategoryList(){
        return categoryList;
    }

    public void setMuscleGroupList(List<MuscleGroup> muscleGroupList){
        this.muscleGroupList = muscleGroupList;
    }
    public List<MuscleGroup> getMuscleGroupList(){
        return muscleGroupList;
    }

    public void setSkillLevel(SkillLevel skillLevel){
        this.skillLevel = skillLevel;
    }
    public SkillLevel getSkillLevel(){
        return skillLevel;
    }
}
