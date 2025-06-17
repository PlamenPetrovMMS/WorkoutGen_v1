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
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if(exercise.getMetric() == Metric.REPS){
            holder.timeLabel.setVisibility(View.INVISIBLE);
            holder.timeResult.setVisibility(View.INVISIBLE);

            holder.repsLabel.setVisibility(View.VISIBLE);
            holder.repsResult.setVisibility(View.VISIBLE);

            holder.repsResult.setText(String.valueOf(exercise.getReps())); // Generated reps

        }else if(exercise.getMetric() == Metric.TIME){
            holder.repsLabel.setVisibility(View.INVISIBLE);
            holder.repsResult.setVisibility(View.INVISIBLE);

            holder.timeLabel.setVisibility(View.VISIBLE);
            holder.timeResult.setVisibility(View.VISIBLE);

            holder.timeResult.setText(String.valueOf(exercise.getMinutes() + " min   " + exercise.getSeconds() + " secs")); // Generated minutes

        }




        holder.doneButton.setOnClickListener(view -> {
            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    // prevent IndexOutOfBound exception
                    int currentPosition = holder.getAdapterPosition();
                    if(currentPosition != RecyclerView.NO_POSITION){
                        localDataset.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                    }

                }).start();
            }).start();
        });

        holder.itemView.setOnClickListener(view -> {

            view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(30).withEndAction(() -> {
                view.animate().scaleX(1f).scaleY(1f).setDuration(10).withEndAction(() -> {

                    Dialog itemInfoDialog = new Dialog(context);
                    itemInfoDialog.setContentView(R.layout.item_info);
                    Objects.requireNonNull(itemInfoDialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_box);

                    TextView itemName = itemInfoDialog.findViewById(R.id.itemInfoItemName);
                    TextView itemDescription = itemInfoDialog.findViewById(R.id.itemInfoItemDescription);
                    LinearLayout itemCategoriesLinearLayout = itemInfoDialog.findViewById(R.id.itemInfoCategoryLinearLayout);
                    LinearLayout itemMuscleGroupLinearLayout = itemInfoDialog.findViewById(R.id.itemInfoMuscleGroupLinearLayout);
                    LinearLayout itemSkillLevelLinearLayout = itemInfoDialog.findViewById(R.id.itemInfoSkillLevelLinearLayout);

                    itemName.setText(exercise.getName());
                    itemDescription.setText(exercise.getDescription());

                    for(Category category: exercise.getCategoryList()){
                        TextView textView = new TextView(context);
                        textView.setText(category.name());
                        Util.setLinearLayoutTextViewUI(context, textView);
                        itemCategoriesLinearLayout.addView(textView);
                    }

                    for(MuscleGroup muscleGroup: exercise.getMuscleGroupList()){
                        TextView textView = new TextView(context);
                        textView.setText(muscleGroup.name());
                        Util.setLinearLayoutTextViewUI(context, textView);
                        itemMuscleGroupLinearLayout.addView(textView);
                    }

                    for(SkillLevel skillLevel: exercise.getSkillLevelList()){
                        TextView textView = new TextView(context);
                        textView.setText(skillLevel.name());
                        Util.setLinearLayoutTextViewUI(context, textView);
                        itemSkillLevelLinearLayout.addView(textView);
                    }

                    itemInfoDialog.show();

                }).start();
            }).start();
        });
    }





    @Override
    public int getItemCount() {
        return localDataset.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView exerciseName, setsResult, repsLabel, repsResult, timeLabel, timeResult;
        MaterialButton doneButton;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;

            exerciseName = itemView.findViewById(R.id.exerciseName);
            setsResult = itemView.findViewById(R.id.setsResult);

            repsLabel = itemView.findViewById(R.id.repsLabel);
            repsResult = itemView.findViewById(R.id.repsResult);

            timeLabel = itemView.findViewById(R.id.timeLabel);
            timeResult = itemView.findViewById(R.id.timeResult);

            doneButton = itemView.findViewById(R.id.itemDoneButton);

        }
    }

}
