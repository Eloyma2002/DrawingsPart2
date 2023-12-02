package com.esliceu.Drawings.Entities;


import java.sql.Timestamp;
import java.time.Instant;
public class Drawing {

    private int id;
    private String name;
    private User user;
    private int idUser;
    private boolean view;
    private Timestamp date;
    private boolean trash;

    public Drawing() {

    }
    public Drawing(String name, User user,boolean view) {
        this.name = name;
        this.user = user;
        this.view = view;
        this.date = Timestamp.from(Instant.now());
        this.trash = false;
    }

    public boolean getView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
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

    public boolean isView() {
        return view;
    }

    public boolean isTrash() {
        return trash;
    }

    public void setTrash(boolean trash) {
        this.trash = trash;
    }

    public Timestamp getDate() {
        return date;
    }

}
