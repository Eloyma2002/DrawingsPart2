package com.esliceu.Drawings.Services;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Entities.Version;
import com.esliceu.Drawings.Repositories.DrawingREPO;
import com.esliceu.Drawings.Repositories.VersionREPO;
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
    public boolean save(String name, User user, String json, boolean view) {
        Drawing drawing = new Drawing(name, user, view);
        drawing.setIdUser(user.getId());
        Version version = new Version(0, 0, 0, json, Timestamp.from(Instant.now()));

        try {
            // Assignam si el dibuix es públic o privat
            drawing.setView(view);

            // Intentar parsejar el JSON i obtenir el nombre de figures
            int numFigures = getNumFigures(json);
            version.setNumFigures(numFigures);
            if (version.getNumFigures() == 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        // Desar el dibuix a la base de dades
        drawingREPO.save(drawing, user, version);
        return true;
    }

    public int getNumFigures(String json) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(json);
        return jsonArray.size();
    }

    // Mètode per carregar tots els dibuixos
    public List<Drawing> loadAll(User user) {

        // Carrega només els teus dibuixos i els públics s'altres usuaris
        List<Drawing> allDrawings = drawingREPO.loadAllLists(user);

        for (Drawing drawing : allDrawings) {
            if (drawing.isTrash() || user.getId() != drawing.getIdUser() && !drawing.isTrash()) {
                return null;
            }
        }
        return allDrawings;
    }

    // Mètode per carregar la llista de dibuixos d'un usuari específic
    public List<Drawing> loadMyTrash(User user) {
        return drawingREPO.loadMyTrash(user);
    }

    // Mètode per assignar un dibuix a la paperera
    public boolean delete(Drawing drawing, User user) {

        if (drawing.getIdUser() != user.getId()) {
            return false;
        }

        return drawingREPO.deleteDrawing(drawing);
    }

    public void changeDrawingName(Drawing drawing, String name) {
        drawingREPO.changeDrawingName(drawing, name);
    }

    // Mètode per obtenir un dibuix pel seu ID
    public Drawing getDrawing(int id) {
        Drawing drawing = drawingREPO.getDrawing(id);
        drawing.setVersionList(versionServices.getAllVersionsOfADrawing(drawing));
        return drawing;
    }

    // Mètode per modificar un dibuix ja creat anteriorment
    public boolean modify(int idDrawing, String figures, String newName, User user, Drawing drawing) {
        JSONParser parser = new JSONParser();

        try {
            // Intentar parsejar el JSON i obtenir el nombre de figures
            JSONArray jsonArray = (JSONArray) parser.parse(figures);
            Version version = new Version(0, idDrawing, jsonArray.size(), figures, Timestamp.from(Instant.now()));
            if (Objects.equals(user.getUsername(), drawing.getUser().getUsername())) {
                drawingREPO.modifyFigures(version);
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
    public boolean recoverDrawingFromTrash(int drawingId, User user) {

        // Obté el dibuix mitjançant l'id
        Drawing drawing = drawingREPO.getDrawing(drawingId);

        // Controlam si el dibuix que intentam recuperar es nostre
        if (drawing.getIdUser() == user.getId()) {
            return drawingREPO.recoverDrawingFromTrash(drawingId);
        }

        return false;
    }
}
