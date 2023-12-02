package com.esliceu.Drawings.Controllers;

import com.esliceu.Drawings.DTO.DrawingDTO;
import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Services.DrawingDTOServices;
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
public class RecoverController {

    @Autowired
    // Serveis per gestionar els dibuixos
    DrawingServices drawingServices;

    @Autowired
    // Serveis per obtenir el dibuixos en DTO
    DrawingDTOServices drawingDTOServices;

    @GetMapping("/recover")
    public String getMyList() {
        return "recover";
    }

    @PostMapping("/recover")
    public String postMyList(Model model, HttpServletRequest req, @RequestParam int drawingId) {
        // Obté l'ID del dibuix a eliminar des del formulari

        // Obté l'usuari de la sessió
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (drawingServices.recoverDrawingFromTrash(drawingId, user)) {
            // Indiquem que l'usuari ha esborrat correctament el dibuix
            model.addAttribute("confirmation", "Your drawing has been recovered");
            return "confirmation";
        }

        // Gestionar l'excepció per a un dibuix no existent configurant un atribut d'error i redirigint a la pàgina de registre
        model.addAttribute("error", "You cant recover this drawing, is not yours");
        return "error";
    }
}

