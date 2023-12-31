package com.esliceu.Drawings.Controllers;


import com.esliceu.Drawings.Services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

// Mapeig del servlet per a la pàgina de registre
@Controller
public class RegisterController {

    @Autowired
    // Inicialitzar UserServices per interactuar amb funcionalitats relacionades amb l'usuari
    UserServices userServices;


    // Gestionar les sol·licituds POST en enviar el formulari de registre
    @PostMapping("/register")
    public String postRegister(Model model, @RequestParam String nameAndLastname, @RequestParam String username, @RequestParam String password,
                               HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Obtindre les dades de l'usuari des del formulari de registre

        try {
            userServices.confirmPassword(password);
            // Intentar registrar l'usuari; si té èxit, redirigir a la pàgina d'inici de sessió
            userServices.register(username, password, nameAndLastname);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "register";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }
}

