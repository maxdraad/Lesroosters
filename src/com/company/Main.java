package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public int N_CLASSROOMS = 7;
    public int N_TIME_SLOTS = 5;
    public int N_DAYS = 5;
    public int N_STUDENTS = 609;
    public int N_SUBJECTS = 29;

        public static void main(String[] args) {

        // In dit gedeelte worden studentgegevens in een List in een ArrayList gezet
        // (dus een 2x2 tabel met studentgegevens)
        try {
            BufferedReader csvStudentGegevens = new BufferedReader(new FileReader("resources/studenten_roostering.csv"));
            String str;
            str = csvStudentGegevens.readLine();

            ArrayList<List<String>> studentGegevens = new ArrayList<>();

            while ((str = csvStudentGegevens.readLine()) != null) {

                List<String> gegevens = Arrays.asList(str.split(","));
                studentGegevens.add(gegevens);
            }
            System.out.println( studentGegevens.get(3) );
            csvStudentGegevens.close();
        } catch (IOException e) {
            System.out.println("File Read Error");
        }

        System.out.println("Push test");
        Student stud1 = new Student("Piet");
            System.out.println(stud1.name);
    }
}
