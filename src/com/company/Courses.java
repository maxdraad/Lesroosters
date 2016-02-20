package com.company;

import java.util.List;

/**
 * Created by Nicole on 2016-02-19.
 */
public class Courses {
    public String name;
    public int numberLectures;
    public List<Integer> courseStudents;


    public Courses(String name, int numberLectures, List<Integer> courseStudents){
        this.name = name;
        this.numberLectures = numberLectures;
        this.courseStudents = courseStudents;
    }

    public String toString() {
        return name+" "+numberLectures+" "+courseStudents;
    }
}
