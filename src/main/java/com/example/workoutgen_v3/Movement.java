package com.example.workoutgen_v3;

public enum Movement {
    PUSH(0),
    PULL(1),
    HOLD(2),
    CURL(3),
    LIFT(4),
    DESCENT(5);

    private final int value;
    Movement(int value){
        this.value = value;
    }

    public static Movement fromInt(int value){
        for(Movement movement: Movement.values()){
            if(movement.value == value){
                return movement;
            }
        }
        throw new IllegalArgumentException("Invalid Movement value: " + value);
    }

    public static Movement fromString(String name){
        for(Movement movement: Movement.values()){
            if(movement.name().equals(name.trim().toUpperCase())){
                return movement;
            }
        }
        throw new IllegalArgumentException("Invalid Movement name: " + name);
    }
}
