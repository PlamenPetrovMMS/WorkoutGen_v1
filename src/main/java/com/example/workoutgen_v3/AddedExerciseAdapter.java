package com.example.workoutgen_v3;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AddedExerciseAdapter extends RecyclerView.Adapter<AddedExerciseAdapter.ViewHolder> {

    Context context;
    List<Exercise> localDataset = new ArrayList<>();



    List<Category> categoryList = new ArrayList<>();
    List<MuscleGroup> muscleGroupList = new ArrayList<>();
    List<SkillLevel> skillLevelList = new ArrayList<>();
    List<Movement> movementList = new ArrayList<>();

    public AddedExerciseAdapter(Context context, List<Exercise> dataset){
        this.context = context;
        localDataset = dataset;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateDataset(List<Exercise> dataset){
        localDataset = dataset;
        Collections.sort(localDataset, new Comparator<Exercise>() {
            @Override
            public int compare(Exercise o1, Exercise o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AddedExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_exercise_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedExerciseAdapter.ViewHolder holder, int position) {
        Exercise exercise = localDataset.get(position);

        holder.exerciseName.setText(exercise.getName());

        holder.itemView.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(20).withEndAction(() -> {

                    Dialog itemInfoDialog = new Dialog(context);
                    itemInfoDialog.setContentView(R.layout.added_exercise_edit);
                    Objects.requireNonNull(itemInfoDialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_box);
                    innitializeDialog(itemInfoDialog, exercise, position);
                    itemInfoDialog.show();

                }).start();
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return localDataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView exerciseName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            exerciseName = itemView.findViewById(R.id.addedExercisesItemName);
        }
    }

    private void innitializeDialog(Dialog dialog, Exercise exercise, int position){

        EditText nameInput = dialog.findViewById(R.id.editItemInfoNameInput);
        EditText descriptionInput = dialog.findViewById(R.id.editItemInfoDescriptionInput);

        MaterialButton editCategoryButton = dialog.findViewById(R.id.editItemInfoEditCategoryButton);
        MaterialButton editMuscleGroupButton = dialog.findViewById(R.id.editItemInfoEditMuscleGroupButton);
        MaterialButton editSkillLevelButton = dialog.findViewById(R.id.editItemInfoEditSkillLevelButton);
        MaterialButton editMovementButton = dialog.findViewById(R.id.editItemInfoEditMovementButton);

        MaterialButton removeButton = dialog.findViewById(R.id.editItemInfoRemoveButton);
        MaterialButton cancelButton = dialog.findViewById(R.id.editItemInfoCancelButton);
        MaterialButton applyButton = dialog.findViewById(R.id.editItemInfoApplyButton);







        LinearLayout categoryLinearLayout = dialog.findViewById(R.id.editItemInfoCategoryLinearLayout);
        List<Category> exerciseCategoryList = new ArrayList<>(exercise.getCategoryList());
        if(!exerciseCategoryList.isEmpty()){
            categoryLinearLayout.removeAllViews();
            for(Category category: exerciseCategoryList){
                TextView textView = new TextView(context);
                textView.setText(category.name());
                setLinearLayoutTextViewUI(textView);
                categoryLinearLayout.addView(textView);
            }
        }
        categoryList = exerciseCategoryList;




        LinearLayout muscleGroupLinearLayout = dialog.findViewById(R.id.editItemInfoMuscleGroupLinearLayout);
        List<MuscleGroup> exerciseMuscleGroupList = new ArrayList<>(exercise.getMuscleGroupList());
        if(!exerciseMuscleGroupList.isEmpty()){
            muscleGroupLinearLayout.removeAllViews();
            for(MuscleGroup muscleGroup: exerciseMuscleGroupList){
                TextView textView = new TextView(context);
                textView.setText(muscleGroup.name());
                setLinearLayoutTextViewUI(textView);
                muscleGroupLinearLayout.addView(textView);
            }
        }
        muscleGroupList = exerciseMuscleGroupList;




        LinearLayout skillLevelLinearLayout = dialog.findViewById(R.id.editItemInfoSkillLevelLinearLayout);
        List<SkillLevel> exerciseSkillLevel = new ArrayList<>(exercise.getSkillLevelList());
        if(!exerciseSkillLevel.isEmpty()){
            skillLevelLinearLayout.removeAllViews();
            for(SkillLevel skillLevel: exerciseSkillLevel){
                TextView textView = new TextView(context);
                textView.setText(skillLevel.name());
                Util.setLinearLayoutTextViewUI(context, textView);
                skillLevelLinearLayout.addView(textView);
            }
        }
        skillLevelList = exerciseSkillLevel;




        MaterialCheckBox setsRepsCheckbox = dialog.findViewById(R.id.editItemInfoMetricSetsRepsCheckbox);
        MaterialCheckBox timeCheckbox = dialog.findViewById(R.id.editItemInfoMetricTimeCheckbox);
        if(exercise.getMetric() != null){
            switch (exercise.getMetric()){
                case REPS:
                    setsRepsCheckbox.setChecked(true);
                    timeCheckbox.setChecked(false);
                    break;
                case TIME:
                    setsRepsCheckbox.setChecked(false);
                    timeCheckbox.setChecked(true);
                    break;
                default:
                    setsRepsCheckbox.setChecked(false);
                    timeCheckbox.setChecked(false);
            }
        }
        LinearLayout metricLinearLayout = dialog.findViewById(R.id.editItemInfoMetricLinearLayout);
        Util.setLinearLayoutMaterialCheckboxListener(metricLinearLayout);




        LinearLayout movementLinearLayout = dialog.findViewById(R.id.editItemInfoMovementLinearLayout);
        List<Movement> exerciseMovementList = new ArrayList<>(exercise.getMovementList());
        if(!exerciseMovementList.isEmpty()){
            movementLinearLayout.removeAllViews();
            for(Movement movement: exerciseMovementList){
                TextView textView = new TextView(context);
                textView.setText(movement.name());
                Util.setLinearLayoutTextViewUI(context, textView);
                movementLinearLayout.addView(textView);
            }
        }
        movementList = exerciseMovementList;
















        nameInput.setText(exercise.getName());
        descriptionInput.setText(exercise.getDescription());



        editCategoryButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog categoriesDialog = new Dialog(context);
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




        editMuscleGroupButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog muscleGroupDialog = new Dialog(context);
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
                                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
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

        editSkillLevelButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog skillLevelDialog = new Dialog(context);
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
                                MaterialCheckBox checkbox = (MaterialCheckBox) linearLayout.getChildAt(i);
                                if(checkbox.isChecked()){
                                    String checkBoxName = checkbox.getText().toString().trim().toUpperCase();
                                    movementList.add(Movement.fromString(checkBoxName));
                                }
                            }

                            Util.updateDisplayedOptions(context, movementLinearLayout, movementList, Movement.class);
                        }
                    });

                    movementDialog.show();

                }).start();
            }).start();
        });





        removeButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog confirmDialog = new Dialog(context);
                    confirmDialog.setContentView(R.layout.confirm_reset);
                    Objects.requireNonNull(confirmDialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_box);

                    MaterialButton yesButton = confirmDialog.findViewById(R.id.confirmResetYesButton);
                    yesButton.setOnClickListener(confirmView -> {
                        confirmView.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                            confirmView.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                                categoryList = new ArrayList<>();
                                muscleGroupList = new ArrayList<>();

                                DBHelper.getInstance(context).removeItemById(exercise.getId());
                                localDataset.remove(position);
                                updateDataset(localDataset);
                                confirmDialog.dismiss();
                                dialog.dismiss();

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




        cancelButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    dialog.dismiss();

                }).start();
            }).start();
        });




        applyButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    String exerciseName = nameInput.getText().toString().trim();
                    String exerciseDescription = descriptionInput.getText().toString().trim();

                    if(exerciseName.isBlank() || exerciseName.isEmpty()){
                        Toast.makeText(context, "Please, enter a valid name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    exercise.setName(exerciseName);
                    exercise.setDescription(exerciseDescription);
                    if(categoryList == null) categoryList = new ArrayList<>();
                    exercise.setCategoryList(categoryList);
                    if(muscleGroupList == null) muscleGroupList = new ArrayList<>();
                    exercise.setMuscleGroupList(muscleGroupList);

                    if(skillLevelList.isEmpty()){
                        Toast.makeText(context, "Please, select a valid skill level", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    exercise.setSkillLevelList(skillLevelList);

                    if(setsRepsCheckbox.isChecked()){
                        exercise.setMetric(Metric.REPS);
                    }else if(timeCheckbox.isChecked()){
                        exercise.setMetric(Metric.TIME);
                    }else{
                        Toast.makeText(context, "Please, select a valid metric", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(movementList == null) movementList = new ArrayList<>();
                    exercise.setMovementList(movementList);

                    DBHelper.getInstance(context).updateItemById(exercise.getId(), exercise);
                    Toast.makeText(context, "Exercise was saved", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                    updateDataset(DBHelper.getInstance(context).getAllExercises());

                    categoryList = new ArrayList<>();
                    muscleGroupList = new ArrayList<>();
                    skillLevelList = new ArrayList<>();

                    setsRepsCheckbox.setChecked(false);
                    timeCheckbox.setChecked(false);

                }).start();
            }).start();
        });
    }








    private void setLinearLayoutTextViewUI(TextView textView){
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 10, 0, 0);
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.drawable.orange_box);
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.main_font));
        textView.setLetterSpacing(0.1f);
        textView.setTextSize(17);
    }


    private void filterList(String text){
        List<Exercise> filteredList = new ArrayList<>();
        for(Exercise exercise: localDataset){
            if(exercise.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(exercise);
            }
        }

        updateDataset(filteredList);

    }
}
