package com.example.habittracker;

public class Habit {
    private String name;
    private String description;
    private String number;

    public Habit(String name, String description, String number) {
        this.name = name;
        this.description = description;
        this.number = number;
    }

    // Getter methods for the fields (name, description, number)

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getNumber() {
        return number;
    }
}
