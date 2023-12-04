package com.esliceu.Drawings.Services;

import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Exceptions.PasswordNotValidException;
import com.esliceu.Drawings.Exceptions.UserDoesntExistException;
import com.esliceu.Drawings.Exceptions.UserExistException;

import com.esliceu.Drawings.Repositories.UserREPO;
import com.google.common.hash.Hashing;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServices {

    @Autowired
    UserREPO userREPO;

    public void register(String username, String password, String nameAndLastname) throws UserExistException {
        // Hash de la contrasenya abans d'emmagatzemar-la a la llista d'usuaris
        User user = new User(username, nameAndLastname, Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString(), 0);

        // Obtenir el DAO d'usuari i inserir el nou usuari
        userREPO.saveUser(user);
    }

    public User login(String userName, String password, HttpServletRequest req) throws UserDoesntExistException,
                                                                                       PasswordNotValidException {

        // Autenticar l'usuari
        if (password.length() < 5) {
            throw new PasswordNotValidException();
        }
        String hash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();

        User user = userREPO.getUser(userName, hash);

        // Si l'usuari existeix, crear una sessió i redirigir a la pàgina de geoform
        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
        } else {
            throw new UserDoesntExistException();
        }

        return user;
    }

    public void confirmPassword(String password) {
        // Validar la contrasenya utilitzant una expressió regular
        String regex = "^(?!.*\\s).{5,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        // Verificar si la contrasenya és vàlida; si no ho és, llançar una excepció
        if (!matcher.matches()) {
            // Gestionar l'excepció configurant un atribut d'error i redirigint a la pàgina de registre
            throw new PasswordNotValidException();
        }
    }
}
