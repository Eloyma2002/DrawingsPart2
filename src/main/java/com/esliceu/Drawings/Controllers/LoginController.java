package com.esliceu.Drawings.Controllers;


import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

// Mapeig del servlet per a la pàgina de login
@Controller
public class LoginController {

    @Autowired
    // Inicialitzar UserServices per interactuar amb funcionalitats relacionades amb l'usuari
    UserServices userServices;


    // Gestionar les sol·licituds POST en enviar el formulari de registre
    @PostMapping("/login")
    public String postRegister(Model model, @RequestParam String username, @RequestParam String password ,
                               HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Intentar realitzar l'inici de sessió i obtenir l'objecte User corresponent
        User user = userServices.login(username, password);

        // Si l'usuari existeix, crear una sessió i redirigir a la pàgina de geoform
        if (user != null) {
            resp.sendRedirect("/geoform");
        } else {
            // Manejar la excepció d'usuari no trobat configurant un missatge d'error i redirigint a la pàgina d'inici de sessió
            req.setAttribute("error", "User not found");
        }
        return "login";
    }

    @GetMapping("/login")
    public String getRegister() {
        return "login";
    }
}

