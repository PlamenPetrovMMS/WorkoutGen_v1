package com.example.workoutgenerator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

    List<Exercise> localDataset = new ArrayList<>();

    public ExerciseAdapter(List<Exercise> dataset){
        localDataset = dataset;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ViewHolder holder, int position) {

        Exercise exercise = localDataset.get(position);

        holder.getNameTextView().setText(exercise.getName());
        holder.getLevelTextView().setText(exercise.getLevel().toString());

        holder.getItemView().setOnLongClickListener(view -> {
            Log.d("ExerciseAdapter", "Long click on item: " + holder.getNameTextView().getText().toString());
            Dialog dialog = new Dialog(view.getContext());
            dialog.setContentView(R.layout.item_edit_dialog);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.item_edit_dialog_bg);


            EditText nameEditText = dialog.findViewById(R.id.editItemNameEditText);
            TextView movementTypeTextView = dialog.findViewById(R.id.editItemMovementTypeResult);
            TextView bodypartTypeTextView = dialog.findViewById(R.id.editItemBodypartTypeResult);
            TextView levelTextView = dialog.findViewById(R.id.editItemLevelResult);
            TextView symmetryTextView = dialog.findViewById(R.id.editItemSymmetryResult);

            MaterialButton editMovementTypeButton = dialog.findViewById(R.id.editItemMovementTypeButton);
            MaterialButton editBodypartTypeButton = dialog.findViewById(R.id.editItemBodypartTypeButton);
            MaterialButton editLevelButton = dialog.findViewById(R.id.editItemLevelButton);
            MaterialButton editSymmetryButton = dialog.findViewById(R.id.editItemSymmetryButton);
            MaterialButton editSaveButton = dialog.findViewById(R.id.editItemSaveButton);

            editMovementTypeButton.setOnClickListener(v1 -> {
                Dialog smallDialog = new Dialog(v1.getContext());
                smallDialog.setContentView(R.layout.edit_movement_type_dialog);
                smallDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                MaterialButton pushEditButton, pullEditButton, noneEditButton;

                pushEditButton = smallDialog.findViewById(R.id.pushMovementEditButton);
                pullEditButton = smallDialog.findViewById(R.id.pullMovementEditButton);

                pushEditButton.setOnClickListener(v2 -> {
                    exercise.setMovementType(Exercise.MovementType.PUSH);
                    if(exercise.getBodypartType() == Exercise.BodypartType.CORE){
                        exercise.setBodypartType(Exercise.BodypartType.ANY);
                        bodypartTypeTextView.setText(exercise.getBodypartType().toString());
                    }
                    smallDialog.dismiss();
                });
                pullEditButton.setOnClickListener(v2 -> {
                    exercise.setMovementType(Exercise.MovementType.PULL);
                    if(exercise.getBodypartType() == Exercise.BodypartType.LOWERBODY){
                        exercise.setBodypartType(Exercise.BodypartType.ANY);
                        bodypartTypeTextView.setText(exercise.getBodypartType().toString());
                    }
                    smallDialog.dismiss();
                });

                smallDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        movementTypeTextView.setText(exercise.getMovementType().toString());
                    }
                });

                smallDialog.show();
            });
            editBodypartTypeButton.setOnClickListener(v1 -> {
                Dialog smallDialog = new Dialog(v1.getContext());
                smallDialog.setContentView(R.layout.edit_bodypart_type_dialog);
                smallDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                MaterialButton upperBodypartEditButton, coreBodypartEditButton, lowerBodypartEditButton;

                upperBodypartEditButton = smallDialog.findViewById(R.id.upperBodypartEditButton);
                coreBodypartEditButton = smallDialog.findViewById(R.id.coreBodypartEditButton);
                lowerBodypartEditButton = smallDialog.findViewById(R.id.lowerBodypartEditButton);

                upperBodypartEditButton.setOnClickListener(v -> {
                    exercise.setBodypartType(Exercise.BodypartType.UPPERBODY);
                    smallDialog.dismiss();
                });
                coreBodypartEditButton.setOnClickListener(v -> {
                    exercise.setBodypartType(Exercise.BodypartType.CORE);
                    if(exercise.getMovementType() == Exercise.MovementType.PUSH){
                        exercise.setMovementType(Exercise.MovementType.ANY);
                        movementTypeTextView.setText(exercise.getMovementType().toString());
                    }
                    smallDialog.dismiss();
                });
                lowerBodypartEditButton.setOnClickListener(v -> {
                    exercise.setBodypartType(Exercise.BodypartType.LOWERBODY);
                    if(exercise.getMovementType() == Exercise.MovementType.PULL){
                        exercise.setMovementType(Exercise.MovementType.ANY);
                        movementTypeTextView.setText(exercise.getMovementType().toString());
                    }
                    smallDialog.dismiss();
                });

                smallDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        bodypartTypeTextView.setText(exercise.getBodypartType().toString());
                    }
                });

                smallDialog.show();
            });
            editLevelButton.setOnClickListener(v1 -> {
                Dialog smallDialog = new Dialog(v1.getContext());
                smallDialog.setContentView(R.layout.edit_level_dialog);
                smallDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                MaterialButton easyLevelEditButton, mediumLevelEditButton, hardLevelEditButton;

                easyLevelEditButton = smallDialog.findViewById(R.id.easyLevelEditButton);
                mediumLevelEditButton = smallDialog.findViewById(R.id.mediumLevelEditButton);
                hardLevelEditButton = smallDialog.findViewById(R.id.hardLevelEditButton);

                easyLevelEditButton.setOnClickListener(v -> {
                    exercise.setLevel(Exercise.Level.EASY);
                    smallDialog.dismiss();
                });
                mediumLevelEditButton.setOnClickListener(v -> {
                    exercise.setLevel(Exercise.Level.MEDIUM);
                    smallDialog.dismiss();
                });
                hardLevelEditButton.setOnClickListener(v -> {
                    exercise.setLevel(Exercise.Level.HARD);
                    smallDialog.dismiss();
                });

                smallDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        levelTextView.setText(exercise.getLevel().toString());
                    }
                });

                smallDialog.show();
            });
            editSymmetryButton.setOnClickListener(v1 -> {
                Dialog smallDialog = new Dialog(v1.getContext());
                smallDialog.setContentView(R.layout.edit_symmetry_dialog);
                smallDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                MaterialButton unilateralEditButton, bilateralEditButton;

                unilateralEditButton = smallDialog.findViewById(R.id.unilateralEditButton);
                bilateralEditButton = smallDialog.findViewById(R.id.bilateralEditButton);

                unilateralEditButton.setOnClickListener(v -> {
                    exercise.setSymmetry(Exercise.Symmetry.UNILATERAL);
                    smallDialog.dismiss();
                });
                bilateralEditButton.setOnClickListener(v -> {
                    exercise.setSymmetry(Exercise.Symmetry.BILATERAL);
                    smallDialog.dismiss();
                });

                smallDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        symmetryTextView.setText(exercise.getSymmetry().toString());
                    }
                });

                smallDialog.show();
            });

            editSaveButton.setOnClickListener(v1 -> {

            });

            nameEditText.setText(exercise.getName());
            movementTypeTextView.setText(exercise.getMovementType().toString());
            bodypartTypeTextView.setText(exercise.getBodypartType().toString());
            levelTextView.setText(exercise.getLevel().toString());
            symmetryTextView.setText(exercise.getSymmetry().toString());




            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Log.d("ExerciseAdapter", "Dialog dismissed");
                }
            });

            dialog.show();

            return true; // true - consumes the event
        });


    }


    @Override
    public int getItemCount() {
        return localDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView exerciseNameTextView, exerciseLevelTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
            exerciseLevelTextView = itemView.findViewById(R.id.exerciseLevelTextView);

        }
        public TextView getNameTextView(){
            return exerciseNameTextView;
        }
        public TextView getLevelTextView(){
            return exerciseLevelTextView;
        }
        public View getItemView(){
            return itemView;
        }
    }
    public void updateLocalDataset(List<Exercise> dataset){
        localDataset = dataset;
    }




}
