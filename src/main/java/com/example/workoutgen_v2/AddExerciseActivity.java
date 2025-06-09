package com.example.workoutgen_v2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class AddExerciseActivity extends AppCompatActivity {
    Context context = this;

    EditText nameInput, descriptionInput;
    MaterialButton editCategoriesButton, editMuscleGroupsButton, editUserSkillGroupButton, addExerciseButton;


    ExerciseModel exerciseModel;
    List<Category> categoryList = new ArrayList<>();
    List<MuscleGroup> muscleGroupList = new ArrayList<>();
    UserSkillGroup userSkillGroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_exercise);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        exerciseModel = new ExerciseModel();

        initializeElements();
        initializeButtonsClickListeners();

    }
    private void initializeElements(){
        nameInput = findViewById(R.id.nameInputEditText);
        descriptionInput = findViewById(R.id.descriptionInputEditText);

        editCategoriesButton = findViewById(R.id.editCategoriesButton);
        editMuscleGroupsButton = findViewById(R.id.editMuscleGroupsButton);
        editUserSkillGroupButton = findViewById(R.id.editUserSkillGroupButton);

        addExerciseButton = findViewById(R.id.addButton);
    }
    @SuppressLint("ResourceAsColor")
    private void initializeButtonsClickListeners(){

        editCategoriesButton.setOnClickListener(view -> {
            showMultiSelectOptionDialog(
                    R.layout.categories_dialog,
                    R.id.dialogCategoriesLinearLayout,
                    categoryList,
                    Category.values(),
                    exerciseModel::setCategories,
                    R.id.categoryOptionsLayout
            );
        });



        editMuscleGroupsButton.setOnClickListener(view -> {
            showMultiSelectOptionDialog(
                    R.layout.muscle_group_dialog,
                    R.id.dialogMuscleGroupLinearLayout,
                    muscleGroupList,
                    MuscleGroup.values(),
                    exerciseModel::setMuscleGroups,
                    R.id.muscleGroupOptionsLayout
            );
        });



        editUserSkillGroupButton.setOnClickListener(view -> {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.user_skill_group_dialog);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_background);

            LinearLayout linearLayout = dialog.findViewById(R.id.dialogUserSkillGroupLinearLayout);

            for(int i = 0; i < linearLayout.getChildCount(); i++){
                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                String checkBoxName = checkBox.getText().toString().toUpperCase();

                checkBox.setChecked(userSkillGroup != null && checkBoxName.equals(userSkillGroup.name()));

                int index = i;
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            for(int j = 0; j < linearLayout.getChildCount(); j++){
                                if(j != index){
                                    CheckBox uncheckedBox = (CheckBox) linearLayout.getChildAt(j);
                                    uncheckedBox.setChecked(false);
                                }
                            }
                        }
                    }
                });
            }

            LinearLayout userSkillGroupOptionsLayout = findViewById(R.id.userSkillGroupOptionsLayout);


            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    for(int i = 0; i < linearLayout.getChildCount(); i++){
                        CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                        if(checkBox.isChecked()){
                            String checkBoxName = checkBox.getText().toString().toUpperCase();
                            for(UserSkillGroup usg: UserSkillGroup.values()){
                                if(checkBoxName.equals(usg.name())){
                                    userSkillGroup = usg;
                                    exerciseModel.setUserSkillGroup(userSkillGroup);

                                    userSkillGroupOptionsLayout.removeAllViews();
                                    TextView textView = new TextView(context);
                                    textView.setText(userSkillGroup.name());
                                    textView.setBackgroundResource(R.drawable.option_bubble_background);
                                    textView.setTextColor(ContextCompat.getColor(context, R.color.black));
                                    userSkillGroupOptionsLayout.addView(textView);
                                    return;
                                }
                            }
                        }
                    }
                }
            });

            dialog.show();
        });

        addExerciseButton.setOnClickListener(view -> {

            exerciseModel.setName(nameInput.getText().toString().trim());
            exerciseModel.setDescription(descriptionInput.getText().toString().trim());

            System.out.println(exerciseModel.getName());
            System.out.println(exerciseModel.getDescription());
            System.out.println(exerciseModel.getCategories());
            System.out.println(exerciseModel.getMuscleGroups());
            System.out.println(exerciseModel.getUserSkillGroup());
        });
    }
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private <T extends Enum<T>> void showMultiSelectOptionDialog(int layoutId,
                                                                 int linearLayoutId,
                                                                 List<T> enumItemList,
                                                                 T[] enumValues,
                                                                 Consumer<List<T>> setItemsMethod,
                                                                 int bubbleLinearLayoutId){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(layoutId);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_background);

        LinearLayout linearLayout = dialog.findViewById(linearLayoutId);

        initializeMultiSelectCheckBoxes(linearLayout, enumItemList, enumValues);

        dialog.setOnDismissListener(dialogInterface -> {
            updateMultiItemList(linearLayout, enumItemList, enumValues);
            setItemsMethod.accept(enumItemList);

            LinearLayout bubbleLinearLayout = findViewById(bubbleLinearLayoutId);
            bubbleLinearLayout.removeAllViews();

            int noneCount = 0;

            for(T item: enumItemList){
                if(item != Category.NONE && item != MuscleGroup.NONE){
                    TextView textView = new TextView(this);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(3, 0, 3, 0);
                    textView.setLayoutParams(params);

                    textView.setText(item.name());
                    textView.setBackgroundResource(R.drawable.option_bubble_background);
                    textView.setTextColor(ContextCompat.getColor(this, R.color.black));
                    bubbleLinearLayout.addView(textView);
                }else{
                    noneCount++;
                }
            }

            if(noneCount == 4){
                bubbleLinearLayout.removeAllViews();
                TextView noneTextView = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(3, 0, 3, 0);
                noneTextView.setLayoutParams(params);

                noneTextView.setText("NONE");
                noneTextView.setBackgroundResource(R.drawable.option_bubble_background);
                noneTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
                bubbleLinearLayout.addView(noneTextView);
                return;
            }
        });

        dialog.show();
    }

    private <T extends Enum<T>> void initializeMultiSelectCheckBoxes(LinearLayout linearLayout,
                                                                     List<T> enumItemList,
                                                                     T[] enumValues){
        if(enumItemList == null || enumItemList.isEmpty()){
            enumItemList = new ArrayList<>();
        }else{
            for(int i = 0; i < linearLayout.getChildCount(); i++){
                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                String checkBoxName = checkBox.getText().toString().toUpperCase();
                String enumItemName = enumItemList.get(i).name();
                checkBox.setChecked(checkBoxName.equals(enumItemName));
            }
        }
    }

    private <T extends Enum<T>> void updateMultiItemList(LinearLayout linearLayout,
                                                         List<T> enumItemList,
                                                         T[] enumValues){
        enumItemList.clear();
        for(int i = 0; i < linearLayout.getChildCount(); i++){
            CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
            if(checkBox.isChecked()){
                for(T enumValue: enumValues){
                    String checkBoxName = checkBox.getText().toString().toUpperCase();
                    if(checkBoxName.equals(enumValue.name())){
                        enumItemList.add(enumValue);
                        break;
                    }
                }
            }else{
                enumItemList.add(enumValues[0]);
            }
        }
    }


} // AddExerciseActivity ends here =================================================================