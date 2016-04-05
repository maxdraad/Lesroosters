package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Main {

    //Om printout tekst in kleur te krijgen
    public static final String ANSI_RED = "\u001B[31m";
    //En weer zwart te maken
    public static final String ANSI_RESET = "\u001B[0m";

    //method

    public static void main(String[] args) {

        // ArrayList to keep al courses, rooms and students and their information
        List<Course> courses = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<Activity> activities = new ArrayList<>();
        List<Timeslot> timeslot = new ArrayList<>();


        // Read all courses from file and define their features
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

                List<Student> courseStudents = new ArrayList<Student>();

                Course newCourse = new Course(name, numberLectures, numberWorkgroups, maxStudentsGroups, numberPracticum, maxStudentsPracticum, courseStudents);
                courses.add(newCourse);
            }
            csvVakkenGegevens.close();
        } catch (IOException e) {
            System.out.println("File Read Error Course");
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

        } catch (IOException e) {
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


                //Instantiation of variable with list of Course belonging to student
                List<String> studentCourses = gegevens.subList(3, gegevens.size());


                Student newStudent = new Student(lastName, firstName, studentNumber, studentCourses);
                students.add(newStudent);

                // Voegt student toe aan course
                for (int i = 0; i < courses.size(); i++) {
                    Course course = courses.get(i);
                    if (studentCourses.contains(course.name)) {
                        course.courseStudents.add(newStudent);
                    }
                }

            }
            csvStudentGegevens.close();
        } catch (IOException e) {
            System.out.println("File Read Error Students");
        }


        //For loop die activities aanmaakt
        //Hier zouden we de eerste heuristieken kunnen toepassen

        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);

            // Maakt 1 hoorcollege groep aan
            if (course.numberLectures > 0) {
                Activity hoorcollege = new Activity(course, "Hoorcollege", course.numberLectures, 1, course.courseStudents);
                activities.add(hoorcollege);
            }


            // Algoritme om werkgroepen aan te maken
            if (course.numberWorkGroups > 0) {

                // Verdeelt studenten over groepen als er meer studenten zijn dan capaciteit van 1 werkgroep
                if (course.courseStudents.size() > course.maxStudentsGroups) {
                    int numberGroups = (int) Math.ceil(((double) course.courseStudents.size()) / ((double) course.maxStudentsGroups));
                    int j;
                    for (j = 1; j < numberGroups; j++) {
                        List<Student> studentsWorkGroup = course.courseStudents.subList((j - 1) * course.maxStudentsGroups, j * course.maxStudentsGroups);
                        Activity workGroup = new Activity(course, "Werkgroep", course.numberWorkGroups, j, studentsWorkGroup);
                        activities.add(workGroup);
                    }
                    List studentsWorkGroup = course.courseStudents.subList(course.maxStudentsGroups * (j - 1), course.courseStudents.size());
                    Activity workGroup = new Activity(course, "Werkgroep", course.numberWorkGroups, j, studentsWorkGroup);
                    activities.add(workGroup);

                }
                //Maakt 1 werkgroep
                else {
                    Activity workGroup = new Activity(course, "Werkgroep", course.numberWorkGroups, 1, course.courseStudents);
                    activities.add(workGroup);
                }
            }

            // Algoritme om practicumgroepen aan te maken
            if (course.numberPracticum > 0) {

                // Verdeelt studenten over practicumgroepen als er meer studenten zijn dan capaciteit van 1 werkgroep
                if (course.courseStudents.size() > course.maxStudentsPracticum) {
                    int numberGroups = (int) Math.ceil(((double) course.courseStudents.size()) / ((double) course.maxStudentsPracticum));
                    int j;

                    for (j = 1; j < numberGroups; j++) {
                        List<Student> studentsPracticumGroup = course.courseStudents.subList(((j - 1) * course.maxStudentsPracticum), j * course.maxStudentsPracticum);
                        Activity workGroup = new Activity(course, "Practicum", course.numberPracticum, j, studentsPracticumGroup);
                        activities.add(workGroup);
                    }
                    List studentsWorkGroup = course.courseStudents.subList(course.maxStudentsPracticum * (j - 1), course.courseStudents.size());
                    Activity workGroup = new Activity(course, "Practicum", course.numberPracticum, j, studentsWorkGroup);
                    activities.add(workGroup);
                }

                //Maakt 1 Practicumgroep
                else {
                    Activity practicumGroup = new Activity(course, "Practicum", course.numberPracticum, 1, course.courseStudents);
                    activities.add(practicumGroup);


                }
            }

        }


        //Print alle Activities met bijbehorende studentnummers van studenten
        for (int i = 0; i < activities.size(); i++) {

            //Stukje code die studentNumber uit lijst van Student extraheert
            List studentList = activities.get(i).studentGroup;
            List<Integer> studentNumberList = new ArrayList<>();
            int studentNumber;
            for (int x = 0; x < studentList.size(); x++) {

                Student thisStudent = (Student) studentList.get(x);
                studentNumber = thisStudent.studentNumber;
                studentNumberList.add(studentNumber);
            }

            System.out.println(activities.get(i).course.name + " | Aantal " + activities.get(i).activity + ": " +
                    activities.get(i).occurrences + " | Groep: " + activities.get(i).groupNumber + " | Aantal studenten: " + activities.get(i).studentGroup.size() + " | Studentnummers: " +
                    studentNumberList);
        }

        System.out.println("Aantal Activities: " + activities.size());


        //Maakt 5 timeslots aan//
        for (int i = 1; i <= 5; i++) {
            Timeslot newTimeslot = new Timeslot(7 + (2 * i) + " tot " + 9 + (2 * i));
            timeslot.add(newTimeslot);
        }



    }

    //Method om activities mee te maken. WORK IN PROGRESS!!
    public List<Activity> createActivity(Course course, Integer activitiesPerWeek, Integer maxNumberStudents, String nameLectureType){
        List<Activity> activities = new ArrayList<>();
        // Algoritme om werkgroepen aan te maken
        if (activitiesPerWeek > 0) {

            // Verdeelt studenten over groepen als er meer studenten zijn dan capaciteit van 1 werkgroep
            if (course.courseStudents.size() > maxNumberStudents) {
                int numberGroups = (int) Math.ceil(((double) course.courseStudents.size()) / ((double) course.maxStudentsGroups));
                int j;
                for (j = 1; j < numberGroups; j++) {
                    List<Student> studentsWorkGroup = course.courseStudents.subList((j - 1) * course.maxStudentsGroups, j * course.maxStudentsGroups);
                    Activity workGroup = new Activity(course, "Werkgroep", course.numberWorkGroups, j, studentsWorkGroup);
                    activities.add(workGroup);
                }
                List studentsWorkGroup = course.courseStudents.subList(course.maxStudentsGroups * (j - 1), course.courseStudents.size());
                Activity workGroup = new Activity(course, "Werkgroep", course.numberWorkGroups, j, studentsWorkGroup);
                activities.add(workGroup);

            }
            //Maakt 1 werkgroep
            else {
                Activity workGroup = new Activity(course, "Werkgroep", course.numberWorkGroups, 1, course.courseStudents);
                activities.add(workGroup);
            }
            return activities;
        }
        else {
            return null;
        }
    }
}
