package com.example.workoutgen_v3;

public enum Metric {
    REPS(0),
    TIME(1);

    private final int value;

    Metric(int value){
        this.value = value;
    }

    public static Metric fromInt(int value){
        for(Metric metric: Metric.values()){
            if(metric.value == value){
                return metric;
            }
        }
        throw new IllegalArgumentException("Invalid Metric value: " + value);
    }

    public static Metric fromString(String name){
        for(Metric metric: Metric.values()){
            if(metric.name().equals(name.trim().toUpperCase())){
                return metric;
            }
        }
        throw new IllegalArgumentException("Invalid Metric name: " + name);
    }
}
