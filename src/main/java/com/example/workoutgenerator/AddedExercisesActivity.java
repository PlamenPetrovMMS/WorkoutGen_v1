package com.example.workoutgenerator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddedExercisesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ExerciseAdapter adapter;
    ImageButton menuButton;
    List<Exercise> addedExercises = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_added_exercises);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeElements();



    } // onCreate ends here ========================================================================

    private void loadExercises(){
        DBExercises exerciseDB = new DBExercises(this);
        addedExercises = exerciseDB.getAllExercises();
        adapter.updateLocalDataset(addedExercises);
        adapter.notifyDataSetChanged();
        exerciseDB.close();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void initializeElements(){

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExerciseAdapter(addedExercises);
        recyclerView.setAdapter(adapter);

        loadExercises();

        menuButton = findViewById(R.id.addedExercisesMenuButton);

    }

} // AddedExercisesActivity ends here ==============================================================