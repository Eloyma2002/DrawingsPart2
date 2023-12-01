package com.esliceu.Drawings.Services;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Entities.Version;
import com.esliceu.Drawings.Repositories.DrawingREPO;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.Objects;

@Service
public class DrawingServices {

    @Autowired
    DrawingREPO drawingREPO;

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
    public List<Drawing> loadAll() {
        return drawingREPO.loadAllLists();
    }

    // Mètode per carregar la llista de dibuixos d'un usuari específic
    public List<Drawing> loadMyTrash(User user) {
        return drawingREPO.loadMyTrash(user);
    }

    // Mètode per eliminar un dibuix
    public boolean delete(Drawing drawing) {
        if (drawing.isTrash()) {
            return drawingREPO.deleteTrash(drawing);
        }
        return drawingREPO.deleteDrawing(drawing);
    }

    // Mètode per obtenir un dibuix pel seu ID
    public Drawing getDrawing(int id) {

        return drawingREPO.getDrawing(id);
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
}
