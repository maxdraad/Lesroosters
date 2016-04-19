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

    // ArrayList to keep al courses, rooms and students and their information
    public List<Course> courses = new ArrayList<>();
    public List<Room> rooms = new ArrayList<>();
    public List<Student> students = new ArrayList<>();
    public List<Activity> activities = new ArrayList<>();
    //public List<Timeslot> timeslots = new ArrayList<>();

    //Random number generator
    public Random intGenerator = new Random();

    //Score keeper
    public int scoreValue;

    public int timeslots = 20;
    public int timeslotsNight = 25;
    public int amountOfRooms;
    public int iterationsCounter = 0;
    public int iterationsLimit = 5000000;

    public int studentConflictCounter;
    public int capacityConflictCounter;
    public int nightSlotPenaltyCount;


    public static void main(String[] args) {
        new Main().go();
    }

    public void go() {

        getCourses();
        getRooms();
        getStudents();
        makeActivities();
        makeRandomSchedule();
        computeScore();

        System.out.println("Nachtslot Maluspunten: "+nightSlotPenaltyCount);
        System.out.println(studentConflictCounter + " studenten zijn dubbel geroosterd!");
        System.out.println(capacityConflictCounter + " studenten passen niet in hun lokaal!");
        System.out.println("Totale score: " + scoreValue);

        while(iterationsCounter < iterationsLimit){
            swapRandomActivities();
            iterationsCounter++;
        }

        System.out.println("Score na "+iterationsLimit+" iteraties: "+scoreValue);
        System.out.println("Nachtslot Maluspunten: "+nightSlotPenaltyCount);
        System.out.println(studentConflictCounter + " studenten zijn dubbel geroosterd!");
        System.out.println(capacityConflictCounter + " studenten passen niet in hun lokaal!");

        // Het valt op dat het algoritme het nachtslot toch vaak gebruikt, de kans dat hij een activity in een nachtslot
        // swapt met een timeslot dat niet in het nachtslot zit is:
        // (5/145)*(140/145)*(16/145) = 0.00367378736 = 0.367378736%
        //  1.00367378736 log (1.5) = 110.56966814, dus er is op 111 interaties een 50% kans voor zo'n swap.
        // Blijkbaar is het nuttig het avondslot te gebruiken

    }

    // Read all courses from file and define their features
    public void getCourses(){
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
            System.out.println("File Read Error Course ");
        }
    }

    // Read in all lecture rooms and their capacity
    public void getRooms(){
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

    // Read in all students and define their features
    public void getStudents(){
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

    /* public void makeTimeslots(){
        //Maakt 5 timeslots aan//
        for(int i = 1; i<= 5; i++){
            for (int j = 1; j <= 5; j++) {
                Timeslot newTimeslot = new Timeslot(7 + (2 * j) + " tot " + 9 + (2 * j), i);
                timeslots.add(newTimeslot);
            }
        }
    }
    */

    public void makeActivities(){
        //For loop die activities aanmaakt
        //Hier zouden we de eerste heuristieken kunnen toepassen
        for (Course course: courses) {
            activities.addAll(createActivity(course, course.numberLectures, 0, "Hoorcollege", true));
            activities.addAll(createActivity(course, course.numberWorkGroups, course.maxStudentsGroups, "Werkcollege", false));
            activities.addAll(createActivity(course, course.numberPracticum, course.maxStudentsPracticum, "Practicum", false));
        }

        /*
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
           activities.get(i).occurrence + " | Groep: " + activities.get(i).groupNumber + " | Aantal studenten: " + activities.get(i).studentGroup.size() + " | Studentnummers: " +
           studentNumberList);
        }
        */

        System.out.println("Aantal Activities: " + activities.size());
    }

    public void makeRandomSchedule(){
        amountOfRooms = rooms.size();
        for (Activity activity : activities) {
            int i = intGenerator.nextInt(amountOfRooms);
            int amountOfTimeslots = rooms.get(i).timetable.size();
            int j = intGenerator.nextInt(amountOfTimeslots);
            while (rooms.get(i).timetable.get(j) != null) {
                i = intGenerator.nextInt(amountOfRooms);
                amountOfTimeslots = rooms.get(i).timetable.size();
                j = intGenerator.nextInt(amountOfTimeslots);
            }
            rooms.get(i).timetable.set(j, activity);
            /*System.out.println(activities.get(x).course.name+ " " + activities.get(x).activity + " ingedeeld in lokaal " + rooms.get(i).name + ", timeslot " + j );
              try {
                Thread.sleep(100);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
        }
        for (Room room : rooms) System.out.println(room.name + "  " + room.nightSlot + " " + room.timetable);

    }

    public void computeScore(){

        int activityCounter = 0;
        for (Activity activity : activities){
            for (Room room : rooms){
                if (room.timetable.contains(activity)){
                    activityCounter++;
                    break;
                }
            }
        }
        if (activityCounter == activities.size()){
            //System.out.println("1000 punten, alle activities ingeroosterd");
            scoreValue = 1000;
        }
        else{
            //System.out.println("0 punten, niet alle activities zijn ingeroosterd");
            scoreValue = 0;
        }

        /*
        Functie die rooster conflicten checkt
        TODO: Een student wordt nog steeds (drie/vier) dubbel gecheckt, als ze op timeslot 1 in lokaal 1 zitten wordt
        een conflict geteld als hij ook in lokaal 2 zit, maar opnieuw vanuit lokaal 2 naar 1.
        */

        studentConflictCounter = 0;
        for (Room room : rooms){
            for (int i = 0 ; i < timeslots ; i++){
                Activity activity = room.timetable.get(i);
                if (activity != null) {
                    for (Student student : activity.studentGroup) {
                        for (Room otherRoom : rooms) {
                            Activity otherActivity = otherRoom.timetable.get(i);
                            if (otherActivity != null && otherActivity != activity) {
                                if (otherActivity.studentGroup.contains(student)) {
                                    studentConflictCounter++;
                                }
                            }
                        }
                    }
                }
            }
        }


        // Functie die capacatiteit conflicten checkt
        capacityConflictCounter = 0;
        for (Room room : rooms){
            for (Activity activity : room.timetable){
                if(activity != null) {
                    if (room.capacity < activity.studentGroup.size()) {
                        int numberOfConflict = activity.studentGroup.size() - room.capacity;
                        capacityConflictCounter = capacityConflictCounter + numberOfConflict;
                    }
                }
            }
        }

        nightSlotPenaltyCount = 0;
        // Checkt gebruik van avondslot
        for (Room room: rooms){
            if (room.nightSlot){
                for (int fromSlot = timeslots; fromSlot < timeslotsNight; fromSlot++){
                    if (room.timetable.get(fromSlot) != null){
                        nightSlotPenaltyCount = nightSlotPenaltyCount + 50;
                    }
                }
            }
        }

        scoreValue = scoreValue - capacityConflictCounter - studentConflictCounter - nightSlotPenaltyCount;



    }

    // Hill climbing swapactivities
    public void swapRandomActivities(){
        int room1 = intGenerator.nextInt(amountOfRooms);
        int amountOfTimeslots1 = rooms.get(room1).timetable.size();
        int timeslot1 = intGenerator.nextInt(amountOfTimeslots1);
        Activity activity1 = rooms.get(room1).timetable.get(timeslot1);

        int room2 = intGenerator.nextInt(amountOfRooms);
        int amountOfTimeslots2 = rooms.get(room2).timetable.size();
        int timeslot2 = intGenerator.nextInt(amountOfTimeslots2);
        Activity activity2 = rooms.get(room2).timetable.get(timeslot2);

        int currentScore = scoreValue;

        rooms.get(room1).timetable.set(timeslot1, activity2);
        rooms.get(room2).timetable.set(timeslot2, activity1);
        computeScore();
        if (scoreValue <= currentScore){
            rooms.get(room1).timetable.set(timeslot1, activity1);
            rooms.get(room2).timetable.set(timeslot2, activity2);
        }

    }

    //Method om activities mee te maken
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
                    List<Student> studentsWorkGroup = course.courseStudents.subList(maxNumberStudents * (j - 1), course.courseStudents.size());
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


