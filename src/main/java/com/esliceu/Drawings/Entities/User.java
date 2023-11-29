package com.esliceu.Drawings.Entities;

public class User {

    private int id;
    private String username;
    private String nameAndLastname;
    private String password;

    public User() {
    }

    public User(String username, String nameAndLastname, String password, int id) {
        this.id = id;
        this.username = username;
        this.nameAndLastname = nameAndLastname;
        this.password = password;
    }

    public String getNameAndLastname() {
        return nameAndLastname;
    }

    public void setNameAndLastname(String nameAndLastname) {
        this.nameAndLastname = nameAndLastname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
