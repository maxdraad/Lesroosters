package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Main {

    //push test Max
    //Om printout tekst in kleur te krijgen
    public static final String ANSI_RED = "\u001B[31m";
    //En weer zwart te maken
    public static final String ANSI_RESET = "\u001B[0m";

    // ArrayList to keep al courses, rooms and students and their information
    public List<Course> courses = new ArrayList<>();
    public List<Room> rooms = new ArrayList<>();
    public List<Student> students = new ArrayList<>();
    public List<Activity> activities = new ArrayList<>();
    public List<Timeslot> timeslots = new ArrayList<>();

    public static void main(String[] args) {
        new Main().go();
    }

    public void go() {

        getCourses();
        getRooms();
        getStudents();
        makeTimeslots();
        makeActivities();
        Timetable.makeTable(activities, timeslots);

        // Dit stukje maakt de eerste prototype roosters :)

        rooms.get(1).timetable.add(3, activities.get(1));
        rooms.get(1).timetable.add(6, activities.get(4));
        rooms.get(2).timetable.add(20, activities.get(4));
        for (int i=1; i< rooms.size();i++){
            System.out.println(rooms.get(i).name+ "  "+rooms.get(i).nightSlot + " " +rooms.get(i).timetable);
        }


    }

    public void getCourses(){
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
    }

    public void getRooms(){
        // Read in all lecture rooms and their capacity
        try {
            BufferedReader csvZaalGegevens = new BufferedReader(new FileReader("resources/zalen_roostering.csv"));
            String room;
            csvZaalGegevens.readLine();

            while ((room = csvZaalGegevens.readLine()) != null) {
                List<String> gegevensZaal = Arrays.asList(room.split(";"));
                String roomName = gegevensZaal.get(0);
                int capacity = Integer.parseInt(gegevensZaal.get(1));
                if (capacity > 100) {
                    List<Activity> list = new ArrayList<>(25);
                    for (int i = 0; i < 25; i++) {
                        list.add(null);
                    }
                    Room newRoom = new Room(roomName, capacity, true, list);
                    rooms.add(newRoom);
                } else {
                    List<Activity> list = new ArrayList<>(2);
                    for (int i = 0; i < 20; i++) {
                        list.add(null);
                    }
                    Room newRoom = new Room(roomName, capacity, false, list);
                    rooms.add(newRoom);
                }

            }
            csvZaalGegevens.close();
        } catch (IOException e) {
            System.out.println("File Read Error Rooms");
        }


    }

    public void getStudents(){
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
    }

    public void makeTimeslots(){
        //Maakt 5 timeslots aan//
        for(int i = 1; i<= 5; i++){
            for (int j = 1; j <= 5; j++) {
                Timeslot newTimeslot = new Timeslot(7 + (2 * j) + " tot " + 9 + (2 * j), i);
                timeslots.add(newTimeslot);
            }
        }
    }

    public void makeActivities(){
        //For loop die activities aanmaakt
        //Hier zouden we de eerste heuristieken kunnen toepassen


        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            activities.addAll(createActivity(course, course.numberLectures, 0, "Hoorcollege", true));
            activities.addAll(createActivity(course, course.numberWorkGroups, course.maxStudentsGroups, "Werkcollege", false));
            activities.addAll(createActivity(course, course.numberPracticum, course.maxStudentsPracticum, "Practicum", false));
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

//            System.out.println(activities.get(i).course.name + " | Aantal " + activities.get(i).activity + ": " +
//                    activities.get(i).occurrence + " | Groep: " + activities.get(i).groupNumber + " | Aantal studenten: " + activities.get(i).studentGroup.size() + " | Studentnummers: " +
//                    studentNumberList);
        }

        System.out.println("Aantal Activities: " + activities.size());
    }

    //Method om activities mee te maken. WERKT :))
    public List<Activity> createActivity(Course course, Integer activitiesPerWeek, Integer maxNumberStudents, String nameLectureType, boolean hoorcollege){
        List<Activity> activities = new ArrayList<>();
        // Algoritme om (werk)groepen aan te maken
        for (int i = 1; i <= activitiesPerWeek; i++) {
            if (!hoorcollege && activitiesPerWeek >= 1) {

                // Verdeelt studenten over groepen als er meer studenten zijn dan capaciteit van 1 (werk)groep
                if (course.courseStudents.size() > maxNumberStudents) {
                    int numberGroups = (int) Math.ceil(((double) course.courseStudents.size()) / ((double) maxNumberStudents));
                    int j;
                    for (j = 1; j < numberGroups; j++) {
                        List<Student> studentsWorkGroup = course.courseStudents.subList((j - 1) * maxNumberStudents, j * maxNumberStudents);


                        Activity workGroup = new Activity(course, nameLectureType, i, j, studentsWorkGroup);
                        activities.add(workGroup);
                    }
                    List studentsWorkGroup = course.courseStudents.subList(maxNumberStudents * (j - 1), course.courseStudents.size());
                    Activity workGroup = new Activity(course, nameLectureType, i, j, studentsWorkGroup);
                    activities.add(workGroup);

                }
                //Maakt 1 (werk)groep
                else {
                    Activity subGroup = new Activity(course, nameLectureType, i, 1, course.courseStudents);
                    activities.add(subGroup);
                }
            } else if (hoorcollege && activitiesPerWeek >= 1) {
                Activity hoorCollege = new Activity(course, nameLectureType, i, 1, course.courseStudents);
                activities.add(hoorCollege);
            }
        }
        return activities;
    }
}


