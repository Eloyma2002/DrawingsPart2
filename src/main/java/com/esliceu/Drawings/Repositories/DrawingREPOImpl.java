package com.esliceu.Drawings.Repositories;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Entities.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class DrawingREPOImpl implements DrawingREPO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void save(Drawing drawing, User user, Version version) {

        String sql = "INSERT INTO drawing (name, idUser, view,  date, trash) VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1,drawing.getName());
            ps.setInt(2,drawing.getIdUser());
            ps.setBoolean(3, drawing.isView());
            ps.setTimestamp(4,drawing.getDate());
            ps.setBoolean(5,drawing.isTrash());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) drawing.setId(key.intValue());
        version.setIdDrawing(drawing.getId());
        addVersion(version);
    }

    @Override
    public List<Drawing> loadAllLists(User user) {
        // Tornar la llista completa de dibuixos
        return jdbcTemplate.query("SELECT id, name, idUser, view, date, trash " +
                                                          "FROM drawing " +
                                                          "WHERE (idUser = ? AND trash = 0) " +
                                                          "OR " +
                                                          "(view = 1 AND trash = 0)",
                                                          new BeanPropertyRowMapper<>(Drawing.class), user.getId());
    }

    @Override
    public List<Drawing> loadMyTrash(User user) {
        // Filtrar i tornar la llista de dibuixos pertanyents a un usuari específic

        List<Drawing> myDrawings = jdbcTemplate.query("SELECT id, name, idUser, view, date, trash " +
                        "FROM drawing " +
                        "WHERE " +
                        "idUser = ? AND trash = 1;",
                new BeanPropertyRowMapper<>(Drawing.class), user.getId());

        for (Drawing drawing : myDrawings) {
            drawing.setUser(user);
        }

        return myDrawings;
    }

    @Override
    public boolean deleteDrawing(Drawing drawing) {


        int rowsUpdated = jdbcTemplate.update("UPDATE drawing SET trash = 1 WHERE id = ?;", drawing.getId());

        // Eliminar un dibuix si coincideix amb la ID i l'id de l'usuari proporcionats
        drawing.setTrash(true);

        // Si rowsUpdated es major que 0, significa que hi ha una filera actualitzada
        return rowsUpdated > 0;
    }

    @Override
    public boolean deleteTrash(Drawing drawing) {
        // Eliminar un dibuix de la paperera si coincideix amb la ID i l'id de l'usuari proporcionats

        int versionDeleted = jdbcTemplate.update("DELETE FROM version WHERE idDrawing = ?",
                                                      drawing.getId());

        int drawingDeleted = jdbcTemplate.update("DELETE FROM drawing WHERE id = ? AND idUser = ?",
                                                      drawing.getId(), drawing.getIdUser());

        // Si drawingDeletes es major que 0, significa que hi ha una filera eliminada, el mateix passa amb versionDeleted
        return drawingDeleted > 0 && versionDeleted > 0;
    }

    @Override
    public Drawing getDrawing(int id) {
        // Buscar i tornar un dibuix per la seva ID

        return jdbcTemplate.queryForObject("SELECT * FROM drawing WHERE id = ?",
                new BeanPropertyRowMapper<>(Drawing.class), id);
    }

    @Override
    public User getUserById(int idUser) {
            return jdbcTemplate.queryForObject
                    ("SELECT * FROM `user` WHERE `id` = ?",
                            new BeanPropertyRowMapper<>(User.class), idUser);
    }

    @Override
    public void addVersion(Version version) {
        // Modificar les figures, aficam una nova versió
        jdbcTemplate.update("INSERT INTO `version` (`figures`, `idDrawing`, `dateModify`, `numFigures`) " +
                                "VALUES (?, ?, ?, ?);",
                                version.getFigures(), version.getIdDrawing(), version.getDateModify(),
                                version.getNumFigures());
    }

    @Override
    public void changeDrawingName(Drawing drawing, String name) {
        // Cambiam el nom del dibuix
        jdbcTemplate.update("UPDATE drawing SET name = ? WHERE id = ?;", name, drawing.getId());
    }

    @Override
    public boolean recoverDrawingFromTrash(int drawingId) {
        // Recupera el dibuix de la paperera
        int drawingRecover = jdbcTemplate.update("UPDATE drawing SET trash = 0 WHERE id = ?;", drawingId);

        // Si drawingRecover es major que 0, significa que hi ha un dibuix recuperat de la paperera
        return drawingRecover > 0;
    }
}
