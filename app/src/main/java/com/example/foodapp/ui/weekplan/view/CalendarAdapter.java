package com.example.foodapp.ui.weekplan.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.data.local.model.CalendarDay;

import java.util.List;


public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private List<CalendarDay> daysList;
    private int selectedPosition = -1;
    private Context context;
    private TextView selectedDateTextView;
    private OnDayClickListener listener;

    public CalendarAdapter(Context context, List<CalendarDay> days,TextView selectedDateTextView,OnDayClickListener listener) {
        this.context = context;
        this.daysList = days;
        this.selectedDateTextView = selectedDateTextView;
        this.listener = listener;
    }

    public void setDays(List<CalendarDay> days) {
        this.daysList = days;
        selectedPosition = getTodayPosition();
        notifyDataSetChanged();
    }

    private int getTodayPosition() {
        Calendar calendar = Calendar.getInstance();
        int todayDayNum = calendar.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < daysList.size(); i++) {
            if (daysList.get(i).getDayNum() == todayDayNum) {
                return i;
            }
        }
        return 0;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_week_day, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CalendarDay day = daysList.get(position);
        holder.dayName.setText(day.getDayName());
        holder.dayNum.setText(String.valueOf(day.getDayNum()));

        if (position == selectedPosition) {
            holder.itemView.setSelected(true);
            holder.dayName.setTextColor(Color.WHITE);
            holder.dayNum.setTextColor(Color.WHITE);
        }else{
            holder.itemView.setSelected(false);
            holder.dayName.setTextColor(Color.GRAY);
            holder.dayNum.setTextColor(Color.GRAY);
        }

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            if (selectedDateTextView != null) {
                selectedDateTextView.setText(convertShortToFullDay(day.getDayName()) + ", " + day.getDayNum() + " " + day.getMonthName());
            }
            listener.onDayClick(convertShortToFullDay(day.getDayName()));
        });
    }
    private String convertShortToFullDay(String shortDay) {
        switch (shortDay.toLowerCase()) {
            case "sun": return "Sunday";
            case "mon": return "Monday";
            case "tue": return "Tuesday";
            case "wed": return "Wednesday";
            case "thu": return "Thursday";
            case "fri": return "Friday";
            case "sat": return "Saturday";
            default: return shortDay;
        }
    }



    @Override
    public int getItemCount() {
        return daysList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayName, dayNum;
        public ViewHolder(View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.textViewDayName);
            dayNum = itemView.findViewById(R.id.textViewDayNum);
        }
    }
}
