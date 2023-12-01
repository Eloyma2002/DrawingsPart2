package com.esliceu.Drawings.Controllers;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Services.DrawingServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// Mapeig del servlet per a la pàgina de la meva llista
@Controller
public class TrashController {

    @Autowired
    // Serveis per gestionar els dibuixos
    DrawingServices drawingServices;

    @GetMapping("/trash")
    public String getMyList(Model model, HttpServletRequest req) {

        // Obtenir l'usuari de la sessió actual
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // Carregar la llista de dibuixos específics de l'usuari
        List<Drawing> drawings = drawingServices.loadMyTrash(user);

        // Configurar l'atribut a la sol·licitud amb la llista de dibuixos de l'usuari
        model.addAttribute("drawings", drawings);
        return "trash";
    }

    @PostMapping("/trash")
    public String postMyList(Model model, @RequestParam int drawingId, HttpServletRequest req) {
        // Obté l'ID del dibuix a eliminar des del formulari

        // Obtenim la sessió actual
        HttpSession session = req.getSession();

        // Obté l'usuari actual des de la sessió
        User user = (User) session.getAttribute("user");

        if (drawingServices.deleteTrash(drawingId, user)) {
            // Indiquem que l'usuari ha esborrat correctament el dibuix
            model.addAttribute("confirmation", "Your drawing is deleted");
            return "confirmation";
        }

        // Gestionar l'excepció per a un dibuix no existent configurant un atribut d'error i redirigint a la pàgina de registre
        model.addAttribute("error", "You cant delete this drawing, is not yours");
        return "error";
    }
}

