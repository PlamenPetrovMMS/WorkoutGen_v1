package com.example.workoutgen_v3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddedExerciseAdapter extends RecyclerView.Adapter<AddedExerciseAdapter.ViewHolder> {

    Context context;
    List<Exercise> localDataset = new ArrayList<>();

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

}
