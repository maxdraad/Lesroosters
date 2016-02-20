package com.company;

import java.util.List;

/**
 * Created by Max on 18-2-2016.
 */
public class Student {
    public String lastName;
    public String firstName;
    public int studentNumber;
    public List<String> studentCourses;


    public Student(String lastName, String firstName, int studentNumber, List studentCourses) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.studentNumber = studentNumber;
        this.studentCourses = studentCourses;

    }

    public String toString() {
        return lastName+" "+firstName+" "+studentNumber+" "+studentCourses;
    }
}
