package com.example.workoutgen_v2;

import android.widget.CheckBox;

public class SkillgroupCheckBoxHelper {
    private UserSkillGroup skillGroup;
    private CheckBox checkBox;

    public SkillgroupCheckBoxHelper(UserSkillGroup userSkillGroup, CheckBox checkBox){
        this.skillGroup = userSkillGroup;
        this.checkBox = checkBox;
    }

    public UserSkillGroup getSkillGroup() {
        return skillGroup;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setSkillGroup(UserSkillGroup skillGroup) {
        this.skillGroup = skillGroup;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}
