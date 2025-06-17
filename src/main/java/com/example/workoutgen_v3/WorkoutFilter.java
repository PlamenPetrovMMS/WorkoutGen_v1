package com.example.workoutgen_v3;

import java.util.ArrayList;
import java.util.List;

public class WorkoutFilter {
    private List<Category> categoryList = new ArrayList<>();
    private List<MuscleGroup> muscleGroupList  = new ArrayList<>();
    private List<SkillLevel> skillLevelList = new ArrayList<>();
    private List<Movement> movementList = new ArrayList<>();
    private Boolean setsRepsMetricState = false, timeMetricState = false;

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

    public void setSkillLevelList(List<SkillLevel> skillLevelList){
        this.skillLevelList = skillLevelList;
    }
    public List<SkillLevel> getSkillLevelList(){
        return skillLevelList;
    }

    public void setMovementList(List<Movement> movementList){
        this.movementList = movementList;
    }
    public List<Movement> getMovementList(){
        return movementList;
    }

    public void setSetsRepsMetricState(boolean setsRepsMetricState){
        this.setsRepsMetricState = setsRepsMetricState;
    }
    public boolean getSetsRepsMetricState(){
        return setsRepsMetricState;
    }

    public void setTimeMetricState(boolean timeMetricState){
        this.timeMetricState = timeMetricState;
    }
    public boolean getTimeMetricState(){
        return timeMetricState;
    }
}
