package com.company;

import java.util.List;

public class Course {
    public String name;
    public int numberLectures;
    public int numberWorkGroups;
    public int maxStudentsGroups;
    public int numberPracticum;
    public int maxStudentsPracticum;
    public List<Student> courseStudents;
    public int numberOfGroups;


    public Course(String name, int numberLectures, int numberWorkGroups, int maxStudentsGroups,
                  int numberPracticum, int maxStudentsPracticum, List<Student> courseStudents, int numberOfGroups){
        this.name = name;
        this.numberLectures = numberLectures;
        this.courseStudents = courseStudents;
        this.numberWorkGroups = numberWorkGroups;
        this.maxStudentsGroups = maxStudentsGroups;
        this.numberPracticum = numberPracticum;
        this.maxStudentsPracticum = maxStudentsPracticum;
        this.numberOfGroups = numberOfGroups;
    }

    public String toString() {
        return name+" "+numberLectures+" "+courseStudents;
    }


}
