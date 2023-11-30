package com.esliceu.Drawings.Repositories;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class DrawingREPOImpl implements DrawingREPO {

    // Llista que emmagatzema els dibuixos com a base de dades (cada un enllaçat amb el seu usuari corresponent)
    static List<Drawing> drawings = new ArrayList<>();

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public boolean save(Drawing drawing, User user) {
        // Assignar una ID única al dibuix i afegir-lo a la llista
        jdbcTemplate.update("INSERT INTO `drawing` (`name`, `figures`, `numFigures`, `date`, `idUser`) VALUES (?, ?, ?, ?, ?);",
                drawing.getName(), drawing.getFigures(), drawing.getNumFigures(), drawing.getDate(), user.getId());
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

        List<Drawing> myDrawings = jdbcTemplate.query("SELECT id, name, figures, numFigures, date " +
                                                          "FROM drawing WHERE idUser = ?",
                                                          new BeanPropertyRowMapper<>(Drawing.class), user.getId());

        for (Drawing drawing : myDrawings) {
           drawing.setUser(user);
        }

        return myDrawings;
    }

    @Override
    public boolean deleteDrawing(int id, User user) {
        // Eliminar un dibuix si coincideix amb la ID i l'id de l'usuari proporcionats

        int rowsDeleted = jdbcTemplate.update("DELETE FROM drawing WHERE id = ? AND idUser = ?", id, user.getId());

        // Si rowsDeletes es major que 0, significa que hi ha una filera eliminada
        return rowsDeleted > 0;
    }

    @Override
    public Drawing getDrawing(int id) {
        // Buscar i tornar un dibuix per la seva ID

        return jdbcTemplate.queryForObject("SELECT * FROM drawing WHERE id = ?",
                        new BeanPropertyRowMapper<>(Drawing.class), id);
    }

    @Override
    public boolean modifyFigures(int id, String figures, String newName, int size, User user) {
        // Modificar les figures, nom i mida d'un dibuix per la seva ID
        jdbcTemplate.update("UPDATE `drawing` SET `figures` = ?, `numFigures` = ?, `date` = ? " +
                                "WHERE `drawing`.`id` = ? AND `drawing`.`idUser` = ?;",
                                figures, size, LocalDate.now(), id, user.getId());
        return true;
    }
}
