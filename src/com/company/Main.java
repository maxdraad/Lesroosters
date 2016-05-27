package com.company;

import java.io.*;
import java.util.*;
import java.lang.Math;


public class Main {

    // Lijsten om vakken, lokalen, studenten en activiteiten in op te slaan
    public List<Course> courses = new ArrayList<>();
    public List<Room> rooms = new ArrayList<>();
    public List<Student> students = new ArrayList<>();
    public List<Activity> activities = new ArrayList<>();

    // Random getal generator
    public Random numberGenerator = new Random();

    // Het aantal normale tijdslots
    public static int timeslots = 20;
    // Het aantal normale tijdslots + nachtslots (met maluspunten)
    public static int timeslotsNight = 25;

    public int amountOfRooms;

    // Telt iteraties van de Hill Climber
    public int iterationsCounter = 0;

    // Limiet van het aantal iteraties van de Hill Climber
    public static int iterationsLimit = 100;

    public int fileNumber = 4;
    public List<Integer> scores = new ArrayList<>();

    public static void main(String[] args) {
        new Main().go();
    }

    public void go() {

        // Alle data inlezen
        getCourses();
        getRooms();
        getStudents();
        makeActivities();

        // Eerste (random) rooster
        makeRandomSchedule();
        int scoreValue = computeScore();

        // Gegevens van het eerste rooster.
        System.out.println("Nachtslot Maluspunten: " + computeNightslotPenalty());
        System.out.println(computeStudentConflicts() + " studenten zijn dubbel geroosterd!");
        System.out.println(computeCapacityConflicts() + " studenten passen niet in hun lokaal!");
        System.out.println("Totale score: " + scoreValue);
        System.out.println("Aantal activities: " + activities.size());

        hillClimber();

        // Gegevens van het best gevonden rooster
        System.out.println("Score na " + iterationsLimit + " iteraties: " + computeScore());
        System.out.println("Nachtslot Maluspunten: " + computeNightslotPenalty());
        System.out.println(computeStudentConflicts() + " studenten zijn dubbel geroosterd!");
        System.out.println(computeCapacityConflicts() + " studenten passen niet in hun lokaal!");

        for (Room room : rooms) System.out.println(room.name + "  " + room.nightSlot + " " + room.timetable);
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

                Course newCourse = new Course(name, numberLectures, numberWorkgroups, maxStudentsGroups,
                        numberPracticum, maxStudentsPracticum, courseStudents, 1);
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
                List<Activity> studentActivities = new ArrayList<>();

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

        //System.out.println("Aantal Activities: " + activities.size());
    }

    public void makeRandomSchedule(){
        amountOfRooms = rooms.size();
        for (Activity activity : activities) {
            int i = numberGenerator.nextInt(amountOfRooms);
            int amountOfTimeslots = rooms.get(i).timetable.size();
            int j = numberGenerator.nextInt(amountOfTimeslots);
            while (rooms.get(i).timetable.get(j) != null) {
                i = numberGenerator.nextInt(amountOfRooms);
                amountOfTimeslots = rooms.get(i).timetable.size();
                j = numberGenerator.nextInt(amountOfTimeslots);
            }
            rooms.get(i).timetable.set(j, activity);
        }
    }

    public int computeScore(){
    //public void computeScore(){

        int activityScore = computeActivityScore();
        int studentConflictCounter = computeStudentConflicts();
        int capacityConflictCounter = computeCapacityConflicts();
        int nightSlotPenaltyCount = computeNightslotPenalty();
        int distributionPoints = computeDistribution();

        return activityScore - studentConflictCounter - capacityConflictCounter
                - nightSlotPenaltyCount + distributionPoints;
    }

