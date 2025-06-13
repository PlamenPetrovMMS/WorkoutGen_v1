package com.example.workoutgen_v3;

public enum MuscleGroup {
    UPPERBODY(0),
    CORE(1),
    LOWERBODY(2),
    FULLBODY(3);

    private final int value;
    MuscleGroup(int value){
        this.value = value;
    }

    public static MuscleGroup fromInt(int value){
        for(MuscleGroup muscleGroup: MuscleGroup.values()){
            if(muscleGroup.value == value){
                return muscleGroup;
            }
        }
        throw new IllegalArgumentException("Invalid MuscleGroup value: " + value);
    }

    public static MuscleGroup fromString(String name){
        for(MuscleGroup muscleGroup: MuscleGroup.values()){
            if(muscleGroup.name().equals(name.trim().toUpperCase())){
                return muscleGroup;
            }
        }
        throw new IllegalArgumentException("Invalid MuscleGroup name: " + name);
    }
}
