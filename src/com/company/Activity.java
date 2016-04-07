package com.company;

import java.util.List;

public class Activity {

    public Course course;
    public String activity;
    public int occurrence;
    public int groupNumber;
    public int numberActivityinWeek;
    public List<Student> studentGroup;


    public Activity(Course course, String activity, int occurrence, int groupNumber, List<Student> studentGroup ){
        this.course = course;
        this.activity = activity;
        this.occurrence = occurrence;
        this.groupNumber = groupNumber;
        this.studentGroup = studentGroup;
    }

    public String toString() {
        return course+" "+activity+" "+occurrence+" "+groupNumber+" "+studentGroup;
    }

}
