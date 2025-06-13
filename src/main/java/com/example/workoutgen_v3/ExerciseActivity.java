package com.example.workoutgen_v3;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {
    Context context = this;
    RecyclerView recyclerView;
    AddedExerciseAdapter adapter;
    MaterialButton createButton, resetButton;
    List<Exercise> addedExercises;


    SearchView searchView;
    LinearLayout categoryLinearLayout, muscleGroupLinearLayout;
    TextView skillLevelResult;
    MaterialButton editCategoriesButton, editMuscleGroupsButton, editSkillLevelButton;
    MaterialButton cancelButton, addButton;


    List<Category> categoryList = new ArrayList<>();
    List<MuscleGroup> muscleGroupList = new ArrayList<>();
    SkillLevel skillLevel = null;


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

        resetButton = findViewById(R.id.exerciseActivityResetButton);
        resetButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog confirmDialog = new Dialog(context);
                    confirmDialog.setContentView(R.layout.confirm_reset);
                    confirmDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_box);

                    MaterialButton yesButton = confirmDialog.findViewById(R.id.confirmResetYesButton);
                    yesButton.setOnClickListener(confirmView -> {
                        confirmView.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                            confirmView.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                                addedExercises = new ArrayList<>();
                                DBHelper.getInstance(this).resetDatabase();
                                adapter.updateDataset(addedExercises);
                                confirmDialog.dismiss();

                            }).start();
                        }).start();
                    });

                    MaterialButton noButton = confirmDialog.findViewById(R.id.confirmResetNoButton);
                    noButton.setOnClickListener(confirmView -> {
                        confirmView.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                            confirmView.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                                confirmDialog.dismiss();

                            }).start();
                        }).start();
                    });

                    confirmDialog.show();

                }).start();
            }).start();
        });



    } // onCreate ends here ========================================================================

    private void initializeDialog(Dialog dialog){
        EditText nameInput, descriptionInput;
        nameInput = dialog.findViewById(R.id.addExerciseNameInput);
        descriptionInput = dialog.findViewById(R.id.addExerciseDescriptionInput);
        descriptionInput.setOnEditorActionListener((textView, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                textView.clearFocus();
                InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null){
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });

        categoryLinearLayout = dialog.findViewById(R.id.addExerciseCategoryLinearLayout);
        muscleGroupLinearLayout = dialog.findViewById(R.id.addExerciseMuscleGroupLinearLayout);
        skillLevelResult = dialog.findViewById(R.id.addExerciseSkillLevelResult);

        editCategoriesButton = dialog.findViewById(R.id.addExerciseEditCategoryButton);
        editCategoriesButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog categoriesDialog = new Dialog(this);
                    categoriesDialog.setContentView(R.layout.categories_dialog);

                    LinearLayout linearLayout = categoriesDialog.findViewById(R.id.categoriesDialogLayout);

                    loadSelectionDialogCheckboxes(linearLayout, categoryList, Category.class);

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
                            if(!categoryList.isEmpty()) updateGenLinearDialog(categoryLinearLayout, categoryList, Category.class);
                            else{
                                TextView textView = new TextView(context);
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








        editMuscleGroupsButton = dialog.findViewById(R.id.addExerciseEditMuscleGroupButton);
        editMuscleGroupsButton.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog muscleGroupDialog = new Dialog(this);
                    muscleGroupDialog.setContentView(R.layout.muscle_group_dialog);

                    LinearLayout linearLayout = muscleGroupDialog.findViewById(R.id.muscleGroupDialogLayout);

                    loadSelectionDialogCheckboxes(linearLayout, muscleGroupList, MuscleGroup.class);

                    muscleGroupDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            muscleGroupList.clear();
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                                    muscleGroupList.add(MuscleGroup.fromString(checkBoxName));
                                }
                            }

                            if(!muscleGroupList.isEmpty()) updateGenLinearDialog(muscleGroupLinearLayout, muscleGroupList, MuscleGroup.class);
                            else{
                                TextView textView = new TextView(context);
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








        editSkillLevelButton = dialog.findViewById(R.id.addExerciseEditSkillLevelButton);
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
                            skillLevel = null;
                            for(int i = 0; i < linearLayout.getChildCount(); i++){
                                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                                if(checkBox.isChecked()){
                                    String checkBoxName = checkBox.getText().toString().trim().toUpperCase();
                                    skillLevel = SkillLevel.fromString(checkBoxName);
                                }
                            }

                            if(skillLevel != null) skillLevelResult.setText(skillLevel.name());
                            else skillLevelResult.setText("ANY");


                        }
                    });

                    skillLevelDialog.show();

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

                    if(skillLevel == null){
                        Toast.makeText(this, "Please, select a valid skill level", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    exercise.setSkillLevel(skillLevel);

                    DBHelper.getInstance(this).saveExercise(exercise);
                    Toast.makeText(this, "Exercise was saved", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                    adapter.updateDataset(DBHelper.getInstance(context).getAllExercises());

                }).start();
            }).start();

        });
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
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 10, 0);
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.drawable.orange_box);
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.main_font));
        textView.setLetterSpacing(0.1f);
        textView.setTextSize(17);
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
