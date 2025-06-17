package com.example.workoutgen_v3;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final Context context = this;

    private FrameLayout frameLayout;
    private RecyclerView recyclerView;
    private MaterialButton generateButton, editButton, addedExercisesButton;

    private WorkoutFilter filter;
    private List<Exercise> workoutExercises = new ArrayList<>();




    private List<Category> categoryList = new ArrayList<>();
    private List<MuscleGroup> muscleGroupList = new ArrayList<>();
    private List<SkillLevel> skillLevelList = new ArrayList<>();
    private List<Movement> movementList = new ArrayList();
    private boolean setsRepsCheckBoxState = false, timeCheckBoxState = false;




    List<Category> tempCategoryList;
    List<MuscleGroup> tempMuscleGroupList;
    List<SkillLevel> tempSkillLevelList;
    List<Movement> tempMovementList;
    boolean tempSetsRepsCheckBoxState = false, tempTimeCheckBoxState = false;









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
//        DBHelper.getInstance(this).addColumn();

        recyclerView = findViewById(R.id.homePageRecyclerView);
        HomeExerciseAdapter adapter = new HomeExerciseAdapter(this, workoutExercises);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        generateButton = findViewById(R.id.homePageGenerateButton);
        generateButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    workoutExercises = WorkoutGenerator.generateWorkout(this, filter);
                    if(workoutExercises.isEmpty()){
                        Toast.makeText(context, "Unable to find exercises for this filter", Toast.LENGTH_SHORT).show();
                    }
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

        MaterialButton editCategoriesButton, editMuscleGroupsButton, editSkillLevelButton, editMovementButton;
        MaterialButton applyButton, cancelButton;


        LinearLayout categoryLinearLayout = dialog.findViewById(R.id.editGenDialogCategoryLinearLayout);
        LinearLayout muscleGroupLinearLayout = dialog.findViewById(R.id.editGenDialogMuscleGroupLinearLayout);
        LinearLayout skillLevelLinearLayout = dialog.findViewById(R.id.editGenDialogSkillLevelLinearLayout);
        LinearLayout movementLinearLayout = dialog.findViewById(R.id.editGenDialogMovementLinearLayout);





        MaterialCheckBox setsRepsCheckBox = dialog.findViewById(R.id.editGenDialogMetricSetsRepsCheckbox);
        setsRepsCheckBox.setChecked(setsRepsCheckBoxState);
        MaterialCheckBox timeCheckBox = dialog.findViewById(R.id.editGenDialogMetricTimeCheckbox);
        timeCheckBox.setChecked(timeCheckBoxState);

        setsRepsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tempSetsRepsCheckBoxState = true;
                }else{
                    tempSetsRepsCheckBoxState = false;
                }
            }
        });
        timeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tempTimeCheckBoxState = true;
                }else{
                    tempTimeCheckBoxState = false;
                }
            }
        });






        tempCategoryList = new ArrayList<>(categoryList);
        Util.updateDisplayedOptions(context, categoryLinearLayout, categoryList, Category.class);

        tempMuscleGroupList = new ArrayList<>(muscleGroupList);
        Util.updateDisplayedOptions(context, muscleGroupLinearLayout, muscleGroupList, MuscleGroup.class);

        tempSkillLevelList = new ArrayList<>(skillLevelList);
        Util.updateDisplayedOptions(context, skillLevelLinearLayout, skillLevelList, SkillLevel.class);

        tempMovementList = new ArrayList<>(movementList);
        Util.updateDisplayedOptions(context, movementLinearLayout, movementList, Movement.class);







        editCategoriesButton = dialog.findViewById(R.id.editGenDialogEditCategoryButton);
        editCategoriesButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog categoriesDialog = new Dialog(this);
                    categoriesDialog.setContentView(R.layout.categories_dialog);
                    Objects.requireNonNull(categoriesDialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_box);

                    LinearLayout linearLayout = categoriesDialog.findViewById(R.id.categoryDialogLinearLayout);
                    Util.loadSelectionDialogCheckboxes(context, linearLayout, tempCategoryList, Category.class);

                    Util.setCheckBoxChildClickAnimation(context, linearLayout);

                    categoriesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            tempCategoryList.clear();
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                MaterialCheckBox checkBox = (MaterialCheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                                    tempCategoryList.add(Category.fromString(checkBoxName));
                                }
                            }

                            Util.updateDisplayedOptions(context, categoryLinearLayout, tempCategoryList, Category.class);
                        }
                    });

                    categoriesDialog.show();

                }).start();
            }).start();

        });







        editMuscleGroupsButton = dialog.findViewById(R.id.editGenDialogEditMuscleGroupButton);
        editMuscleGroupsButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog muscleGroupDialog = new Dialog(this);
                    muscleGroupDialog.setContentView(R.layout.muscle_group_dialog);
                    Objects.requireNonNull(muscleGroupDialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_box);

                    LinearLayout linearLayout = muscleGroupDialog.findViewById(R.id.muscleGroupDialogLinearLayout);
                    Util.loadSelectionDialogCheckboxes(context, linearLayout, tempMuscleGroupList, MuscleGroup.class);

                    Util.setCheckBoxChildClickAnimation(context, linearLayout);

                    muscleGroupDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            tempMuscleGroupList.clear();
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                MaterialCheckBox checkBox = (MaterialCheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                                    tempMuscleGroupList.add(MuscleGroup.fromString(checkBoxName));
                                }
                            }

                            Util.updateDisplayedOptions(context, muscleGroupLinearLayout, tempMuscleGroupList, MuscleGroup.class);
                        }
                    });

                    muscleGroupDialog.show();

                }).start();
            }).start();

        });








        editSkillLevelButton = dialog.findViewById(R.id.editGenDialogEditSkillLevelButton);
        editSkillLevelButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog skillLevelDialog = new Dialog(this);
                    skillLevelDialog.setContentView(R.layout.skill_level_dialog);
                    Objects.requireNonNull(skillLevelDialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_box);

                    LinearLayout linearLayout = skillLevelDialog.findViewById(R.id.skillLevelDialogLinearLayout);
                    Util.loadSelectionDialogCheckboxes(context, linearLayout, tempSkillLevelList, SkillLevel.class);

                    Util.setCheckBoxChildClickAnimation(context, linearLayout);

                    skillLevelDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            tempSkillLevelList.clear();
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                MaterialCheckBox checkBox = (MaterialCheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                                    tempSkillLevelList.add(SkillLevel.fromString(checkBoxName));
                                }
                            }

                            Util.updateDisplayedOptions(context, skillLevelLinearLayout, tempSkillLevelList, SkillLevel.class);
                        }
                    });

                    skillLevelDialog.show();

                }).start();
            }).start();


        });




        editMovementButton = dialog.findViewById(R.id.editGenDialogEditMovementButton);
        editMovementButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog movementDialog = new Dialog(this);
                    movementDialog.setContentView(R.layout.movement_dialog);
                    Objects.requireNonNull(movementDialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_box);

                    LinearLayout linearLayout = movementDialog.findViewById(R.id.movementDialogLinearLayout);
                    Util.loadSelectionDialogCheckboxes(context, linearLayout, tempMovementList, Movement.class);

                    Util.setCheckBoxChildClickAnimation(context, linearLayout);

                    movementDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            tempMovementList.clear();
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                MaterialCheckBox checkBox = (MaterialCheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                                    tempMovementList.add(Movement.fromString(checkBoxName));
                                }
                            }

                            Util.updateDisplayedOptions(context, movementLinearLayout, tempMovementList, Movement.class);
                        }
                    });

                    movementDialog.show();

                }).start();
            }).start();
        });






        applyButton = dialog.findViewById(R.id.editGenDialogApplyButton);
        applyButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    categoryList = tempCategoryList;
                    muscleGroupList = tempMuscleGroupList;
                    skillLevelList = tempSkillLevelList;
                    movementList = tempMovementList;

                    setsRepsCheckBoxState = tempSetsRepsCheckBoxState;
                    timeCheckBoxState = tempTimeCheckBoxState;


                    setWorkoutFilterParams(
                            categoryList,
                            muscleGroupList,
                            skillLevelList,
                            movementList,
                            setsRepsCheckBoxState,
                            timeCheckBoxState
                    );

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

    private void setWorkoutFilterParams(
            List<Category> categoryList,
            List<MuscleGroup> muscleGroupList,
            List<SkillLevel> skillLevelList,
            List<Movement> movementList,
            boolean setsRepsCheckBoxState,
            boolean timeCheckBoxState
    ){
        filter = new WorkoutFilter();
        filter.setCategoryList(categoryList);
        filter.setMuscleGroupList(muscleGroupList);
        filter.setSkillLevelList(skillLevelList);
        filter.setMovementList(movementList);
        filter.setSetsRepsMetricState(setsRepsCheckBoxState);
        filter.setTimeMetricState(timeCheckBoxState);
    }

} // MainActivity ends here ========================================================================