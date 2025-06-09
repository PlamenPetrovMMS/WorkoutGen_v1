package com.example.workoutgen_v2;

public enum MuscleGroup {
    NONE(0),
    UPPERBODY(1),
    CORE(2),
    LOWERBODY(3),
    FULLBODY(4);

    private final int value;
    MuscleGroup(int value){
        this.value = value;
    }
    private static MuscleGroup fromInt(int value){
        for(MuscleGroup muscleGroup: MuscleGroup.values()){
            if(muscleGroup.value == value){
                return muscleGroup;
            }
        }
        throw new IllegalArgumentException("Invalid MuscleGroup value: " + value);
    }
}
