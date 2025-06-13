package com.example.workoutgen_v3;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final Context CONTEXT = this;

    private FrameLayout frameLayout;
    private RecyclerView recyclerView;
    private MaterialButton generateButton, editButton, addedExercisesButton;

    private WorkoutFilter filter;
    private List<Exercise> workoutExercises = new ArrayList<>();

    private List<Category> categoryList = new ArrayList<>();
    private List<MuscleGroup> muscleGroupList = new ArrayList<>();
    private SkillLevel skillLevel = null;
    private SkillLevel tempSkillLevel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        DBHelper.getInstance(this).resetDatabase();


        recyclerView = findViewById(R.id.homePageRecyclerView);
        HomeExerciseAdapter adapter = new HomeExerciseAdapter(this, workoutExercises);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        generateButton = findViewById(R.id.homePageGenerateButton);
        generateButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    workoutExercises = WorkoutGenerator.generateWorkout(this, filter);
                    adapter.updateDataset(workoutExercises);

                }).start();
            }).start();
        });




        editButton = findViewById(R.id.homePageEditButton);
        editButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    showEditDialog();

                }).start();
            }).start();
        });




        addedExercisesButton = findViewById(R.id.homePageAddedExercisesButton);
        addedExercisesButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    startActivity(new Intent(this, ExerciseActivity.class));

                }).start();
            }).start();
        });

    } // onCreate ends here ========================================================================

    private void showEditDialog(){

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_workout_gen);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_box);

        Dialog categoriesDialog = new Dialog(this);
        categoriesDialog.setContentView(R.layout.categories_dialog);

        Dialog muscleGroupDialog = new Dialog(this);
        muscleGroupDialog.setContentView(R.layout.muscle_group_dialog);

        MaterialButton editCategoriesButton, editMuscleGroupsButton, editSkillLevelButton;
        TextView skillLevelResult;
        MaterialButton applyButton, cancelButton;

        List<MuscleGroup> tempMuscleGroupList = new ArrayList<>();
        List<Category> tempCategoryList = new ArrayList<>();




        skillLevelResult = dialog.findViewById(R.id.skillLevelResult);
        LinearLayout categoryLinearLayout = dialog.findViewById(R.id.editGenDialogCategoryLinearLayout);
        LinearLayout muscleGroupLinearLayout = dialog.findViewById(R.id.editGenDialogMuscleGroupLinearLayout);





        if(!this.categoryList.isEmpty()){
            updateGenLinearDialog(categoryLinearLayout, this.categoryList, Category.class);

            LinearLayout linearLayout = categoriesDialog.findViewById(R.id.categoriesDialogLayout);
            loadSelectionDialogCheckboxes(linearLayout, categoryList, Category.class);
        }else{
            TextView textView = new TextView(this);
            textView.setText("ANY");
            setLinearLayoutTextViewUI(textView);
            categoryLinearLayout.addView(textView);
        }
        if(!this.muscleGroupList.isEmpty()){
            updateGenLinearDialog(muscleGroupLinearLayout, this.muscleGroupList, MuscleGroup.class);

            LinearLayout linearLayout = muscleGroupDialog.findViewById(R.id.muscleGroupDialogLayout);
            loadSelectionDialogCheckboxes(linearLayout, muscleGroupList, MuscleGroup.class);
        }else{
            TextView textView = new TextView(this);
            textView.setText("ANY");
            setLinearLayoutTextViewUI(textView);
            muscleGroupLinearLayout.addView(textView);
        }
        if(this.skillLevel != null){
            skillLevelResult.setText(skillLevel.name());
        }else{
            skillLevelResult.setText("ANY");
        }

        skillLevelResult.setTextColor(ContextCompat.getColor(CONTEXT, R.color.white));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);
        params.addRule(RelativeLayout.BELOW, R.id.editGenDialogSkillLevelLabel);
        skillLevelResult.setLayoutParams(params);
        skillLevelResult.setBackgroundResource(R.drawable.orange_box);
        skillLevelResult.setTypeface(ResourcesCompat.getFont(CONTEXT, R.font.main_font));
        skillLevelResult.setTextSize(17);
        skillLevelResult.setLetterSpacing(0.04f);







        editCategoriesButton = dialog.findViewById(R.id.editGenDialogOpenCategorySelection);
        editCategoriesButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    LinearLayout linearLayout = categoriesDialog.findViewById(R.id.categoriesDialogLayout);

                    loadSelectionDialogCheckboxes(linearLayout, tempCategoryList, Category.class);

                    categoriesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            tempCategoryList.clear();
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                                    tempCategoryList.add(Category.fromString(checkBoxName));
                                }
                            }
                            if(!tempCategoryList.isEmpty()) updateGenLinearDialog(categoryLinearLayout, tempCategoryList, Category.class);
                            else{
                                TextView textView = new TextView(CONTEXT);
                                textView.setText("ANY");
                                setLinearLayoutTextViewUI(textView);
                                categoryLinearLayout.removeAllViews();
                                categoryLinearLayout.addView(textView);
                            }
                        }
                    });

                    categoriesDialog.show();

                }).start();
            }).start();

        });







        editMuscleGroupsButton = dialog.findViewById(R.id.editGenDialogOpenMuscleGroupSelection);
        editMuscleGroupsButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    LinearLayout linearLayout = muscleGroupDialog.findViewById(R.id.muscleGroupDialogLayout);

                    loadSelectionDialogCheckboxes(linearLayout, tempMuscleGroupList, MuscleGroup.class);

                    muscleGroupDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            tempMuscleGroupList.clear();
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                                    tempMuscleGroupList.add(MuscleGroup.fromString(checkBoxName));
                                }
                            }

                            if(!tempMuscleGroupList.isEmpty()) updateGenLinearDialog(muscleGroupLinearLayout, tempMuscleGroupList, MuscleGroup.class);
                            else{
                                TextView textView = new TextView(CONTEXT);
                                textView.setText("ANY");
                                setLinearLayoutTextViewUI(textView);
                                muscleGroupLinearLayout.removeAllViews();
                                muscleGroupLinearLayout.addView(textView);
                            }
                        }
                    });

                    muscleGroupDialog.show();

                }).start();
            }).start();

        });








        editSkillLevelButton = dialog.findViewById(R.id.editGenDialogOpenSkillLevelSelection);
        editSkillLevelButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog skillLevelDialog = new Dialog(this);
                    skillLevelDialog.setContentView(R.layout.skill_level_dialog);

                    LinearLayout linearLayout = skillLevelDialog.findViewById(R.id.skillLevelDialogLayout);

                    if(skillLevel != null){
                        for(int i = 0; i < linearLayout.getChildCount(); i++){
                            CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                            String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                            if(skillLevel.name().equals(checkBoxName)){
                                checkBox.setChecked(true);
                            }
                        }
                    }

                    for(int i = 0; i < linearLayout.getChildCount(); i++){
                        CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                        int currentCheckBoxIndex = i;
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    for(int j = 0; j < linearLayout.getChildCount(); j++){
                                        CheckBox uncheckedBox = (CheckBox) linearLayout.getChildAt(j);
                                        if(j != currentCheckBoxIndex){
                                            uncheckedBox.setChecked(false);
                                        }
                                    }
                                }
                            }
                        });
                    }

                    skillLevelDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                                    tempSkillLevel = SkillLevel.fromString(checkBoxName);
                                }
                            }

                            if(tempSkillLevel != null) skillLevelResult.setText(tempSkillLevel.name());
                            else skillLevelResult.setText("ANY");
                        }
                    });

                    skillLevelDialog.show();

                }).start();
            }).start();


        });











        applyButton = dialog.findViewById(R.id.editGenDialogApplyButton);
        applyButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    categoryList = tempCategoryList;
                    muscleGroupList = tempMuscleGroupList;
                    skillLevel = tempSkillLevel;

                    setWorkoutFilterParams(categoryList, muscleGroupList, skillLevel);

                    Toast.makeText(this, "Filter settings applied", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }).start();
            }).start();


        });









        cancelButton = dialog.findViewById(R.id.editGenDialogCancelButton);
        cancelButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    dialog.dismiss();

                }).start();
            }).start();
        });







        dialog.show();
    }

    private void setWorkoutFilterParams(List<Category> categoryList, List<MuscleGroup> muscleGroupList, SkillLevel skillLevel){
        filter = new WorkoutFilter();
        filter.setCategoryList(categoryList);
        filter.setMuscleGroupList(muscleGroupList);
        filter.setSkillLevel(skillLevel);
    }



    private <T extends Enum<T>> void loadSelectionDialogCheckboxes(LinearLayout linearLayout, List<T> enumItemList, Class<T> enumClass){
        if(!enumItemList.isEmpty()){
            for(T category: enumItemList){
                for(int i = 0; i < linearLayout.getChildCount(); i++){
                    CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                    if(category.name().equals(checkBox.getText().toString().trim().toUpperCase())){
                        checkBox.setChecked(true);
                    }
                }
            }
        }
    }


    private <T extends Enum<T>> void updateGenLinearDialog(LinearLayout linearLayout, List<T> enumItemList, Class<T> enumClass){
        linearLayout.removeAllViews();

        for(T enumItem: enumItemList){
            TextView textView = new TextView(this);
            textView.setText(enumItem.name());
            setLinearLayoutTextViewUI(textView);
            linearLayout.addView(textView);
        }

    }



    private void setLinearLayoutTextViewUI(TextView textView){
        textView.setTextColor(ContextCompat.getColor(CONTEXT, R.color.white));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 10, 0, 0);
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.drawable.orange_box);
        textView.setTypeface(ResourcesCompat.getFont(CONTEXT, R.font.main_font));
        textView.setLetterSpacing(0.04f);
        textView.setTextSize(17);
    }



} // MainActivity ends here ========================================================================