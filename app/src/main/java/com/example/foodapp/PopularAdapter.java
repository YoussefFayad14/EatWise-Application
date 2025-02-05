package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder> {

    private Context context;
    private List<String> popularMealList;
    private Fragment parentFragment;

    public PopularAdapter(Context context, List<String> PopularMeals, Fragment parentFragment) {
        this.context = context;
        this.popularMealList = PopularMeals;
        this.parentFragment = parentFragment;

    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_card_popular, parent, false);
        return new PopularViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        String popularMeal = popularMealList.get(position);
        holder.itemTitle.setText(popularMeal);
        holder.itemImage.setImageResource(R.drawable.ic_launcher_foreground);
        holder.itemView.setOnClickListener(view -> {
            FragmentTransaction transaction = parentFragment.getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(parentFragment.getId(), new MealDetailsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
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
