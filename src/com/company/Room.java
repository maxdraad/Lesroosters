package com.company;

public class Room {
    public String name;
    public int capacity;

    public Room(String name, int capacity){
        this.name = name;
        this.capacity = capacity;
    }

    public String toString() {
        return name+" "+capacity;
    }
}
