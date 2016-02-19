package com.company;

/**
 * Created by Nicole on 2016-02-19.
 */
public class Courses {
    public String name;
    public int numberLectures;

    public Courses(String name, int numberLectures){
        this.name = name;
        this.numberLectures = numberLectures;
    }

    public String toString() {
        return name+" "+numberLectures;
    }
}
