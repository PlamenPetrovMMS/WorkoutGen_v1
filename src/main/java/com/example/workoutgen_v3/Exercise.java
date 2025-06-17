package com.example.workoutgen_v3;

import java.util.List;

public class Exercise {
    private int id;
    private String name;
    private String description;
    private List<Category> categoryList;
    private List<MuscleGroup> muscleGroupList;
    private List<SkillLevel> skillLevelList;
    private Metric metric;
    private List<Movement> movementList;

    private int sets;
    private int reps;

    private int minutes;
    private int seconds;


    public Exercise(){}
    public Exercise(
            int id,
            String name,
            String description,
            List<Category> categoryList,
            List<MuscleGroup> muscleGroupList,
            List<SkillLevel> skillLevel,
            Metric metric,
            List<Movement> movement
    ){
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryList = categoryList;
        this.muscleGroupList = muscleGroupList;
        this.skillLevelList = skillLevel;
        this.metric = metric;
        this.movementList = movement;
    }


    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }

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
    public <T extends Enum<T>> String getItemListAsString(List<T> enumItemList, Class<T> enumClass){
        StringBuilder stringBuilder = new StringBuilder();
        for(T item: enumItemList){
            stringBuilder.append(item.name()).append(",");
        }
        return stringBuilder.toString();
    }

    public void setSkillLevelList(List<SkillLevel> skillLevelList){
        this.skillLevelList = skillLevelList;
    }

    public List<SkillLevel> getSkillLevelList(){
        return skillLevelList;
    }

    public void setSets(int sets){
        this.sets = sets;
    }
    public void setReps(int reps){
        this.reps = reps;
    }



    public int getSets(){
        return sets;
    }
    public int getReps(){
        return reps;
    }

    public void setMinutes(int minutes){
        this.minutes = minutes;
    }
    public int getMinutes(){
        return minutes;
    }

    public void setSeconds(int seconds){
        this.seconds = seconds;
    }
    public int getSeconds(){
        return seconds;
    }


    public void setMetric(Metric metric){
        this.metric = metric;
    }
    public Metric getMetric(){
        return metric;
    }


    public void setMovementList(List<Movement> movementList){
        this.movementList = movementList;
    }
    public List<Movement> getMovementList(){
        return movementList;
    }

}
