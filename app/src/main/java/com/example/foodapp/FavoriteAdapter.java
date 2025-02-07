package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Meal> mealList;
    private Context context;

    public FavoriteAdapter(List<Meal> mealList, Context context) {
        this.mealList = mealList;
        this.context = context;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_favourites, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.mealName.setText(meal.getName());
        holder.mealDescription.setText(meal.getDescription());
        holder.mealImage.setImageResource(meal.getImageResId());

        holder.removeButton.setOnClickListener(v -> {
            removeItem(position);
        });

        holder.addToPlanButton.setOnClickListener(v -> {
            addToPlan(meal);
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView mealName, mealDescription;
        ImageView mealImage;
        Button removeButton, addToPlanButton;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName);
            mealDescription = itemView.findViewById(R.id.mealCountry);
            mealImage = itemView.findViewById(R.id.mealImage);
            removeButton = itemView.findViewById(R.id.removeButton);
            addToPlanButton = itemView.findViewById(R.id.addToPlanButton);
        }
    }

    private void removeItem(int position) {
        mealList.remove(position);
        notifyItemRemoved(position);
    }
    private void addToPlan(Meal meal) {
        Toast.makeText(context, meal.getName() + " added to weekly plan!", Toast.LENGTH_SHORT).show();
    }
}
