package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public int N_CLASSROOMS = 7;
    public int N_TIME_SLOTS = 5;
    public int N_DAYS = 5;
    public int N_STUDENTS = 609;
    public int N_SUBJECTS = 29;

    //test
    public int N_COURSES = 10;



    public static void main(String[] args) {

        try {
            BufferedReader in = new BufferedReader(new FileReader("resources/studenten_roostering.csv"));
            String str;
            str = in.readLine();
            while ((str = in.readLine()) != null) {
                System.out.println(str);
            }
            in.close();
        } catch (IOException e) {
            System.out.println("File Read Error");
        }

        System.out.println("Hello World");
        System.out.println("Test Push");
        System.out.println("Push Back");
	    System.out.println("Hello World");
        System.out.println("Hello Again");
        System.out.println("Push");
        System.out.println("Haaay");

    }
}
