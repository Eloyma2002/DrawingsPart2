package com.esliceu.Drawings.Controllers;


import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Services.DrawingServices;
import com.esliceu.Drawings.Services.UserServices;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
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
public class GeoformController {

    @Autowired
    // Inicialitzar UserServices per interactuar amb funcionalitats relacionades amb l'usuari
    UserServices userServices;
    HttpSession httpSession;
    DrawingServices drawingServices;


    // Gestionar les sol·licituds POST en enviar el formulari de registre
    @PostMapping("/geoform")
    public String postRegister(Model model, @RequestParam String json, @RequestParam String name ,
                               HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        // Obtenir l'usuari de la sessió actual
        User user = (User) httpSession.getAttribute("user");

        // Guardar el dibuix a la base de dades
        if (drawingServices.save(name, user, json)) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/templates/confirmationSave.html");
            model.addAttribute("confirmation", "Your drawing is saved");
            dispatcher.forward(req, resp);
        } else {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/templates/error.html");
            model.addAttribute("error", "You cannot save a drawing without content");
            dispatcher.forward(req, resp);
        }
        return "geoform";
    }

    @GetMapping("/geoform")
    public String getRegister() {
        return "geoform";
    }
}

