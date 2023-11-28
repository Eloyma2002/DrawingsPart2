package com.esliceu.Drawings.Repositories;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class DrawingDAOImpl implements DrawingDAO {

    // Llista que emmagatzema els dibuixos com a base de dades (cada un enllaçat amb el seu usuari corresponent)
    static List<Drawing> drawings = new ArrayList<>();

    // Variable per assignar IDs úniques als dibuixos
    static int id = 0;

    @Override
    public boolean save(Drawing drawing) {
        // Assignar una ID única al dibuix i afegir-lo a la llista
        drawing.setId(id);
        drawings.add(drawing);
        // Incrementar la ID per al pròxim dibuix
        id++;
        return true;
    }

    @Override
    public List<Drawing> loadAllLists() {
        // Tornar la llista completa de dibuixos
        return drawings;
    }

    @Override
    public List<Drawing> loadMyList(User user) {
        // Filtrar i tornar la llista de dibuixos pertanyents a un usuari específic
        List<Drawing> myList = new ArrayList<>();
        for (Drawing drawing : drawings) {
            if (drawing.getUser().getUsername().equals(user.getUsername())) {
                myList.add(drawing);
            }
        }
        return myList;
    }

    @Override
    public boolean deleteDrawing(int id, User user) {
        // Eliminar un dibuix si coincideix amb la ID i l'usuari proporcionats
        return drawings.removeIf(drawing -> Objects.equals(drawing.getUser().getUsername(), user.getUsername()) && drawing.getId() == id);
    }

    @Override
    public Drawing getDrawing(int id) {
        // Buscar i tornar un dibuix per la seva ID
        for (Drawing drawing : drawings) {
            if (drawing.getId() == id) {
                return drawing;
            }
        }
        return null;
    }

    @Override
    public boolean modifyFigures(int id, String figures, String newName, int size, User user) {
        // Modificar les figures, nom i mida d'un dibuix per la seva ID
        for (Drawing drawing : drawings) {
            if (drawing.getId() == id && Objects.equals(drawing.getUser().getUsername(), user.getUsername())) {
                drawing.setFigures(figures);
                drawing.setName(newName);
                drawing.setNumFigures(size);
                return true;
            }
        }
        return false;
    }
}
