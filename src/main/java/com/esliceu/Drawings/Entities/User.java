package com.esliceu.Drawings.Entities;

public class User {

    private String username;
    private String nameAndLastname;
    private String password;

    public User(String username, String nameAndLastname, String password) {
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
