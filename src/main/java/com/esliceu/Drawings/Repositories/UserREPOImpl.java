package com.esliceu.Drawings.Repositories;

import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Exceptions.UserDoesntExist;
import com.esliceu.Drawings.Exceptions.UserExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserREPOImpl implements UserREPO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // Llista que emmagatzema els usuaris registrats com a base de dades
    static List<User> users = new ArrayList<>();

    @Override
    public void saveUser(User user) throws UserExist {

        // Verificar si el usuario ya existe en la base de datos
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM `user` WHERE `username` = ?",
                Integer.class, user.getUsername());

        // Si el conteo es mayor que 0, significa que ya hay un usuario con ese nombre de usuario
        if (count > 0) {
            throw new UserExist();
        }

        // Si no hay usuarios con ese nombre de usuario, proceder con la inserción
        jdbcTemplate.update("INSERT INTO `user` (`username`, `nameAndLastname`, `password`) VALUES (?, ?, ?);",
                user.getUsername(), user.getNameAndLastname(), user.getPassword());
    }


    @Override
    public User getUser(String username, String password) throws UserDoesntExist {
        String nameAndLastname = jdbcTemplate.queryForObject
                ("SELECT nameAndLastname FROM `user` WHERE `username` = ? AND `password` = ?;",
                        String.class, username, password);
        User user = new User(username, nameAndLastname, password);

        return user;
    }
}