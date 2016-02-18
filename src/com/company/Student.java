package com.company;

/**
 * Created by Max on 18-2-2016.
 */
public class Student {
    public String lastName;
    public String firstName;
    public int studentNumber;


    public Student(String lastName, String firstName, int studentNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.studentNumber = studentNumber;

    }

    public String toString() {
        return lastName+" "+firstName+" "+studentNumber;

    }
}
