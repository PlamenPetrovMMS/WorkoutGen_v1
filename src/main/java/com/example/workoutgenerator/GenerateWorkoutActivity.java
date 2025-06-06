package com.example.workoutgenerator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Map;

public class GenerateWorkoutActivity extends AppCompatActivity {

    Exercise.MovementType movementType;
    Exercise.BodypartType bodypartType;
    Exercise.Level exerciseLevel;

    Workout.Level workoutLevel;

    private final int DEFAULT_EXERCISE_AMOUNT = 2;

    TextView generatedWorkoutView;
    EditText exerciseAmountInput;
    MaterialButton generateWorkoutButton, saveWorkoutButton, customizeWorkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generate_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeElements();
        setOnClickListeners();

    } // onCreate ends here ========================================================================
    private void initializeElements(){
        generatedWorkoutView = findViewById(R.id.generatedWorkoutExercisesView);

        exerciseAmountInput = findViewById(R.id.exerciseAmountInput);

        generateWorkoutButton = findViewById(R.id.generateWorkoutButton);
        saveWorkoutButton = findViewById(R.id.saveWorkoutButton);
        customizeWorkoutButton = findViewById(R.id.customizeWorkoutButton);
    }
    private void setOnClickListeners(){
        if(generateWorkoutButton == null) throw new RuntimeException("Generate Workout button is not initialized");
        if(saveWorkoutButton == null) throw new RuntimeException("Save Workout button is not initialized");

        generateWorkoutButton.setOnClickListener(view -> {
            if(exerciseLevel == null) exerciseLevel = Exercise.DEFAULT_LEVEL;
            if(movementType == null) movementType = Exercise.DEFAULT_MOVEMENT_TYPE;
            if(bodypartType == null) bodypartType = Exercise.DEFAULT_BODYPART_TYPE;

            DBExercises exercisesDB = new DBExercises(this);

            if(workoutLevel == null){
                Toast.makeText(this, "Please, select workout level", Toast.LENGTH_SHORT).show();
                return;
            }

            Workout workout = new Workout(this, workoutLevel, movementType, bodypartType, Integer.parseInt(exerciseAmountInput.getText().toString()));
            if(workout.getWorkourMap().isEmpty()){
                return;
            }

            StringBuilder stringBuilder = new StringBuilder();
            for(Map.Entry<Exercise, Map<String, Integer>> exerciseEntry: workout.getWorkourMap().entrySet()){
                stringBuilder.append(exerciseEntry.getKey().getName()).append(" | ");
                for(Map.Entry<String, Integer> repEntry: exerciseEntry.getValue().entrySet()){
                    stringBuilder.append(repEntry.getKey()).append(" :: ").append(repEntry.getValue().toString());
                    stringBuilder.append("     ");
                }
                stringBuilder.append("\n");
            }

            generatedWorkoutView.setText(stringBuilder.toString());

            Toast.makeText(this, "Workout was generated successfully", Toast.LENGTH_SHORT).show();
        });
        saveWorkoutButton.setOnClickListener(view -> {

        });
        customizeWorkoutButton.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.workout_edit_dialog);
            // dialog.getWindow().setBackgroundDrawable();

            TextView movementTextView = dialog.findViewById(R.id.editWorkoutMovementResult);
            TextView bodypartTextView = dialog.findViewById(R.id.editWorkoutBodypartResult);
            TextView levelTextView = dialog.findViewById(R.id.editWorkoutLevelResult);

            MaterialButton movementButton = dialog.findViewById(R.id.editWorkoutMovementButton);
            MaterialButton bodypartButton = dialog.findViewById(R.id.editWorkoutBodypartButton);
            MaterialButton levelButton = dialog.findViewById(R.id.editWorkoutLevelButton);
            MaterialButton applyButton = dialog.findViewById(R.id.editWorkoutApplyButton);

            movementButton.setOnClickListener(v1 -> {

                Dialog smallDialog = new Dialog(v1.getContext());
                smallDialog.setContentView(R.layout.edit_movement_type_dialog);
                smallDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                MaterialButton pushEditButton, pullEditButton, noneEditButton;

                pushEditButton = smallDialog.findViewById(R.id.pushMovementEditButton);
                pullEditButton = smallDialog.findViewById(R.id.pullMovementEditButton);

                pushEditButton.setOnClickListener(v2 -> {
                    if(bodypartTextView.getText().toString().equals(Exercise.BodypartType.CORE.toString())){
                        bodypartTextView.setText(Exercise.DEFAULT_BODYPART_TYPE.toString());
                    }
                    movementTextView.setText(Exercise.MovementType.PUSH.toString());
                    smallDialog.dismiss();
                });
                pullEditButton.setOnClickListener(v2 -> {
                    if(bodypartTextView.getText().toString().equals(Exercise.BodypartType.LOWERBODY.toString())){
                        bodypartTextView.setText(Exercise.DEFAULT_BODYPART_TYPE.toString());
                    }
                    movementTextView.setText(Exercise.MovementType.PULL.toString());
                    smallDialog.dismiss();
                });

                smallDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });

                smallDialog.show();
            });
            bodypartButton.setOnClickListener(v1 -> {
                Dialog smallDialog = new Dialog(v1.getContext());
                smallDialog.setContentView(R.layout.edit_bodypart_type_dialog);
                smallDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                MaterialButton upperBodypartEditButton, coreBodypartEditButton, lowerBodypartEditButton;

                upperBodypartEditButton = smallDialog.findViewById(R.id.upperBodypartEditButton);
                coreBodypartEditButton = smallDialog.findViewById(R.id.coreBodypartEditButton);
                lowerBodypartEditButton = smallDialog.findViewById(R.id.lowerBodypartEditButton);

                upperBodypartEditButton.setOnClickListener(v -> {
                    bodypartTextView.setText(Exercise.BodypartType.UPPERBODY.toString());
                    smallDialog.dismiss();
                });
                coreBodypartEditButton.setOnClickListener(v -> {
                    if(movementTextView.getText().toString().equals(Exercise.MovementType.PUSH.toString())){
                        movementTextView.setText(Exercise.DEFAULT_MOVEMENT_TYPE.toString());
                    }
                    bodypartTextView.setText(Exercise.BodypartType.CORE.toString());
                    smallDialog.dismiss();
                });
                lowerBodypartEditButton.setOnClickListener(v -> {
                    if(movementTextView.getText().toString().equals(Exercise.MovementType.PULL.toString())){
                        movementTextView.setText(Exercise.DEFAULT_MOVEMENT_TYPE.toString());
                    }
                    bodypartTextView.setText(Exercise.BodypartType.LOWERBODY.toString());
                    smallDialog.dismiss();
                });

                smallDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });

                smallDialog.show();
            });
            levelButton.setOnClickListener(v1 -> {
                Dialog smallDialog = new Dialog(v1.getContext());
                smallDialog.setContentView(R.layout.edit_level_dialog);
                smallDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                MaterialButton easyLevelEditButton, mediumLevelEditButton, hardLevelEditButton;

                easyLevelEditButton = smallDialog.findViewById(R.id.easyLevelEditButton);
                mediumLevelEditButton = smallDialog.findViewById(R.id.mediumLevelEditButton);
                hardLevelEditButton = smallDialog.findViewById(R.id.hardLevelEditButton);

                easyLevelEditButton.setText("BEGINNER");
                easyLevelEditButton.setOnClickListener(v -> {
                    levelTextView.setText(Workout.Level.BEGINNER.toString());
                    workoutLevel = Workout.Level.BEGINNER;
                    smallDialog.dismiss();
                });

                mediumLevelEditButton.setText("INTERMEDIATE");
                mediumLevelEditButton.setOnClickListener(v -> {
                    levelTextView.setText(Workout.Level.INTERMEDIATE.toString());
                    workoutLevel = Workout.Level.INTERMEDIATE;
                    smallDialog.dismiss();
                });

                hardLevelEditButton.setText("PRO");
                hardLevelEditButton.setOnClickListener(v -> {
                    levelTextView.setText(Workout.Level.PRO.toString());
                    workoutLevel = Workout.Level.PRO;
                    smallDialog.dismiss();
                });

                smallDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });

                smallDialog.show();
            });


            applyButton.setOnClickListener(v -> {

                String workoutLevel = levelTextView.getText().toString().trim();
                String movement = movementTextView.getText().toString().trim();
                String bodypart = bodypartTextView.getText().toString().trim();

                this.workoutLevel = Workout.Level.fromString(workoutLevel);
                this.movementType = Exercise.MovementType.fromString(movement);
                this.bodypartType = Exercise.BodypartType.fromString(bodypart);

                Toast.makeText(this, "Customization applied", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

            dialog.show();

        });
    }
} // GenerateWorkoutActivity ends here =============================================================