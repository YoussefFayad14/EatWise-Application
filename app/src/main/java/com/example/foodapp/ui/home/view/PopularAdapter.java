package com.example.foodapp.ui.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.data.model.CountryMeal;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {

    private Context context;
    private List<CountryMeal> popularMealList;
    private onClickListener listener;

    public PopularAdapter(Context context, List<CountryMeal> PopularMeals , onClickListener listener) {
        this.context = context;
        this.popularMealList = PopularMeals;
        this.listener = listener;

    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_card_popular, parent, false);
        return new PopularViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        CountryMeal popularMeal = popularMealList.get(position);
        holder.itemTitle.setText(popularMeal.getMealName());
        Glide.with(holder.itemView.getContext())
                .load(popularMeal.getMealThumb())
                .into(holder.itemImage);

        holder.itemView.setOnClickListener(view -> {
            listener.onItemClick(popularMeal);
        });
    }

    @Override
    public int getItemCount() {
        return popularMealList.size();
    }

    public static class PopularViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        ImageView itemImage;

        public PopularViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemImage = itemView.findViewById(R.id.itemImage);        }
    }
}
