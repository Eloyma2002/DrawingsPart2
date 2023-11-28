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
        // Verificar si l'usuari ja existeix abans de desar-lo
        jdbcTemplate.update("INSERT INTO `user` (`username`, `nameAndLastname`, `password`) VALUES (?, ?, ?);",
                                                user.getUsername(), user.getNameAndLastname(), user.getPassword());
        for (User existingUser : users) {
            if (existingUser.getUsername().equalsIgnoreCase(user.getUsername())) {
                throw new UserExist();
            }
        }
        // Afegir el nou usuari a la llista
        users.add(user);
    }

    @Override
    public User getUser(String userName, String password) throws UserDoesntExist {
        // Buscar un usuari per nom d'usuari i contrasenya
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(userName) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
