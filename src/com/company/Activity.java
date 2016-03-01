package com.company;

import java.util.List;

public class Activity {

    public String course;
    public String activity;
    public int occurrences;
    public int groupNumber;
    public List<Integer> studentGroup;


    public Activity(String course, String activity, int occurrences, int groupNumber, List<Integer> studentGroup ){
        this.course = course;
        this.activity = activity;
        this.occurrences = occurrences;
        this.groupNumber = groupNumber;
        this.studentGroup = studentGroup;
    }

}
