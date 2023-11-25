package com.esliceu.Drawings.Services;

import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Exceptions.UserDoesntExist;
import com.esliceu.Drawings.Exceptions.UserExist;

import com.esliceu.Drawings.Repositories.UserDAO;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class UserServices {

    @Autowired
    UserDAO userDAO;

    public void register(String username, String password, String nameAndLastname) throws UserExist {
        // Hash de la contrasenya abans d'emmagatzemar-la a la llista d'usuaris
        User user = new User(username, nameAndLastname, Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString());

        // Obtenir el DAO d'usuari i inserir el nou usuari
        userDAO.saveUser(user);
    }

    public User login(String userName, String password) throws UserDoesntExist {
        // Autenticar l'usuari
        if (password.length() < 5) {
            return null;
        }
        User user = userDAO.getUser(userName, Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString());
        return user;
    }
}
