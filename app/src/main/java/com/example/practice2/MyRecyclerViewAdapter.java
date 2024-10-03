package com.example.practice2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Item> itemList;
    private OnItemLongClickListener longClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(Item item);
    }

    public MyRecyclerViewAdapter(List<Item> itemList, OnItemLongClickListener longClickListener) {
        this.itemList = itemList;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.taskName.setText(item.getName());
        holder.taskDate.setText("Due Date: " + item.getDate()); // Set the date text

        switch (item.getPriority()) {
            case "High":
                holder.priorityIcon.setImageResource(R.drawable.baseline_priority_high_24);
                break;
            case "Medium":
                holder.priorityIcon.setImageResource(R.drawable.baseline_priority_medium_24);
                break;
            case "Low":
                holder.priorityIcon.setImageResource(R.drawable.baseline_priority_low_24);
                break;
            case "none":
                holder.priorityIcon.setImageResource(R.drawable.baseline_priority_none);
                break;
        }

        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClick(item);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView taskDate; // New TextView for date
        ImageView priorityIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task_text);
            taskDate = itemView.findViewById(R.id.text_date); // Initialize date TextView
            priorityIcon = itemView.findViewById(R.id.icon_priority);
        }
    }
}
