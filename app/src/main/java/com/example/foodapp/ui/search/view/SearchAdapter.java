package com.example.foodapp.ui.search.view;

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
import com.example.foodapp.data.remote.model.Meal;
import com.example.foodapp.ui.home.view.PopularAdapter;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Context context;
    private List<Meal> meals;
    private OnMealClickListener listener;
    public SearchAdapter(Context context, List<Meal> meals, OnMealClickListener listener){
        this.context = context;
        this.meals = meals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_card_search, parent, false);
        return new SearchAdapter.SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.itemName.setText(meal.getMealName());

        Glide.with(holder.itemView.getContext())
                .load(meal.getMealThumb())
                .into(holder.itemImage);

        holder.itemView.setOnClickListener(view -> {
            listener.onMealClick(meal);
        });

    }

    public void updateData(List<Meal> newMeals) {
        meals.clear();
        meals.addAll(newMeals);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return meals.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView itemImage;

        public SearchViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.mealName);
            itemImage = itemView.findViewById(R.id.mealImage);
        }
    }
}
