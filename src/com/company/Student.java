package com.company;

/**
 * Created by Max on 18-2-2016.
 */
public class Student {
    public String name;
    public int studentNumber;

    public Student(String name, int studentNumber) {
        this.name = name;
        this.studentNumber = studentNumber;
        int x = 5;
    }

    public String toString() {
        return name+" "+studentNumber;

    }
}
