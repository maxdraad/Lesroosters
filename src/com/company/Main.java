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

        ArrayList<Courses> courses = new ArrayList<>();
        ArrayList<Room> rooms = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();

        try {
            BufferedReader csvVakkenGegevens = new BufferedReader(new FileReader("resources/vakken_roostering.csv"));
            String course;
            course = csvVakkenGegevens.readLine();

            while ((course = csvVakkenGegevens.readLine()) != null) {
                List<String> gegevensVak = Arrays.asList(course.split(";"));
                String name = gegevensVak.get(0);
                int numberLectures = Integer.parseInt(gegevensVak.get(1));
                int numberWorkgroups = Integer.parseInt(gegevensVak.get(2));
                int maxStudentsGroups = Integer.parseInt(gegevensVak.get(3));
                int numberPracticum = Integer.parseInt(gegevensVak.get(4));
                int maxStudentsPracticum = Integer.parseInt(gegevensVak.get(5));

                Courses newCourse = new Courses(name, numberLectures);
                courses.add(newCourse);
            }
            csvVakkenGegevens.close();
        } catch (IOException e) {
            System.out.println("File Read Error Courses");
        }

        try {
            BufferedReader csvZaalGegevens = new BufferedReader(new FileReader("resources/zalen_roostering.csv"));
            String room;
            room = csvZaalGegevens.readLine();

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

        try {
            BufferedReader csvStudentGegevens = new BufferedReader(new FileReader("resources/studenten_roostering.csv"));
            String student;
            student = csvStudentGegevens.readLine();

            while ((student = csvStudentGegevens.readLine()) != null) {
                List<String> gegevens = Arrays.asList(student.split(";"));
                String lastName = gegevens.get(0);
                String firstName = gegevens.get(1);
                int studentNumber = Integer.parseInt(gegevens.get(2));

                Student newStudent = new Student(lastName, firstName, studentNumber);
                students.add(newStudent);

                String vak = gegevens.get(3);
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

    }
}
