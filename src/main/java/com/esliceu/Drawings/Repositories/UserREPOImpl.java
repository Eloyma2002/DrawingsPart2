package com.esliceu.Drawings.Repositories;

import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Exceptions.UserDoesntExist;
import com.esliceu.Drawings.Exceptions.UserExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserREPOImpl implements UserREPO {
    @Autowired
    JdbcTemplate jdbcTemplate;

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
        System.out.println("DENTRO");

        User user = jdbcTemplate.queryForObject
                    ("SELECT * FROM `user` WHERE `username` = ? AND `password` = ?;",
                            new BeanPropertyRowMapper<>(User.class), username, password);

        System.out.println("USER: " + user);
        return user;
    }
}
