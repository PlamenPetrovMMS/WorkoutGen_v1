package com.example.workoutgen_v3;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;

public class Util {
    public static void setLinearLayoutTextViewUI(Context context, TextView textView){
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

    public static <T extends Enum<T>> void updateDisplayedOptions(Context context, LinearLayout linearLayout, List<T> enumItemList, Class<T> enumClass){
        linearLayout.removeAllViews();

        if(enumItemList.isEmpty()){
            TextView textView = new TextView(context);
            textView.setText("ANY");
            setLinearLayoutTextViewUI(context, textView);
            linearLayout.addView(textView);
            return;
        }

        for(T enumItem: enumItemList){
            TextView textView = new TextView(context);
            textView.setText(enumItem.name());
            setLinearLayoutTextViewUI(context, textView);
            linearLayout.addView(textView);
        }
    }

    public static <T extends Enum<T>> void loadSelectionDialogCheckboxes(Context context, LinearLayout linearLayout, List<T> enumItemList, Class<T> enumClass){
        if(!enumItemList.isEmpty()){
            for(int i = 0; i < linearLayout.getChildCount(); i++){
                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                if(enumItemList.contains(T.valueOf(enumClass, checkBox.getText().toString().trim().toUpperCase()))){
                    checkBox.setChecked(true);
                    checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_option));
                }else{
                    checkBox.setChecked(false);
                    checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.unselected_option));
                }
            }
        }
    }


    public static void setLinearLayoutMaterialCheckboxListener(LinearLayout linearLayout){
        for(int i = 0; i < linearLayout.getChildCount(); i++){
            MaterialCheckBox checkbox = (MaterialCheckBox) linearLayout.getChildAt(i);
            int currentCheckboxIndex = i;
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        for(int j = 0; j < linearLayout.getChildCount(); j++){
                            MaterialCheckBox uncheckedBox = (MaterialCheckBox) linearLayout.getChildAt(j);
                            if(j != currentCheckboxIndex)
                                uncheckedBox.setChecked(false);
                        }
                    }
                }
            });
        }
    }


    public static void setCheckBoxChildClickAnimation(Context context, LinearLayout linearLayout){
        for(int i = 0; i < linearLayout.getChildCount(); i++){
            MaterialCheckBox checkBox = (MaterialCheckBox) linearLayout.getChildAt(i);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        checkBox.animate()
                                .scaleX(1.05f)
                                .scaleY(1.05f)
                                .setDuration(30).withEndAction(() -> {
                                    checkBox.animate()
                                            .scaleX(1f)
                                            .scaleY(1f)
                                            .setDuration(30)
                                            .start();
                                }).start();



                        checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_option));
                    }else{
                        checkBox.animate()
                                .scaleX(1.05f)
                                .scaleY(1.05f)
                                .setDuration(30).withEndAction(() -> {
                                    checkBox.animate()
                                            .scaleX(1f)
                                            .scaleY(1f)
                                            .setDuration(30)
                                            .start();
                                }).start();



                        checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.unselected_option));
                    }
                }
            });
        }
    }


}
