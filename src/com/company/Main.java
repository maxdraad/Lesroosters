package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public int N_CLASSROOMS = 7;
    public int N_TIME_SLOTS = 5;
    public int N_DAYS = 5;
    public int N_STUDENTS = 609;
    public int N_SUBJECTS = 29;

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
            System.out.println(courses);
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

                List<String> studentCourses;
                studentCourses = gegevens.subList(3, gegevens.size());
                //Maakt een lijst aan van de vakken die bij de student horen

                /*
                List<String> studentCourses;
                if (gegevens.size() >= 3){
                    studentCourses = gegevens.subList(3, gegevens.size());
                    System.out.println(studentCourses);
                }
                else{
                    studentCourses = [];
                    System.out.println("empty list");
                }
                */

                // Voegt student toe aan course


                for (int i = 0; i < courses.size(); i++){
                    Courses course = courses.get(i);
                    if (studentCourses.contains(course)){
                            course.courseStudents.add(studentNumber);   // Somehow werkt dit nog niet goed
                            System.out.println(course);
                    }
                    System.out.println(course.courseStudents.size());
                }



                Student newStudent = new Student(lastName, firstName, studentNumber, studentCourses);
                students.add(newStudent);

              //  String vak = gegevens.get(3);
        /*              for (Course course : courses) {
                    if (course.getName() == vak) {
                        student.addCourse(course);
                        course.addStudent(student);
                    }
                }*/

            }
            csvStudentGegevens.close();
        } catch (IOException e) {
            System.out.println("File Read Error Students");
        }




        // Studenten tellen voor een bepaald vak - test
        int studentenModerneDatabases = 0;

        for(int i = 0; i < students.size(); i++){
            Student student = students.get(i);
            if (student.studentCourses.contains("Moderne Databases")){
                studentenModerneDatabases += 1;
            }
        //    System.out.println(student);
        }

        System.out.println(studentenModerneDatabases); // Dit geeft al het goede aantal studenten voor dit vak

        // ------------------------------------------------

    }
}
