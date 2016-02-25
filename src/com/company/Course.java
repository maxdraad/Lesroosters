package com.company;

import java.util.List;

public class Course {
    public String name;
    public int numberLectures;
    public List<Integer> courseStudents;


    public Course(String name, int numberLectures, List<Integer> courseStudents){
        this.name = name;
        this.numberLectures = numberLectures;
        this.courseStudents = courseStudents;
    }

    public String toString() {
        return name+" "+numberLectures+" "+courseStudents;
    }
}
