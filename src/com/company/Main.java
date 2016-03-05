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


    public static void main(String[] args) {

        // ArrayList to keep al courses, rooms and students and their information
        List<Course> courses = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<Activity> activities = new ArrayList<>();


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

                Course newCourse = new Course(name, numberLectures, numberWorkgroups, maxStudentsGroups, numberPracticum, maxStudentsPracticum, courseStudents);
                courses.add(newCourse);
            }
            csvVakkenGegevens.close();
        }
        catch (IOException e) {
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

        }
        catch (IOException e){
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

                // Voegt student toe aan course
                for (int i = 0; i < courses.size(); i++){
                    Course course = courses.get(i);
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

        //Test Prints. Herkent ook of er meerdere werkgroepen nodig zijn!!
        for(int i = 0; i < courses.size(); i++){
            Course course = courses.get(i);
            System.out.println(course.name+" "+course.courseStudents.size());
            if (course.courseStudents.size() > course.maxStudentsGroups && course.numberWorkGroups > 0){
                int numberGroups = (int) Math.ceil(((double) course.courseStudents.size())/((double) course.maxStudentsGroups));
                System.out.println(ANSI_RED + numberGroups + " werkgroepen nodig (Capaciteit Werkgroep = "
                         + course.maxStudentsGroups+ANSI_RESET+")");

            }
            if  (course.courseStudents.size() > course.maxStudentsPracticum && course.numberPracticum > 0){

                int numberPracticumGroups = (int) Math.ceil(((double) course.courseStudents.size())/((double) course.maxStudentsPracticum));
                System.out.println(ANSI_RED + numberPracticumGroups +
                        " practicumgroepen nodig (Capaciteit Practicumgroep=" + course.maxStudentsPracticum+ANSI_RESET+")");
            }
        }




        //For loop die activities aanmaakt
        //Hier zouden we de eerste heuristieken kunnen toepassen

        for(int i = 0; i < courses.size(); i++){
            Course course = courses.get(i);

            // Maakt 1 hoorcollege groep aan
            if (course.numberLectures > 0) {
                Activity hoorcollege = new Activity(course.name, "Hoorcollege", course.numberLectures, 1, course.courseStudents);
                activities.add(hoorcollege);
            }

            // Algoritme om werkgroepen aan te maken
            if (course.numberWorkGroups > 0) {

                // Verdeelt studenten over groepen als er meer studenten zijn dan capaciteit van 1 werkgroep
                if (course.courseStudents.size() > course.maxStudentsGroups) {
                    int numberGroups = (int) Math.ceil(((double) course.courseStudents.size()) / ((double) course.maxStudentsGroups));
                    int j;
                    for (j = 1; j < numberGroups; j++) {
                        List<Integer> studentsWorkGroup = course.courseStudents.subList((j - 1) * course.maxStudentsGroups, j * course.maxStudentsGroups - 1);
                        Activity workGroup = new Activity(course.name, "Werkgroep", course.numberWorkGroups, j, studentsWorkGroup);
                        activities.add(workGroup);
                    }
                    List studentsWorkGroup = course.courseStudents.subList(course.maxStudentsGroups*(j-1), course.courseStudents.size());
                    Activity workGroup = new Activity(course.name, "Werkgroep", course.numberWorkGroups, j, studentsWorkGroup);
                    activities.add(workGroup);

                }
                //Maakt 1 werkgroep
                else {
                    Activity workGroup = new Activity(course.name, "Werkgroep", course.numberWorkGroups, 1, course.courseStudents);
                    activities.add(workGroup);
                }
            }

            // Algoritme om practicumgroepen aan te maken
            if (course.numberPracticum > 0) {

                    // Verdeelt studenten over practicumgroepen als er meer studenten zijn dan capaciteit van 1 werkgroep
                if (course.courseStudents.size() > course.maxStudentsPracticum){
                    int numberGroups = (int) Math.ceil(((double) course.courseStudents.size())/((double) course.maxStudentsPracticum));
                    int j;

                    for (j = 1; j < numberGroups; j++){
                        List<Integer> studentsPracticumGroup = course.courseStudents.subList(((j-1)*course.maxStudentsPracticum), j*course.maxStudentsPracticum);
                        Activity workGroup = new Activity(course.name, "Practicum", course.numberPracticum, j, studentsPracticumGroup );
                        activities.add(workGroup);
                    }
                    List studentsWorkGroup = course.courseStudents.subList(course.maxStudentsPracticum*(j-1), course.courseStudents.size());
                    Activity workGroup = new Activity(course.name, "Practicum", course.numberPracticum, j, studentsWorkGroup);
                    activities.add(workGroup);
                }

                //Maakt 1 Practicumgroep
                else {
                    Activity practicumGroup = new Activity(course.name, "Practicum", course.numberPracticum, 1, course.courseStudents);
                    activities.add(practicumGroup);

                }


            }
        }

        for(int i = 0; i < activities.size();i++) {
            System.out.println(activities.get(i));
        }
        System.out.println("Aantal Activities: " + activities.size());



    }
}
