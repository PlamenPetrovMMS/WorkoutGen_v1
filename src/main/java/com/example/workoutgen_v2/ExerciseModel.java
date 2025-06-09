package com.example.workoutgen_v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExerciseModel {
    private int id;
    private String name;
    private String description;
    private List<Category> categories;
    private List<MuscleGroup> muscleGroups;
    private UserSkillGroup userSkillGroup;


    public ExerciseModel(){}
    public ExerciseModel(
            int id,
            String name,
            String description,
            List<Category> categories,
            List<MuscleGroup> muscleGroups,
            UserSkillGroup userSkillGroup
    ){
        this.id = id;
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.muscleGroups = muscleGroups;
        this.userSkillGroup = userSkillGroup;
    }



    public String getDescription() {
        return description;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<Category> getCategories() {
        return categories;
    }
    public List<MuscleGroup> getMuscleGroups() {
        return muscleGroups;
    }
    public UserSkillGroup getUserSkillGroup() {
        return userSkillGroup;
    }



    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategories(List<Category> categories) {
        this.categories = new ArrayList<>();
        for(Category category: categories){
            if(category != Category.NONE){
                this.categories.add(category);
            }
        }
    }

    public void setMuscleGroups(List<MuscleGroup> muscleGroups) {
        this.muscleGroups = new ArrayList<>();
        for(MuscleGroup muscleGroup: muscleGroups){
            if(muscleGroup != MuscleGroup.NONE){
                this.muscleGroups.add(muscleGroup);
            }
        }
    }

    public void setUserSkillGroup(UserSkillGroup userSkillGroup) {
        this.userSkillGroup = userSkillGroup;
    }


}
