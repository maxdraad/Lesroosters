package com.company;

import java.util.List;

public class Activity {

    public String course;
    public String type;
    public int groupNumber;
    public List<String> studentGroup;


    public Activity(String course, String type, int groupNumber, List<String> studentGroup ){
        this.type = type;
        this.course = course;
        this.groupNumber = groupNumber;
        this.studentGroup = studentGroup;
    }

}
