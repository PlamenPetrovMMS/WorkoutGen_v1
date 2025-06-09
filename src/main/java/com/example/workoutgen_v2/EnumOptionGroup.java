package com.example.workoutgen_v2;

public class EnumOptionGroup<T extends Enum<T>>{
    public final String name;
    public final T[] values;
    public final boolean singleSelection;

    public EnumOptionGroup(String name, T[] values, boolean singleSelection){
        this.name = name;
        this.values = values;
        this.singleSelection = singleSelection;
    }
}
