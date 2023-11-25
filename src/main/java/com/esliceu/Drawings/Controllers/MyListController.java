package com.esliceu.Drawings.Controllers;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Services.DrawingServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

// Mapeig del servlet per a la pàgina de la meva llista
@Controller
public class MyListController {

    @Autowired
    // Serveis per gestionar els dibuixos
    DrawingServices drawingServices;
    // Obté la sessió actual
    HttpSession httpSession;

    @GetMapping("/myList")
    public String getMyList(Model model) {
        // Obté l'usuari actual des de la sessió
        User user = (User) httpSession.getAttribute("user");

        // Carregar la llista de dibuixos específics de l'usuari
        List<Drawing> drawings = drawingServices.loadMyList(user);

        // Configurar l'atribut a la sol·licitud amb la llista de dibuixos de l'usuari
        model.addAttribute("drawings", drawings);

        return "myList";
    }

    @PostMapping("/myList")
    public String postMyList() {

        return "myList";
    }
}

