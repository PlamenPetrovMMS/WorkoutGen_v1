package com.example.workoutgenerator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    MaterialButton createExerciseButton, addedExercisesButton, generateWorkoutButton;

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

        DBExercises exercises = new DBExercises(this);
        exercises.resetDatabase();

        createExerciseButton = findViewById(R.id.createExerciseButton);
        createExerciseButton.setOnClickListener(view ->{
            startActivity(new Intent(this, CreateExerciseActivity.class));
        });

        addedExercisesButton = findViewById(R.id.addedExercisesButton);
        addedExercisesButton.setOnClickListener(view -> {
            startActivity(new Intent(this, AddedExercisesActivity.class));
        });

        generateWorkoutButton = findViewById(R.id.mainGenerateWorkoutButton);
        generateWorkoutButton.setOnClickListener(view -> {
            startActivity(new Intent(this, GenerateWorkoutActivity.class));
        });

    }
}