package com.esliceu.Drawings.Services;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Entities.Version;
import com.esliceu.Drawings.Exceptions.*;
import com.esliceu.Drawings.Repositories.DrawingREPO;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class DrawingServices {

    @Autowired
    DrawingREPO drawingREPO;

    @Autowired
    VersionServices versionServices;

    // Mètode per desar un dibuix
    public boolean save(String name, User user, String json, boolean view) throws DrawingWithoutContentException, ParseException {
        Drawing drawing = new Drawing(name, user, view);
        drawing.setIdUser(user.getId());
        Version version = new Version(0, 0, 0, json, Timestamp.from(Instant.now()));

            // Assignam si el dibuix es públic o privat
            drawing.setView(view);

            // Intentar parsejar el JSON i obtenir el nombre de figures
            int numFigures = getNumFigures(json);
            version.setNumFigures(numFigures);
            if (version.getNumFigures() == 0) {
                throw new DrawingWithoutContentException();
            }

        // Desar el dibuix a la base de dades
        drawingREPO.save(drawing, user, version);
        return true;
    }

    public int getNumFigures(String json) throws ParseException, DrawingWithoutContentException{
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(json);
        if (jsonArray.size() == 0) {
            throw new DrawingWithoutContentException();
        }
        return jsonArray.size();
    }

    // Mètode per carregar tots els dibuixos
    public List<Drawing> loadAll(User user) throws ErrorWithListException {

            // Carrega només els teus dibuixos i els públics s'altres usuaris
            List<Drawing> allDrawings = drawingREPO.loadAllLists(user);

            // Assignam a cada dibuix el seu usuari mitjançant l'id del mateix
            for (Drawing drawing : allDrawings) {
                drawing.setUser(drawingREPO.getUserById(drawing.getIdUser()));
            }

            for (Drawing drawing : allDrawings) {
                if (drawing.isTrash() || user.getId() != drawing.getIdUser() && !drawing.isTrash()) {
                    throw new ErrorWithListException();
                }
            }

            if (allDrawings == null) {
                // Gestionar l'excepció per si hi ha un dibuix que no hi hauria d'estar a aquesta llista
                throw new ErrorWithListException();
            }
            return allDrawings;
    }

    // Mètode per carregar la llista de dibuixos d'un usuari específic
    public List<Drawing> loadMyTrash(User user) {
        return drawingREPO.loadMyTrash(user);
    }

    // Mètode per assignar un dibuix a la paperera
    public boolean delete(Drawing drawing, User user) throws YouCantDeleteThisDrawingException {

        if (drawing.getIdUser() != user.getId()) {
            throw new YouCantDeleteThisDrawingException();
        }

        return drawingREPO.deleteDrawing(drawing);
    }

    public void changeDrawingName(Drawing drawing, String name) {
        drawingREPO.changeDrawingName(drawing, name);
    }

    // Mètode per obtenir un dibuix pel seu ID
    public Drawing getDrawing(int id) {
        Drawing drawing = drawingREPO.getDrawing(id);
        drawing.setUser(drawingREPO.getUserById(drawing.getIdUser()));
        drawing.setVersionList(versionServices.getAllVersionsOfADrawing(drawing));
        return drawing;
    }

    // Mètode per modificar un dibuix ja creat anteriorment
    public boolean addDrawingVersion(int idDrawing, String figures, String newName, User user, Drawing drawing) {
        JSONParser parser = new JSONParser();

        try {
            // Intentar parsejar el JSON i obtenir el nombre de figures
            JSONArray jsonArray = (JSONArray) parser.parse(figures);
            Version version = new Version(0, idDrawing, jsonArray.size(), figures, Timestamp.from(Instant.now()));
            if (Objects.equals(user.getUsername(), drawing.getUser().getUsername())) {
                drawingREPO.addVersion(version);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean deleteTrash(int drawingId) {
        return drawingREPO.deleteTrash(getDrawing(drawingId));
    }

    // Recupera el dibuix de la paperera
    public boolean recoverDrawingFromTrash(int drawingId, User user) throws YouCantRecoverThisDrawingException {

        // Obté el dibuix mitjançant l'id
        Drawing drawing = drawingREPO.getDrawing(drawingId);

        // Controlam si el dibuix que intentam recuperar es nostre
        if (drawing.getIdUser() == user.getId()) {
            return drawingREPO.recoverDrawingFromTrash(drawingId);
        }

        throw new YouCantRecoverThisDrawingException();
    }

    public boolean confirmViewType(String viewType) throws NonCorrectViewTypeException {

        if (viewType.equals("1")) {
            return true;
        } else if (viewType.equals("0")) {
            return false;
        } else {
            throw new NonCorrectViewTypeException();
        }
    }

    public void confirmIfDrawingIsPublic(Drawing drawing, User user) throws YouCantAccessToThisDrawingException{
        if (!drawing.getView() && drawing.getIdUser() != user.getId()) {
            throw new YouCantAccessToThisDrawingException();
        }
    }

    public boolean confirmDrawingChanges(Version version, Drawing drawing, String json, String name) {

        if (version.getFigures().equals(json) && drawing.getName().equals(name)) {
            throw new DrawingWithoutChangesException();
        } else if (version.getFigures().equals(json) && !Objects.equals(drawing.getName(), name)) {
            changeDrawingName(drawing, name);
            return true;
        }
        return false;
    }
}
