package com.example.workoutgen_v3;

public enum SkillLevel {
    BEGINNER(0),
    INTERMEDIATE(1),
    ADVANCED(2),
    PRO(3);

    private final int value;
    SkillLevel(int value){
        this.value = value;
    }

    public static SkillLevel fromInt(int value){
        for(SkillLevel skillLevel: SkillLevel.values()){
            if(skillLevel.value == value){
                return skillLevel;
            }
        }
        throw new IllegalArgumentException("Invalid SkillLevel value: " + value);
    }

    public static SkillLevel fromString(String name){
        for(SkillLevel skillLevel: SkillLevel.values()){
            if(skillLevel.name().equals(name.trim().toUpperCase())){
                return skillLevel;
            }
        }
        throw new IllegalArgumentException("Invalid SkillLevel name: " + name);
    }
}
