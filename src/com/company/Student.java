package com.company;

/**
 * Created by Max on 18-2-2016.
 */
public class Student {
    public String lastName;
    public String firstName;
    public int studentNumber;
    public String course1;
    public String course2;
    public String course3;
    public String course4;
    public String course5;


    public Student(String lastName, String firstName, int studentNumber,
                   String course1, String course2, String course3, String course4, String course5) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.studentNumber = studentNumber;
        this.course1 = course1;
        this.course2 = course2;
        this.course3 = course3;
        this.course4 = course4;
        this.course5 = course5;
    }

    public String toString() {
        return lastName+" "+firstName+" "+studentNumber;

    }
}
