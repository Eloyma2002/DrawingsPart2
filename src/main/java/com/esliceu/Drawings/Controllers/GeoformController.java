package com.esliceu.Drawings.Controllers;


import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Services.DrawingServices;
import com.esliceu.Drawings.Services.UserServices;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.checkerframework.checker.units.qual.A;
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
    DrawingServices drawingServices;


    // Gestionar les sol·licituds POST en enviar el formulari de registre
    @PostMapping("/geoform")
    public String postGeoform(Model model, @RequestParam String json, @RequestParam String name, HttpServletRequest req){

        // Obtenir l'usuari de la sessió actual
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // Guardar el dibuix a la base de dades
        if (drawingServices.save(name, user, json)) {
            model.addAttribute("confirmation", "Your drawing is saved");
            return "confirmation";
        } else {
            model.addAttribute("error", "You cannot save a drawing without content");
            return "error";
        }
    }

    @GetMapping("/geoform")
    public String getGeoform() {
        return "geoform";
    }
}

