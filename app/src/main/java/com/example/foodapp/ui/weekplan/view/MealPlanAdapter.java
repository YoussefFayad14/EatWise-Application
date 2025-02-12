package com.example.foodapp.ui.weekplan.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.data.local.weekplandb.MealPlan;
import java.util.ArrayList;
import java.util.List;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.MealViewHolder> {
    private Context context;
    private OnMealClickListener listener;

    private List<MealPlan> meals = new ArrayList<>();

    public MealPlanAdapter(Context context, OnMealClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void updateList(List<MealPlan> newMeals) {
        this.meals.clear();
        this.meals.addAll(newMeals);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_favourites, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealPlan mealPlan = meals.get(position);
        holder.bind(mealPlan);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealName, mealCountry;
        ImageView mealImage;
        Button addToPlanButton;
        ImageButton removeButton;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName);
            mealCountry = itemView.findViewById(R.id.mealCountry);
            mealImage = itemView.findViewById(R.id.mealImage);
            removeButton = itemView.findViewById(R.id.removeButton);
            addToPlanButton = itemView.findViewById(R.id.addToPlanButton);

            addToPlanButton.setVisibility(View.GONE);

        }

        void bind(MealPlan mealPlan) {
            mealName.setText(mealPlan.getMealName());
            mealCountry.setText(mealPlan.getMealArea());

            Glide.with(itemView.getContext())
                    .load(mealPlan.getMealImage())
                    .placeholder(R.drawable.baseline_visibility_off_24)
                    .into(mealImage);

            removeButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMealRemove(mealPlan);
                }
            });
        }
    }
}
