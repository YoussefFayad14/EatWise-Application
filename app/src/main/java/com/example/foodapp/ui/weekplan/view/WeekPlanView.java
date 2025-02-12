package com.example.foodapp.ui.weekplan.view;

import com.example.foodapp.data.local.weekplandb.CalendarDay;
import com.example.foodapp.data.local.weekplandb.MealPlan;

import java.util.List;

public interface WeekPlanView {
    void showCalendarDays(List<CalendarDay> days);
    void showMealPlan(List<MealPlan> mealPlans);
    void showMessage(String message);
}
