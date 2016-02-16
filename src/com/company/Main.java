package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public int N_CLASSROOMS = 7;
    public int N_TIME_SLOTS = 5;
    public int N_DAYS = 5;
    public int N_STUDENTS = 609;
    public int N_SUBJECTS = 29;

        public static void main(String[] args) {

        // BufferedReader die het tekstbestand met Studentgegevens inleest
        try {
            BufferedReader in = new BufferedReader(new FileReader("resources/studenten_roostering.csv"));
            String str;
            str = in.readLine();
            ArrayList<String> dataList = new ArrayList<String>();
            while ((str = in.readLine()) != null) {
                 dataList.add(str);
            }
            System.out.println(dataList[2]);
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
