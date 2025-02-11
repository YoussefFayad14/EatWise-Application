package com.example.foodapp.ui.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.data.remote.model.Area;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private Context context;
    private List<Area> countryList;
    private onClickListener listener;


    public CountryAdapter(Context context, List<Area> Countries, onClickListener listener) {
        this.context = context;
        this.countryList = Countries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_card_country, parent, false);
        return new CountryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Area country = countryList.get(position);
        holder.itemTitle.setText(country.getName());

        holder.itemView.setOnClickListener(view -> {
            listener.onSectionClick(country);
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        public CountryViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.btnAreaTitle);
        }
    }
}
