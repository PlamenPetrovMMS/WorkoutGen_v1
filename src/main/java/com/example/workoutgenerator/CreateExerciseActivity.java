package com.example.workoutgenerator;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class CreateExerciseActivity extends AppCompatActivity {
    EditText nameEditText;
    TextView movementTypeResultTextView, bodypartTypeResultTextView, symmetryResultTextView, levelResultTextView;
    MaterialButton editMovementTypeButton, editBodypartTypeButton, editLevelButton, editSymmetryButton, createButton;
    Exercise exercise = new Exercise();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_exercise);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.createExerciseActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeLayoutElements();
        setEditTextFocusChangeListener();
        setButtonClickListeners();


//        Exercise exercise1 = new Exercise(Exercise.NEXT_ID, "Push up", Exercise.Level.EASY, Exercise.MovementType.PUSH, Exercise.BodypartType.UPPERBODY, Exercise.Symmetry.UNILATERAL);
//        Exercise exercise2 = new Exercise(Exercise.NEXT_ID, "Archer Push up", Exercise.Level.MEDIUM, Exercise.MovementType.PUSH, Exercise.BodypartType.UPPERBODY, Exercise.Symmetry.BILATERAL);
//        Exercise exercise3 = new Exercise(Exercise.NEXT_ID, "Pull up", Exercise.Level.MEDIUM, Exercise.MovementType.PULL, Exercise.BodypartType.UPPERBODY, Exercise.Symmetry.UNILATERAL);
//        Exercise exercise4 = new Exercise(Exercise.NEXT_ID, "Squad", Exercise.Level.EASY, Exercise.MovementType.NONE, Exercise.BodypartType.LOWERBODY, Exercise.Symmetry.UNILATERAL);
//
//
//        DBExercises exerciseDB = new DBExercises(this);
//        exerciseDB.resetDatabase();
//
//        exerciseDB.saveExercise(exercise1);
//        exerciseDB.saveExercise(exercise2);
//        exerciseDB.saveExercise(exercise3);
//        exerciseDB.saveExercise(exercise4);
//
//        Workout workout = new Workout(this, Workout.Level.INTERMEDIATE, 3);
//        workout.generateSetsAndReps();
//
//        DBWorkouts workoutDB = new DBWorkouts(this);
//        workoutDB.resetDatabase();
//        workoutDB.saveWorkout(workout);
//
//        exerciseDB.close();
//        workoutDB.close();

    } // onCreate ends here ========================================================================
    private void initializeLayoutElements(){
        nameEditText = findViewById(R.id.nameEditText);

        movementTypeResultTextView = findViewById(R.id.movementTypeResultTextView);
        bodypartTypeResultTextView = findViewById(R.id.bodypartTypeResultTextView);
        symmetryResultTextView = findViewById(R.id.symmetryResultTextView);
        levelResultTextView = findViewById(R.id.levelResultTextView);

        editMovementTypeButton = findViewById(R.id.createMovementTypeButton);
        editBodypartTypeButton = findViewById(R.id.createBodypartTypeButton);
        editLevelButton = findViewById(R.id.createLevelButton);
        editSymmetryButton = findViewById(R.id.createSymmetryButton);
        createButton = findViewById(R.id.createButton);
    }
    private void setEditTextFocusChangeListener(){
        if(nameEditText == null) throw new RuntimeException("Uninitialized edit text");

//        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                exercise.setName(nameEditText.getText().toString());
//            }
//        });

        nameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                    String input = nameEditText.getText().toString().trim();
                    DBExercises exerciseDB = new DBExercises(CreateExerciseActivity.this);
                    if(!exerciseDB.isExerciseNameTaken(input)){
                        exercise.setName(input);
                        nameEditText.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }else{
                        Toast.makeText(CreateExerciseActivity.this, "Exercise name already taken", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }
    private void setButtonClickListeners(){
        if(editMovementTypeButton == null ||
           editBodypartTypeButton == null ||
           editLevelButton == null ||
           editSymmetryButton == null ||
           createButton == null) throw new RuntimeException("Uninitialized edit buttons");


        editMovementTypeButton.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.edit_movement_type_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            MaterialButton pushEditButton, pullEditButton, noneEditButton;

            pushEditButton = dialog.findViewById(R.id.pushMovementEditButton);
            pullEditButton = dialog.findViewById(R.id.pullMovementEditButton);

            pushEditButton.setOnClickListener(v -> {
                exercise.setMovementType(Exercise.MovementType.PUSH);
                applyMovementRestrictions();
                dialog.dismiss();
            });
            pullEditButton.setOnClickListener(v -> {
                exercise.setMovementType(Exercise.MovementType.PULL);
                applyMovementRestrictions();
                dialog.dismiss();
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    movementTypeResultTextView.setText(exercise.getMovementType().toString());
                }
            });

            dialog.show();
        });
        editBodypartTypeButton.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.edit_bodypart_type_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            MaterialButton upperBodypartEditButton, coreBodypartEditButton, lowerBodypartEditButton;

            upperBodypartEditButton = dialog.findViewById(R.id.upperBodypartEditButton);
            coreBodypartEditButton = dialog.findViewById(R.id.coreBodypartEditButton);
            lowerBodypartEditButton = dialog.findViewById(R.id.lowerBodypartEditButton);

            upperBodypartEditButton.setOnClickListener(v -> {
                exercise.setBodypartType(Exercise.BodypartType.UPPERBODY);
                dialog.dismiss();
            });
            coreBodypartEditButton.setOnClickListener(v -> {
                exercise.setBodypartType(Exercise.BodypartType.CORE);
                applyCoreRestrictions();
                dialog.dismiss();
            });
            lowerBodypartEditButton.setOnClickListener(v -> {
                exercise.setBodypartType(Exercise.BodypartType.LOWERBODY);
                applyLowerbodyRestrictions();
                dialog.dismiss();
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    bodypartTypeResultTextView.setText(exercise.getBodypartType().toString());
                }
            });

            dialog.show();
        });
        editLevelButton.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.edit_level_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            MaterialButton easyLevelEditButton, mediumLevelEditButton, hardLevelEditButton;

            easyLevelEditButton = dialog.findViewById(R.id.easyLevelEditButton);
            mediumLevelEditButton = dialog.findViewById(R.id.mediumLevelEditButton);
            hardLevelEditButton = dialog.findViewById(R.id.hardLevelEditButton);

            easyLevelEditButton.setOnClickListener(v -> {
                exercise.setLevel(Exercise.Level.EASY);
                dialog.dismiss();
            });
            mediumLevelEditButton.setOnClickListener(v -> {
                exercise.setLevel(Exercise.Level.MEDIUM);
                dialog.dismiss();
            });
            hardLevelEditButton.setOnClickListener(v -> {
                exercise.setLevel(Exercise.Level.HARD);
                dialog.dismiss();
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    levelResultTextView.setText(exercise.getLevel().toString());
                }
            });

            dialog.show();
        });
        editSymmetryButton.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.edit_symmetry_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            MaterialButton unilateralEditButton, bilateralEditButton;

            unilateralEditButton = dialog.findViewById(R.id.unilateralEditButton);
            bilateralEditButton = dialog.findViewById(R.id.bilateralEditButton);

            unilateralEditButton.setOnClickListener(v -> {
                exercise.setSymmetry(Exercise.Symmetry.UNILATERAL);
                dialog.dismiss();
            });
            bilateralEditButton.setOnClickListener(v -> {
                exercise.setSymmetry(Exercise.Symmetry.BILATERAL);
                dialog.dismiss();
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    symmetryResultTextView.setText(exercise.getSymmetry().toString());
                }
            });

            dialog.show();
        });
        createButton.setOnClickListener(view -> {
            DBExercises exercisesDB = new DBExercises(this);

            if(exercise.getName() == null || !exercise.getName().equals(nameEditText.getText().toString().trim())){
                exercise.setName(nameEditText.getText().toString().trim());
                if(exercisesDB.isExerciseNameTaken(exercise.getName())){
                    Toast.makeText(this, "Exercise name already taken", Toast.LENGTH_SHORT).show();
                    exercisesDB.close();
                    return;
                }
            }

            if(!exercisesDB.exerciseExists(exercise)){
                if(exercise.isExerciseValid()){
                    exercisesDB.saveExercise(exercise);
                    Toast.makeText(this, "Exercise created", Toast.LENGTH_SHORT).show();
                    exercisesDB.close();
                    finish();
                }else{
                    Toast.makeText(this, "Invalid exercise", Toast.LENGTH_SHORT).show();
                    exercisesDB.close();
                }
            }else{
                Toast.makeText(this, "Exercise already exists", Toast.LENGTH_SHORT).show();
                exercisesDB.close();
            }
        });
    }
    private void applyMovementRestrictions(){

        Exercise.MovementType movementType = exercise.getMovementType();
        Exercise.BodypartType bodypartType = exercise.getBodypartType();

        if(movementType == Exercise.MovementType.PULL && bodypartType == Exercise.BodypartType.LOWERBODY){
            exercise.setBodypartType(Exercise.BodypartType.ANY);
            bodypartTypeResultTextView.setText(exercise.getBodypartType().toString());
        }else if(movementType == Exercise.MovementType.PUSH && bodypartType == Exercise.BodypartType.CORE){
            exercise.setBodypartType(Exercise.BodypartType.ANY);
            bodypartTypeResultTextView.setText(exercise.getBodypartType().toString());
        }
    }

    private void applyCoreRestrictions(){

        Exercise.BodypartType bodypartType = exercise.getBodypartType();
        Exercise.MovementType movementType = exercise.getMovementType();

        if(bodypartType == Exercise.BodypartType.CORE && movementType == Exercise.MovementType.PUSH){
            exercise.setMovementType(Exercise.MovementType.ANY);
            movementTypeResultTextView.setText(exercise.getMovementType().toString());
        }
    }

    private void applyLowerbodyRestrictions(){
        Exercise.BodypartType bodypartType = exercise.getBodypartType();
        Exercise.MovementType movementType = exercise.getMovementType();

        if(bodypartType == Exercise.BodypartType.LOWERBODY && movementType == Exercise.MovementType.PULL){
            exercise.setMovementType(Exercise.MovementType.ANY);
            movementTypeResultTextView.setText(exercise.getMovementType().toString());
        }
    }


} // CreateExerciseActivity ends here ==============================================================