package com.example.workoutgen_v3;

public enum Category {
    STRENGTH(0),
    FLEXIBILITY(1),
    REHAB(2),
    BALANCE(3);

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

    public static Category fromString(String name){
        for(Category category: Category.values()){
            if(category.name().equals(name.trim().toUpperCase())){
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid Category name: " + name);
    }
}
