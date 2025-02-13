package com.example.foodapp.ui.favorite.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.data.local.model.FavoriteMeal;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private Context context;
    private List<FavoriteMeal> favMealList;
    private onClickListener listener;


    public FavoriteAdapter(Context context,List<FavoriteMeal> FavMealList,onClickListener listener) {
        this.context = context;
        this.favMealList = FavMealList;
        this.listener = listener;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_favourites, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        FavoriteMeal meal = favMealList.get(position);
        holder.mealName.setText(meal.getMealName());
        holder.mealCountry.setText(meal.getMealArea());
        Glide.with(holder.itemView.getContext())
                .load(meal.getMealImage())
                .into(holder.mealImage);

        holder.itemView.setOnClickListener(v -> {
            showMealDetails(meal);
        });

        holder.removeButton.setOnClickListener(v -> {
            removeItem(position, meal);
        });

        holder.addToPlanButton.setOnClickListener(v -> {
           addToPlan(meal);
        });
    }

    @Override
    public int getItemCount() {
        return favMealList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView mealName, mealCountry;
        ImageView mealImage;
        Button addToPlanButton;
        ImageButton removeButton;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName);
            mealCountry = itemView.findViewById(R.id.mealCountry);
            mealImage = itemView.findViewById(R.id.mealImage);
            removeButton = itemView.findViewById(R.id.removeButton);
            addToPlanButton = itemView.findViewById(R.id.addToPlanButton);
        }
    }
    private void showMealDetails(FavoriteMeal meal) {
        listener.onMealClick(meal);
    }
    private void removeItem(int position, FavoriteMeal meal) {
        favMealList.remove(position);
        notifyItemRemoved(position);
        listener.RemoveFromFavouritesClick(meal);
    }
    private void addToPlan(FavoriteMeal meal) {
        listener.AddToPlanClick(meal);
    }
}
