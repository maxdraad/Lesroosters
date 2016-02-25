package com.company;

import java.util.List;

/**
 * Created by Max on 18-2-2016.
 */
public class Activities {

    public String course;
    public String type;
    public int groupNumber;
    public List<String> studentGroup;


    public Group(String course, String type, int groupNumber, List<String> studentGroup ){
        this.type = type;
        this.course = course;
        this.groupNumber = groupNumber;
        this.studentGroup = studentGroup
    }

}
