package com.esliceu.Drawings.DTO;

import java.sql.Timestamp;

public class DrawingDTO {
    private int id;
    private int numFigures;
    private int idUser;
    private boolean visualization;
    private String name;
    private String username;
    private String figures;
    private String dateCreated;
    private Timestamp dateModify;

    public DrawingDTO() {

    }

    public DrawingDTO(int id, int numFigures, int idUser, boolean visualization, String name, String username,
                      String figures, String dateCreated, Timestamp dateModify) {
        this.id = id;
        this.numFigures = numFigures;
        this.idUser = idUser;
        this.visualization = visualization;
        this.name = name;
        this.username = username;
        this.figures = figures;
        this.dateCreated = dateCreated;
        this.dateModify = dateModify;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumFigures() {
        return numFigures;
    }

    public void setNumFigures(int numFigures) {
        this.numFigures = numFigures;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public boolean isVisualization() {
        return visualization;
    }

    public void setVisualization(boolean visualization) {
        this.visualization = visualization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFigures() {
        return figures;
    }

    public void setFigures(String figures) {
        this.figures = figures;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateModify() {
        return dateModify;
    }

    public void setDateModify(Timestamp dateModify) {
        this.dateModify = dateModify;
    }
}
