package com.example.workoutgen_v3;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeExerciseAdapter extends RecyclerView.Adapter<HomeExerciseAdapter.ViewHolder> {

    Context context;
    List<Exercise> localDataset = new ArrayList<>();




    public HomeExerciseAdapter(Context context, List<Exercise> dataset){
        this.context = context;
        localDataset = dataset;
    }




    @SuppressLint("NotifyDataSetChanged")
    public void updateDataset(List<Exercise> dataset){
        localDataset = dataset;
        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public HomeExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_exercise_item, parent, false);
        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Exercise exercise = localDataset.get(position);

        holder.exerciseName.setText(exercise.getName() + " "); // Exercise name + " " to fix font visual cut off
        holder.setsResult.setText(String.valueOf(exercise.getSets())); // Generated sets
        holder.repsResult.setText(String.valueOf(exercise.getReps())); // Generated reps



        holder.doneButton.setOnClickListener(view -> {
            // prevent IndexOutOfBound exception
            int currentPosition = holder.getAdapterPosition();
            if(currentPosition != RecyclerView.NO_POSITION){
                localDataset.remove(currentPosition);
                notifyItemRemoved(currentPosition);
            }
        });

        holder.itemView.setOnClickListener(view -> {
            Dialog itemInfoDialog = new Dialog(context);
            itemInfoDialog.setContentView(R.layout.item_info);

            TextView itemName = itemInfoDialog.findViewById(R.id.itemInfoItemName);
            TextView itemDescription = itemInfoDialog.findViewById(R.id.itemInfoItemDescription);
            LinearLayout itemCategoriesLinearLayout = itemInfoDialog.findViewById(R.id.itemInfoCategoryLinearLayout);
            LinearLayout itemMuscleGroupLinearLayout = itemInfoDialog.findViewById(R.id.itemInfoMuscleGroupLinearLayout);
            TextView itemSkillGroup = itemInfoDialog.findViewById(R.id.itemInfoSkillLevelResult);

            itemName.setText(exercise.getName());
            itemDescription.setText(exercise.getDescription());

            for(Category category: exercise.getCategoryList()){
                TextView textView = new TextView(context);
                textView.setText(category.name());
                itemCategoriesLinearLayout.addView(textView);
            }

            for(MuscleGroup muscleGroup: exercise.getMuscleGroupList()){
                TextView textView = new TextView(context);
                textView.setText(muscleGroup.name());
                itemMuscleGroupLinearLayout.addView(textView);
            }

            itemSkillGroup.setText(exercise.getSkillLevel().name());

            itemInfoDialog.show();
        });
    }





    @Override
    public int getItemCount() {
        return localDataset.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView exerciseName, setsResult, repsResult;
        MaterialButton doneButton;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;

            exerciseName = itemView.findViewById(R.id.exerciseName);
            setsResult = itemView.findViewById(R.id.setsResult);
            repsResult = itemView.findViewById(R.id.repsResult);
            doneButton = itemView.findViewById(R.id.itemDoneButton);

        }
    }


}
