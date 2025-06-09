package com.example.workoutgen_v2;

import android.widget.CheckBox;

public class CheckboxHelper{
    private Category category;
    private CheckBox checkBox;

    public CheckboxHelper(Category category, CheckBox checkBox){
        this.category = category;
        this.checkBox = checkBox;
    }

    public Category getCategory() {
        return category;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}
