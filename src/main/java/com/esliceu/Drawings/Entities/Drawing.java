package com.esliceu.Drawings.Entities;


import java.time.LocalDate;

public class Drawing {

    private int id;
    private String name;
    private User user;
    private String figures;
    private int numFigures;
    private LocalDate date;

    public Drawing(String name, User user, String figures) {
        this.name = name;
        this.user = user;
        this.figures = figures;
        this.numFigures = numFigures;
        this.date = LocalDate.now();
    }

    public int getNumFigures() {
        return numFigures;
    }

    public void setNumFigures(int numFigures) {
        this.numFigures = numFigures;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFigures() {
        return figures;
    }

    public void setFigures(String figures) {
        this.figures = figures;
    }
}
