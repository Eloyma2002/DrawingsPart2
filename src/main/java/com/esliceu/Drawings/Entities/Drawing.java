package com.esliceu.Drawings.Entities;


import java.time.LocalDate;

public class Drawing {

    private int id;
    private String name;
    private User user;
    private String figures;
    private int numFigures;
    private LocalDate date;

    private int idUser;
    private boolean view;

    public Drawing() {

    }
    public Drawing(String name, User user, String figures, boolean view) {
        this.name = name;
        this.user = user;
        this.figures = figures;
        this.date = LocalDate.now();
        this.view = view;
    }

    public int getNumFigures() {
        return numFigures;
    }

    public void setNumFigures(int numFigures) {
        this.numFigures = numFigures;
    }

    public boolean getView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }
    public LocalDate getDate() {
        return date;
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
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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
