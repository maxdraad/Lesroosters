package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        // ArrayList to keep al courses, rooms and students and their information
        ArrayList<Courses> courses = new ArrayList<>();
        ArrayList<Room> rooms = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();

        // Read in all courses and define their features
        try {
            BufferedReader csvVakkenGegevens = new BufferedReader(new FileReader("resources/vakken_roostering.csv"));
            String course;
            csvVakkenGegevens.readLine();

            while ((course = csvVakkenGegevens.readLine()) != null) {
                List<String> gegevensVak = Arrays.asList(course.split(";"));
                String name = gegevensVak.get(0);
                int numberLectures = Integer.parseInt(gegevensVak.get(1));
                int numberWorkgroups = Integer.parseInt(gegevensVak.get(2));
                int maxStudentsGroups = Integer.parseInt(gegevensVak.get(3));
                int numberPracticum = Integer.parseInt(gegevensVak.get(4));
                int maxStudentsPracticum = Integer.parseInt(gegevensVak.get(5));

                List<Integer> courseStudents = new ArrayList<Integer>();

                Courses newCourse = new Courses(name, numberLectures, courseStudents);
                courses.add(newCourse);
            }
            csvVakkenGegevens.close();

        } catch (IOException e) {
            System.out.println("File Read Error Courses");
        }

        // Read in all lecture rooms and their capacity
        try {
            BufferedReader csvZaalGegevens = new BufferedReader(new FileReader("resources/zalen_roostering.csv"));
            String room;
            csvZaalGegevens.readLine();

            while ((room = csvZaalGegevens.readLine()) != null) {
                List<String> gegevensZaal = Arrays.asList(room.split(";"));
                String roomName = gegevensZaal.get(0);
                int capacity = Integer.parseInt(gegevensZaal.get(1));

                Room newRoom = new Room(roomName, capacity);
                rooms.add(newRoom);
            }

        } catch (IOException e){
            System.out.println("File Read Error Rooms");
        }

        // Read in all students and define their features
        try {
            BufferedReader csvStudentGegevens = new BufferedReader(new FileReader("resources/studenten_roostering.csv"));
            String student;
            csvStudentGegevens.readLine();

            while ((student = csvStudentGegevens.readLine()) != null) {
                List<String> gegevens = Arrays.asList(student.split(";"));
                String lastName = gegevens.get(0);
                String firstName = gegevens.get(1);
                int studentNumber = Integer.parseInt(gegevens.get(2));


                //Instantiation of variable with list of Courses belonging to student
                List<String> studentCourses = gegevens.subList(3, gegevens.size());

                // Voegt student toe aan course
                for (int i = 0; i < courses.size(); i++){
                    Courses course = courses.get(i);
                    if (studentCourses.contains(course.name)) {
                        course.courseStudents.add(studentNumber);
                    }
                }
                Student newStudent = new Student(lastName, firstName, studentNumber, studentCourses);
                students.add(newStudent);

            }
            csvStudentGegevens.close();
        } catch (IOException e) {
            System.out.println("File Read Error Students");
        }

        for(int i = 0; i < courses.size(); i++){
            Courses course = courses.get(i);
            System.out.println(course.name+" "+course.courseStudents.size());
        }
        Courses course = courses.get(3);
        System.out.println(course.name);




    }
}
