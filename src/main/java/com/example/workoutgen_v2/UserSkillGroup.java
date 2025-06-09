package com.example.workoutgen_v2;

public enum UserSkillGroup {
    BEGINNER(0),
    INTERMEDIATE(1),
    ADVANCED(2);

    private final int value;

    UserSkillGroup(int value){
        this.value = value;
    }
    public static UserSkillGroup fromInt(int value){
        for(UserSkillGroup skillGroup: UserSkillGroup.values()){
            if(skillGroup.value == value){
                return skillGroup;
            }
        }
        throw new IllegalArgumentException("Invalid UserSkillGroup value: " + value);
    }
}
