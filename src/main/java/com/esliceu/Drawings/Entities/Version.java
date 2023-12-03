package com.esliceu.Drawings.Entities;

import java.sql.Time;
import java.sql.Timestamp;

public class Version {

    private int id;
    private int idDrawing;
    private int numFigures;
    private String figures;
    private Timestamp dateModify;

    public Version() {

    }

    public Version(int id, int idDrawing, int numFigures, String figures, Timestamp dateModify) {
        this.id = id;
        this.idDrawing = idDrawing;
        this.numFigures = numFigures;
        this.figures = figures;
        this.dateModify = dateModify;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDrawing() {
        return idDrawing;
    }

    public void setIdDrawing(int idDrawing) {
        this.idDrawing = idDrawing;
    }

    public int getNumFigures() {
        return numFigures;
    }

    public void setNumFigures(int numFigures) {
        this.numFigures = numFigures;
    }

    public String getFigures() {
        return figures;
    }

    public void setFigures(String figures) {
        this.figures = figures;
    }

    public Timestamp getDateModify() {
        return dateModify;
    }

    public void setDateModify(Timestamp dateModify) {
        this.dateModify = dateModify;
    }


}
