package com.esliceu.Drawings.Repositories;

import com.esliceu.Drawings.Entities.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VersionREPOImpl implements VersionREPO {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public Version getLastVersion(int idDrawing) {

        return jdbcTemplate.queryForObject(
                "SELECT * FROM version WHERE idDrawing = ? ORDER BY id DESC LIMIT 1",
                new BeanPropertyRowMapper<>(Version.class), idDrawing
        );
    }

    @Override
    public List<Version> getAllVersion(int id) {
        return jdbcTemplate.query("SELECT * FROM version WHERE idDrawing = ? ORDER BY id DESC",
                new BeanPropertyRowMapper<>(Version.class), id);
    }
}
