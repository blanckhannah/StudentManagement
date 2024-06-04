package com.techelevator.StudentBrowseSystem.models;

import lombok.Getter;
import lombok.Setter;


public class Teacher {
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    public Teacher(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    public Teacher() {
    }

    @Override
    public String toString() {
        return firstName +
                " " + lastName +
                ", email: " + email +
                ", id: " + id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
