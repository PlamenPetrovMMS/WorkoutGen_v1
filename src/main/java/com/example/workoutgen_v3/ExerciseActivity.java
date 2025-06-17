package com.example.workoutgen_v3;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
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

public class ExerciseActivity extends AppCompatActivity {
    Context context = this;
    RecyclerView recyclerView;
    AddedExerciseAdapter adapter;
    MaterialButton createButton;
    List<Exercise> addedExercises;


    SearchView searchView;
    LinearLayout categoryLinearLayout, muscleGroupLinearLayout, metricLinearLayout, skillLevelLinearLayout, movementLinearLayout;
    MaterialButton editCategoriesButton, editMuscleGroupsButton, editSkillLevelButton, editMovementButton;
    MaterialCheckBox setsRepsCheckbox, timeCheckbox;
    MaterialButton cancelButton, addButton;


    List<Category> categoryList = new ArrayList<>();
    List<MuscleGroup> muscleGroupList = new ArrayList<>();
    List<SkillLevel> skillLevelList = new ArrayList<>();
    List<Movement> movementList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exercise);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addedExercises = DBHelper.getInstance(this).getAllExercises();
        if(addedExercises == null) addedExercises = new ArrayList<>();
        else addedExercises.sort((e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()));

        searchView = findViewById(R.id.exerciseActivitySearch);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        recyclerView = findViewById(R.id.exerciseActivityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddedExerciseAdapter(this, addedExercises);
        recyclerView.setAdapter(adapter);





        createButton = findViewById(R.id.exerciseActivityCreateButton);
        createButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.activity_create_exercise);
                    initializeDialog(dialog);
                    dialog.show();

                }).start();
            }).start();
        });

    } // onCreate ends here ========================================================================

    private void initializeDialog(Dialog dialog){

        EditText nameInput = dialog.findViewById(R.id.addExerciseNameInput);
        EditText descriptionInput = dialog.findViewById(R.id.addExerciseDescriptionInput);

        categoryLinearLayout = dialog.findViewById(R.id.addExerciseCategoryLinearLayout);
        muscleGroupLinearLayout = dialog.findViewById(R.id.addExerciseMuscleGroupLinearLayout);
        skillLevelLinearLayout = dialog.findViewById(R.id.addExerciseSkillLevelLinearLayout);
        movementLinearLayout = dialog.findViewById(R.id.addExerciseMovementLinearLayout);

        metricLinearLayout = dialog.findViewById(R.id.addExerciseMetricLinearLayout);
        setsRepsCheckbox = dialog.findViewById(R.id.addExerciseMetricSetsRepsCheckbox);
        timeCheckbox = dialog.findViewById(R.id.addExerciseMetricTimeCheckbox);
        Util.setLinearLayoutMaterialCheckboxListener(metricLinearLayout);













        categoryList.clear();
        muscleGroupList.clear();
        movementList.clear();
        skillLevelList.clear();




        editCategoriesButton = dialog.findViewById(R.id.addExerciseEditCategoryButton);
        editCategoriesButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog categoriesDialog = new Dialog(this);
                    categoriesDialog.setContentView(R.layout.categories_dialog);
                    Objects.requireNonNull(categoriesDialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_box);

                    LinearLayout linearLayout = categoriesDialog.findViewById(R.id.categoryDialogLinearLayout);
                    Util.loadSelectionDialogCheckboxes(context, linearLayout, categoryList, Category.class);

                    Util.setCheckBoxChildClickAnimation(context, linearLayout);

                    categoriesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            categoryList.clear();
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                                    categoryList.add(Category.fromString(checkBoxName));
                                }
                            }

                            Util.updateDisplayedOptions(context, categoryLinearLayout, categoryList, Category.class);

                        }
                    });

                    categoriesDialog.show();

                }).start();
            }).start();

        });








        editMuscleGroupsButton = dialog.findViewById(R.id.addExerciseEditMuscleGroupButton);
        editMuscleGroupsButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog muscleGroupDialog = new Dialog(this);
                    muscleGroupDialog.setContentView(R.layout.muscle_group_dialog);
                    Objects.requireNonNull(muscleGroupDialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_box);

                    LinearLayout linearLayout = muscleGroupDialog.findViewById(R.id.muscleGroupDialogLinearLayout);
                    Util.loadSelectionDialogCheckboxes(context, linearLayout, muscleGroupList, MuscleGroup.class);

                    Util.setCheckBoxChildClickAnimation(context, linearLayout);

                    muscleGroupDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            muscleGroupList.clear();
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                MaterialCheckBox checkBox = (MaterialCheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                                    muscleGroupList.add(MuscleGroup.fromString(checkBoxName));
                                }
                            }

                            Util.updateDisplayedOptions(context, muscleGroupLinearLayout, muscleGroupList, MuscleGroup.class);
                        }
                    });

                    muscleGroupDialog.show();

                }).start();
            }).start();

        });








        editSkillLevelButton = dialog.findViewById(R.id.addExerciseEditSkillLevelButton);
        editSkillLevelButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog skillLevelDialog = new Dialog(this);
                    skillLevelDialog.setContentView(R.layout.skill_level_dialog);
                    Objects.requireNonNull(skillLevelDialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_box);

                    LinearLayout linearLayout = skillLevelDialog.findViewById(R.id.skillLevelDialogLinearLayout);
                    Util.loadSelectionDialogCheckboxes(context, linearLayout, skillLevelList, SkillLevel.class);

                    Util.setCheckBoxChildClickAnimation(context, linearLayout);

                    skillLevelDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            skillLevelList.clear();
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                MaterialCheckBox checkBox = (MaterialCheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                                    skillLevelList.add(SkillLevel.fromString(checkBoxName));
                                }
                            }

                            Util.updateDisplayedOptions(context, skillLevelLinearLayout, skillLevelList, SkillLevel.class);

                        }
                    });

                    skillLevelDialog.show();

                }).start();
            }).start();

        });





        editMovementButton = dialog.findViewById(R.id.addExerciseEditMovementButton);
        editMovementButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog movementDialog = new Dialog(context);
                    movementDialog.setContentView(R.layout.movement_dialog);
                    Objects.requireNonNull(movementDialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_box);

                    LinearLayout linearLayout = movementDialog.findViewById(R.id.movementDialogLinearLayout);
                    Util.loadSelectionDialogCheckboxes(context, linearLayout, movementList, Movement.class);

                    Util.setCheckBoxChildClickAnimation(context, linearLayout);

                    movementDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            movementList.clear();
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                MaterialCheckBox checkBox = (MaterialCheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    movementList.add(Movement.fromString(checkBox.getText().toString()));
                                }
                            }

                            Util.updateDisplayedOptions(context, movementLinearLayout, movementList, Movement.class);

                        }
                    });

                    movementDialog.show();

                }).start();
            }).start();
        });






        cancelButton = dialog.findViewById(R.id.addExerciseCancelButton);
        cancelButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    dialog.dismiss();

                }).start();
            }).start();

        });






        addButton = dialog.findViewById(R.id.addExerciseAddButton);
        addButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    String exerciseName = nameInput.getText().toString().trim();
                    String exerciseDescription = descriptionInput.getText().toString().trim();

                    Exercise exercise = new Exercise();

                    if(exerciseName.isBlank() || exerciseName.isEmpty()){
                        Toast.makeText(this, "Please, enter a valid name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    exercise.setName(exerciseName);

                    exercise.setDescription(exerciseDescription);

                    if(categoryList == null) categoryList = new ArrayList<>();
                    exercise.setCategoryList(categoryList);

                    if(muscleGroupList == null) muscleGroupList = new ArrayList<>();
                    exercise.setMuscleGroupList(muscleGroupList);

                    if(skillLevelList.isEmpty()){
                        Toast.makeText(this, "Please, select a valid skill level", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    exercise.setSkillLevelList(skillLevelList);
                    
                    if(setsRepsCheckbox.isChecked()){
                        exercise.setMetric(Metric.REPS);
                    }else if (timeCheckbox.isChecked()){
                        exercise.setMetric(Metric.TIME);
                    }else{
                        Toast.makeText(context, "Please, select a valid metric", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(movementList == null) movementList = new ArrayList<>();
                    exercise.setMovementList(movementList);

                    DBHelper.getInstance(this).saveExercise(exercise);
                    Toast.makeText(this, "Exercise was saved", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                    adapter.updateDataset(DBHelper.getInstance(context).getAllExercises());

                    categoryList = new ArrayList<>();
                    muscleGroupList = new ArrayList<>();
                    skillLevelList = new ArrayList<>();

                }).start();
            }).start();

        });
    }

    private void filterList(String text){
        List<Exercise> filteredList = new ArrayList<>();
        for(Exercise exercise: addedExercises){
            if(exercise.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(exercise);
            }
        }
        
        adapter.updateDataset(filteredList);

    }




} // AddedExercisesActivity ends here ==============================================================
