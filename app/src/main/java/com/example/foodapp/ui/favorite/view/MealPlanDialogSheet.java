package com.example.foodapp.ui.favorite.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.foodapp.R;
import com.example.foodapp.data.local.model.MealPlan;
import com.example.foodapp.data.local.model.FavoriteMeal;
import com.example.foodapp.ui.weekplan.presenter.WeekPlanPresenter;

public class MealPlanDialogSheet extends DialogFragment {
    private Spinner daySpinner;
    private RadioGroup mealTypeGroup;
    private Button btnAddMeal;
    private FavoriteMeal meal;
    private WeekPlanPresenter presenter;

    public MealPlanDialogSheet(FavoriteMeal meal, WeekPlanPresenter presenter) {
        this.meal = meal;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_sheet_meal_plan, container, false);

        daySpinner = view.findViewById(R.id.spinner_day);
        mealTypeGroup = view.findViewById(R.id.radioGroup_mealType);
        btnAddMeal = view.findViewById(R.id.btn_add_meal);

        btnAddMeal.setOnClickListener(v -> {
            String selectedDay = daySpinner.getSelectedItem().toString();
            int selectedMealId = mealTypeGroup.getCheckedRadioButtonId();
            String mealType = "";

            if (selectedMealId != -1) {
                RadioButton selectedMealButton = view.findViewById(selectedMealId);
                mealType = selectedMealButton.getText().toString();
            }

            if (!selectedDay.isEmpty() && !mealType.isEmpty()) {
                    MealPlan mealPlan = new MealPlan(meal.getMealId(), meal.getMealName(), meal.getMealArea(), meal.getMealImage(), selectedDay, mealType);
                    presenter.addMealToPlan(mealPlan);
                    dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setDimAmount(0.7f);
            window.setGravity(android.view.Gravity.CENTER);
        }
    }
}