    //Deze method checkt of alle activiteiten daadwerkelijk ingeroosterd zijn
    public int computeActivityScore(){
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
            return 1000;
        }
        else{
            //System.out.println("0 punten, niet alle activities zijn ingeroosterd");
            return 0;
        }
    }

    //Deze method checkt rooster conflicten (Dubbele roosteringen van studenten)
    public int computeStudentConflicts(){
        ArrayList<Student> checkedStudentsList = new ArrayList<Student>();
        int studentConflictCounter = 0;
        boolean conflictFound = false;

        for (int i = 0 ; i < timeslots ; i++){
            for (int j = 0; j < rooms.size() - 1; j++){
                Activity activity = rooms.get(j).timetable.get(i);
                if (activity != null) {
                    for (Student student : activity.studentGroup) {
                        if (!checkedStudentsList.contains(student)) {
                            for (int k = j + 1; k < rooms.size(); k++) {
                                Room otherRoom = rooms.get(k);
                                Activity otherActivity = otherRoom.timetable.get(i);
                                if (otherActivity != null) {
                                    if (otherActivity.studentGroup.contains(student)) {
                                        studentConflictCounter++;
                                        conflictFound = true;
                                    }
                                }
                            }
                        }
                        if (conflictFound){
                            checkedStudentsList.add(student);
                            conflictFound = false;
                        }
                    }
                }
            }
            checkedStudentsList.clear();
        }
        return studentConflictCounter;
    }

    // Deze method checkt capacatiteit conflicten (te veel studenten in een zaal)
    public int computeCapacityConflicts(){
        int capacityConflictCounter = 0;
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
        return capacityConflictCounter;
    }

    // Deze method checkt het gebruik van het nachtslot
    public int computeNightslotPenalty(){
        int nightSlotPenaltyCount = 0;
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
        return nightSlotPenaltyCount;
    }

    // Deze method checkt de weekverdeling van activiteiten
    public int computeDistribution() {
        int distributionPoints = 0;

        for (Course course : courses) {
            List<List<Boolean>> workgroupWeeks = new ArrayList<>();
            for (int i = 0; i < course.numberOfGroups; i++) {
                List<Boolean> weekDistribution = new ArrayList<>();
                for (int s = 0; s < 5; s++) {
                    weekDistribution.add(false);
                }
                workgroupWeeks.add(weekDistribution);
            }
            int distributionMalus = computeDistributionMalus(course, workgroupWeeks);
            int distributionBonus = computeDistributionBonus(course, workgroupWeeks);
            int factorMalus;
            int factorBonus;
            switch (course.numberOfGroups){     //hardcoded
                case 1:
                    factorMalus = 10;
                    factorBonus = 20;
                    break;
                case 2:
                    factorMalus = 5;
                    factorBonus = 10;
                    break;
                case 3:
                    factorMalus = 3;
                    factorBonus = 7;
                    break;
                case 4:
                    factorMalus = 3;
                    factorBonus = 5;
                    break;
                case 5:
                    factorMalus = 2;
                    factorBonus = 4;
                    break;
                case 6:
                    factorMalus = 1;
                    factorBonus = 3;
                    break;
                default:
                    factorMalus = 1;
                    factorBonus = 1;
                    break;
            }

            distributionPoints = distributionPoints + (distributionBonus * factorBonus) - (distributionMalus * factorMalus);
        }
        return distributionPoints;
    }

    // Deze method checkt de maluspunten voor de weekverdeling van activiteiten
    public int computeDistributionMalus(Course course, List<List<Boolean>> workgroupWeeks){
        int distributionMalus = 0;
        for(int i = 0; i < timeslotsNight; i++){
            for(int j = 0; j < rooms.size(); j++) {
                if (!rooms.get(j).nightSlot && i >= timeslots){
                    break;
                }
                Activity activity = rooms.get(j).timetable.get(i);
                if(activity != null){
                    if(activity.course == course) {
                        int day;
                        switch (i){
                            case 0:case 1:case 2:case 3:case 20:
                                day = 0;
                                break;
                            case 4:case 5:case 6:case 7:case 21:
                                day = 1;
                                break;
                            case 8:case 9:case 10:case 11:case 22:
                                day = 2;
                                break;
                            case 12:case 13:case 14:case 15:case 23:
                                day = 3;
                                break;
                            case 16:case 17:case 18:case 19:case 24:
                                day = 4;
                                break;
                            default:
                                day = 0;
                                break;
                        }

                        if (activity.activity == "Hoorcollege"){
                            for(int l = 0; l < course.numberOfGroups; l++){
                                if (!workgroupWeeks.get(l).get(day)){
                                    workgroupWeeks.get(l).set(day, true);
                                }
                                else{
                                    distributionMalus++;
                                }
                            }
                        }
                        else{
                            int workgroup = activity.groupNumber - 1;
                            if (!workgroupWeeks.get(workgroup).get(day)) {
                                workgroupWeeks.get(workgroup).set(day, true);
                            }
                            else{
                                distributionMalus++;
                            }
                        }
                    }
                }
            }
        }
        return distributionMalus;
    }

    // Deze method checkt de bonuspunten voor de weekverdeling van activiteiten
    public int computeDistributionBonus(Course course, List<List<Boolean>> workgroupWeeks){
        int distributionBonus = 0;
        for (int i = 0; i < course.numberOfGroups; i++) {
            int numberOfActivities = course.numberLectures + course.numberWorkGroups + course.numberPracticum;
            List<Boolean> check = workgroupWeeks.get(i);
            int bonus = 0;
            switch (numberOfActivities){
                case 1:
                    if(check.get(0) || check.get(1) || check.get(2) || check.get(3) || check.get (4))
                    {
                        bonus = 1;
                    }
                    break;
                case 2:
                    if((check.get(0) && check.get(4)) || (check.get(0) && check.get(3)) || (check.get(1) && check.get(4))){
                        bonus = 1;
                    }
                    break;
                case 3:
                    if(check.get(0) && check.get(2) && check.get(4)){
                        bonus = 1;
                    }
                    break;
                case 4:
                    if(check.get(0) && check.get(1) && check.get(3) && check.get(4)){
                        bonus = 1;
                    }
                    break;
                default:
                    bonus = 0;
                    break;
            }
            distributionBonus += bonus;
        }
        return distributionBonus;
    }

    // Deze method is een hill climber waar iteraties en scores bijgehouden worden
    public void hillClimber(){
        while(iterationsCounter < iterationsLimit) {
            swapRandomActivities();
            scores.add(computeScore());
            iterationsCounter++;
            if (iterationsCounter % 10000 == 0){
                //System.out.println(scores);
                try {
                    FileWriter fw = new FileWriter("HillclimberFile" + fileNumber + ".txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw);
                    for(int i = 0; i < 10000; i++){
                        out.println(scores.get(i).toString());
                    }
                    out.close();
                }catch (IOException ex){
                    System.out.println("Something went wrong");
                }
                scores.clear();
            }
        }
    }

    // Deze method is de hill climber
    public void swapRandomActivities(){

        //Kies een random activiteit #1
        int room1 = numberGenerator.nextInt(amountOfRooms);
        int amountOfTimeslots1 = rooms.get(room1).timetable.size();
        int timeslot1 = numberGenerator.nextInt(amountOfTimeslots1);
        Activity activity1 = rooms.get(room1).timetable.get(timeslot1);

        //Kies een random activiteit #2
        int room2 = numberGenerator.nextInt(amountOfRooms);
        int amountOfTimeslots2 = rooms.get(room2).timetable.size();
        int timeslot2 = numberGenerator.nextInt(amountOfTimeslots2);
        Activity activity2 = rooms.get(room2).timetable.get(timeslot2);

        //Bereken de huidige score
        int currentScore = computeScore();

        //Verwissel de activiteiten
        rooms.get(room1).timetable.set(timeslot1, activity2);
        rooms.get(room2).timetable.set(timeslot2, activity1);

        //Bereken de score opnieuw
        int newScore = computeScore();

        if (/*scoreValue*/ newScore < currentScore && iterationsCounter > 0){

            //Hier is simulated annealing met functie f(n-iteraties) = 1-e^((VARIABELE/(newScore - currentScore))/n-iteraties)
            //VARIABELE kan aangepast worden, plot de grafiek om de kansverdeling te zien, hoger is vrijer

            double chance = 1 - Math.exp( ( 10000 / ( newScore - currentScore) ) / iterationsCounter  ) ;

            if (chance < numberGenerator.nextDouble()){
                rooms.get(room1).timetable.set(timeslot1, activity1);
                rooms.get(room2).timetable.set(timeslot2, activity2);
            }
        }

    }

    // Deze method maakt activities aan
    public List<Activity> createActivity(Course course, Integer activitiesPerWeek, Integer maxNumberStudents, String nameLectureType, boolean hoorcollege){
        List<Activity> activities = new ArrayList<>();
        // Algoritme om (werk)groepen aan te maken
        for (int i = 1; i <= activitiesPerWeek; i++) {
            if (!hoorcollege && activitiesPerWeek >= 1) {

                // Verdeelt studenten over groepen als er meer studenten zijn dan capaciteit van 1 (werk)groep
                if (course.courseStudents.size() > maxNumberStudents) {
                    int numberGroups = (int) Math.ceil(((double) course.courseStudents.size()) / ((double) maxNumberStudents));
                    int numberStudentsGroup = (int) Math.ceil(((double) course.courseStudents.size()) / ((double) numberGroups));

                    course.numberOfGroups = numberGroups;
                    int j;
                    for (j = 1; j < numberGroups; j++) {
                        List<Student> studentsWorkGroup = course.courseStudents.subList((j - 1) * numberStudentsGroup, j * numberStudentsGroup);
                        Activity workGroup = new Activity(course, nameLectureType, i, j, studentsWorkGroup);
                        activities.add(workGroup);
                    }
                    List<Student> studentsWorkGroup = course.courseStudents.subList(numberStudentsGroup * (j - 1), course.courseStudents.size());
                    Activity workGroup = new Activity(course, nameLectureType, i, j, studentsWorkGroup);
                    activities.add(workGroup);

                }
                //Maakt 1 (werk)groep
                else {
                    Activity subGroup = new Activity(course, nameLectureType, i, 1, course.courseStudents);
                    activities.add(subGroup);
                }
                //Maakt 1 hoorcollege
            } else if (hoorcollege && activitiesPerWeek >= 1) {
                Activity hoorCollege = new Activity(course, nameLectureType, i, 1, course.courseStudents);
                activities.add(hoorCollege);
            }
        }
        return activities;
    }


}


