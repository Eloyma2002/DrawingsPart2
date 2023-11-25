package com.esliceu.Drawings.Services;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Repositories.DrawingDAO;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DrawingServices {

    @Autowired
    DrawingDAO drawingDAO;

    // Mètode per desar un dibuix
    public boolean save(String name, User user, String json) {
        Drawing drawing = new Drawing(name, user, json);

        try {
            // Intentar parsejar el JSON i obtenir el nombre de figures
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(json);
            drawing.setNumFigures(jsonArray.size());
            if (drawing.getNumFigures() == 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        // Desar el dibuix a la base de dades
        drawingDAO.save(drawing);
        return true;
    }

    // Mètode per carregar tots els dibuixos
    public List<Drawing> loadAll() {
        return drawingDAO.loadAllLists();
    }

    // Mètode per carregar la llista de dibuixos d'un usuari específic
    public List<Drawing> loadMyList(User user) {
        return drawingDAO.loadMyList(user);
    }

    // Mètode per eliminar un dibuix
    public boolean delete(int id, User user) {
        return drawingDAO.deleteDrawing(id, user);
    }

    // Mètode per obtenir un dibuix pel seu ID
    public Drawing getDrawing(int id) {
        return drawingDAO.getDrawing(id);
    }

    // Mètode per modificar un dibuix ja creat anteriorment
    public boolean modify(int id, String figures, String newName, User user, Drawing drawing) {
        JSONParser parser = new JSONParser();

        try {
            // Intentar parsejar el JSON i obtenir el nombre de figures
            JSONArray jsonArray = (JSONArray) parser.parse(figures);
            if (Objects.equals(user.getUsername(), drawing.getUser().getUsername())) {
                drawingDAO.modifyFigures(id, figures, newName, jsonArray.size(), user);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
