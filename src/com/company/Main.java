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

           ArrayList<List<String>> studentGegevens = new ArrayList<>();

            while ((str = csvStudentGegevens.readLine()) != null){
                List<String> gegevens = Arrays.asList(str.split(","));
               // Student stud1 = new Student(gegevens.get(0), gegevens.get(1), gegevens.get(2),
               //         gegevens.get(3),gegevens.get(4),gegevens.get(5),gegevens.get(6),gegevens.get(7));

             studentGegevens.add(gegevens);

            }
            System.out.println(studentGegevens.get(1));
            csvStudentGegevens.close();
        } catch (IOException e) {
            System.out.println("File Read Error");
        }

         Student test = new Student("de Vries", "Piet", 7);
        ArrayList<Student> students = new ArrayList<>();
//        students.add(stud1);
        System.out.println(test);


//      Aanmaken van de verschillende (beschikbare) lokalen. Mogelijkheid tot verbetering door dit
//      te doen door middel van het inlezen van een bestand - mogelijkheid tot verandering van het programma.
        Room room1 = new Room("A1.04", 41);
        Room room2 = new Room("A1.06", 22);
        Room room3 = new Room("A1.08", 20);
        Room room4 = new Room("A1.10", 56);
        Room room5 = new Room("B0.201", 48);
        Room room6 = new Room("C0.110", 117);
        Room room7 = new Room("C1.112", 60);

    }
}
