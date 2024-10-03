package com.example.practice2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.OnItemLongClickListener {
    private MyRecyclerViewAdapter adapter;
    private List<Item> itemList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyRecyclerViewAdapter(itemList, this);
        recyclerView.setAdapter(adapter);
        setupSwipeToDelete();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showDialog());
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // We are not implementing drag-and-drop
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Item itemToDelete = itemList.get(position);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            itemList.remove(position);
                            adapter.notifyItemRemoved(position);
                        })
                        .setNegativeButton("No", (dialog, which) -> adapter.notifyItemChanged(position))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onItemLongClick(Item item) {
        showEditDialog(item);
    }

    private void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);

        final EditText taskEditText = dialogView.findViewById(R.id.taskInput);
        final ImageView priorityIcon = dialogView.findViewById(R.id.icon_priority);
        final ImageView calendarIcon = dialogView.findViewById(R.id.icon_calendar);
        final Calendar calendar = Calendar.getInstance();
        final String[] selectedPriority = {""}; // Default priority
        final String[] selectedDate = {""}; // Default date

        priorityIcon.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
            popupMenu.getMenuInflater().inflate(R.menu.priority_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.priority_high) {
                    priorityIcon.setImageResource(R.drawable.baseline_priority_high_24);
                    selectedPriority[0] = "High";
                } else if (item.getItemId() == R.id.priority_medium) {
                    priorityIcon.setImageResource(R.drawable.baseline_priority_medium_24);
                    selectedPriority[0] = "Medium";
                } else if (item.getItemId() == R.id.priority_low) {
                    priorityIcon.setImageResource(R.drawable.baseline_priority_low_24);
                    selectedPriority[0] = "Low";
                } else if (item.getItemId() == R.id.priority_none) {
                    priorityIcon.setImageResource(R.drawable.baseline_priority_none);
                    selectedPriority[0] = "None";
                }
                return true;
            });
            popupMenu.show();
        });

        calendarIcon.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedDate[0] = dayOfMonth + "/" + (month + 1) + "/" + year; // Format date
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Task")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, id) -> {
                    String userInput = taskEditText.getText().toString();
                    if (!userInput.isEmpty()) {
                        Item newItem = new Item(userInput, selectedPriority[0], selectedDate[0]);
                        itemList.add(newItem);
                        adapter.notifyItemInserted(itemList.size() - 1);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEditDialog(Item item) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);

        final EditText taskEditText = dialogView.findViewById(R.id.taskInput);
        final ImageView priorityIcon = dialogView.findViewById(R.id.icon_priority);
        final ImageView calendarIcon = dialogView.findViewById(R.id.icon_calendar);
        taskEditText.setText(item.getName());

        final String[] selectedPriority = {item.getPriority()}; // Set current priority
        final String[] selectedDate = {item.getDate()}; // Set current date

        switch (selectedPriority[0]) {
            case "High":
                priorityIcon.setImageResource(R.drawable.baseline_priority_high_24);
                break;
            case "Medium":
                priorityIcon.setImageResource(R.drawable.baseline_priority_medium_24);
                break;
            case "Low":
                priorityIcon.setImageResource(R.drawable.baseline_priority_low_24);
                break;
            case "None":
                priorityIcon.setImageResource(R.drawable.baseline_priority_none);
                break;
        }

        priorityIcon.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
            popupMenu.getMenuInflater().inflate(R.menu.priority_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item1 -> {
                if (item1.getItemId() == R.id.priority_high) {
                    priorityIcon.setImageResource(R.drawable.baseline_priority_high_24);
                    selectedPriority[0] = "High";
                } else if (item1.getItemId() == R.id.priority_medium) {
                    priorityIcon.setImageResource(R.drawable.baseline_priority_medium_24);
                    selectedPriority[0] = "Medium";
                } else if (item1.getItemId() == R.id.priority_low) {
                    priorityIcon.setImageResource(R.drawable.baseline_priority_low_24);
                    selectedPriority[0] = "Low";
                }else if (item1.getItemId() == R.id.priority_none) {
                    priorityIcon.setImageResource(R.drawable.baseline_priority_none);
                    selectedPriority[0] = "None";
                }
                return true;
            });

            popupMenu.show();
        });

        calendarIcon.setOnClickListener(v -> {
            // Open date picker with the current date
            String[] dateParts = selectedDate[0].split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based in Calendar
            int year = Integer.parseInt(dateParts[2]);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        selectedDate[0] = dayOfMonth + "/" + (month1 + 1) + "/" + year1; // Format date
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, id) -> {
                    String updatedInput = taskEditText.getText().toString();
                    if (!updatedInput.isEmpty()) {
                        item.setName(updatedInput);
                        item.setPriority(selectedPriority[0]);
                        item.setDate(selectedDate[0]); // Update date
                        adapter.notifyDataSetChanged(); // Refresh the list
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
