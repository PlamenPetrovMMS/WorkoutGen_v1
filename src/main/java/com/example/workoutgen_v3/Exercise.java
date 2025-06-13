package com.example.workoutgen_v3;

import java.util.List;

public class Exercise {
    private int id;
    private String name;
    private String description;
    private List<Category> categoryList;
    private List<MuscleGroup> muscleGroupList;
    private SkillLevel skillLevel;

    private int sets;
    private int reps;


    public Exercise(){}
    public Exercise(
            int id,
            String name,
            String description,
            List<Category> categoryList,
            List<MuscleGroup> muscleGroupList,
            SkillLevel skillLevel
    ){
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryList = categoryList;
        this.muscleGroupList = muscleGroupList;
        this.skillLevel = skillLevel;
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

    public void setSkillLevel(SkillLevel skillLevel){
        this.skillLevel = skillLevel;
    }
    public SkillLevel getSkillLevel(){
        return skillLevel;
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

}
