package com.company;

import java.util.List;

public class Room {
    public String name;
    public int capacity;
    public boolean nightSlot;
    public List<Activity> timetable;

    public Room(String name, int capacity, boolean nightSlot, List<Activity> timetable){
            this.name = name;
        this.capacity = capacity;
        this.timetable = timetable;
        this.nightSlot = nightSlot;
    }

    public String toString() {
        return name+" "+capacity;
    }
}
