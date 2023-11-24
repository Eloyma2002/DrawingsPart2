package com.esliceu.Drawings.Services;

import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Exceptions.UserDoesntExist;
import com.esliceu.Drawings.Exceptions.UserExist;

import com.esliceu.Drawings.Repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServices {

    @Autowired
    UserDAO userDAO;

    public void register(String username, String password, String nameAndLastname) throws UserExist {
        User user = new User(username, nameAndLastname, password);

        // Hash de la contrasenya abans d'emmagatzemar-la a la llista d'usuaris
        user.setPassword(DigestUtils.md5Hex(user.getPassword()).toUpperCase());

        // Obtenir el DAO d'usuari i inserir el nou usuari
        userDAO.saveUser(user);
    }

    public User login(String userName, String password) throws UserDoesntExist {
        // Obtenir el DAO d'usuari i autenticar l'usuari
        if (password.length() < 5) {
            return null;
        }
        User user = userDAO.getUser(userName, DigestUtils.md5Hex(password).toUpperCase());
        return null;
    }
}
