package com.example.practice2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
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

        priorityIcon.setOnClickListener(v -> {
            priorityIcon.setImageResource(R.drawable.baseline_priority_high_24);
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Task")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, id) -> {
                    String userInput = taskEditText.getText().toString();
                    if (!userInput.isEmpty()) {
                        Item newItem = new Item(userInput);
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
        taskEditText.setText(item.getName());

        priorityIcon.setOnClickListener(v -> {
            priorityIcon.setImageResource(R.drawable.baseline_priority_high_24);
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, id) -> {
                    String updatedInput = taskEditText.getText().toString();
                    if (!updatedInput.isEmpty()) {
                        item.setName(updatedInput);
                        adapter.notifyDataSetChanged(); // Refresh the list
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
