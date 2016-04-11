package com.company;

import java.util.*;

public class Timetable {
/*    public List<Activity> activities;
    public List<Timeslot> timeslots;

    public Timetable(List<Activity> activities, List<Timeslot> timeslots){
        this.activities = activities;
        this.timeslots = timeslots;
    }*/

    public static void makeTable(List<Activity>activities, List<Timeslot> timeslots){
        new Timetable().go(activities, timeslots);
    }

    public void go(List<Activity> activities, List<Timeslot> timeslots){
        System.out.println("Hier komt een rooster!");

       // randomly assign activities to rooms

        // calculate score
    }
}
