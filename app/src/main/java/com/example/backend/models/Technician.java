package com.example.backend.models;

import java.io.Serializable;

public class Technician implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String specialty;
    private String expectedVisitTime;
    private double rating;

    public Technician(String id, String name, String phone, String specialty, String expectedVisitTime, double rating) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.specialty = specialty;
        this.expectedVisitTime = expectedVisitTime;
        this.rating = rating;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getExpectedVisitTime() { return expectedVisitTime; }
    public void setExpectedVisitTime(String expectedVisitTime) { this.expectedVisitTime = expectedVisitTime; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
