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

    public int i = 1;

        public static void main(String[] args) {

        // In dit gedeelte worden studentgegevens in een List in een ArrayList gezet
        // (dus een 2x2 tabel met studentgegevens)
        try {
            BufferedReader csvStudentGegevens = new BufferedReader(new FileReader("resources/studenten_roostering.csv"));
            String str;
            str = csvStudentGegevens.readLine();

//            ArrayList<List<String>> studentGegevens = new ArrayList<>();

            while ((str = csvStudentGegevens.readLine()) != null){
                Student stud1 = new Student("de vries","piet",10,"a","b","c","d","e");
//                List<String> gegevens = Arrays.asList(str.split(","));
//                studentGegevens.add(gegevens);
                System.out.println(stud1);
            }
//            System.out.println( studentGegevens );
            csvStudentGegevens.close();
        } catch (IOException e) {
            System.out.println("File Read Error");
        }

//        Student stud1 = new Student("de Vries", "Piet", 7);
        ArrayList<Student> students = new ArrayList<>();
//        students.add(stud1);
//        System.out.println(stud1);


    }
}
