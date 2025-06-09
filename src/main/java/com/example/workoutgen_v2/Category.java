package com.example.workoutgen_v2;

public enum Category {
    NONE(0),
    STRENGTH(1),
    FLEXIBILITY(2),
    REHAB(3),
    BALANCE(4);

    private final int value;
    Category(int value){
        this.value = value;
    }
    public static Category fromInt(int value){
        for(Category category: Category.values()){
            if(category.value == value){
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid Category value: " + value);
    }
}
