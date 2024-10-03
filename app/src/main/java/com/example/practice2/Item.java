package com.example.practice2;

public class Item {
    private String name;
    private String priority;
    private String date; // New field for date

    public Item(String name, String priority, String date) {
        this.name = name;
        this.priority = priority;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
