package com.esliceu.Drawings.Controllers;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Entities.Version;
import com.esliceu.Drawings.Services.DrawingServices;
import com.esliceu.Drawings.Services.VersionServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.Instant;


// Mapeig del servlet per a la pàgina de la meva llista
@Controller
public class ViewDrawingController {

    @Autowired
    // Serveis per gestionar els dibuixos
    DrawingServices drawingServices;

    @Autowired
    // Serveis per gestionar les versions
    VersionServices versionServices;

    @GetMapping("/viewDrawing")
    public String getViewDrawing(Model model, HttpServletRequest req, @RequestParam int drawingId) {
        // Obtindre l'ID del dibuix a visualitzar des del formulari

        try {
            // Obtenir l'usuari de la sessió actual
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            // Obtindre el dibuix des del servei de dibuixos
            Drawing drawing = drawingServices.getDrawing(drawingId);
            Version version = versionServices.getLastVersion(drawingId);

            drawingServices.confirmIfDrawingIsPublic(drawing, user);

            // Configurar l'atribut en la sol·licitud amb el JSON del dibuix
            model.addAttribute("json", version.getFigures());
            model.addAttribute("versionList", drawing.getVersionList());
            model.addAttribute("name", drawing.getName());
            model.addAttribute("inTrash", drawing.isTrash());


        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "viewDrawing";
    }

    @PostMapping("/viewDrawing")
    public String postViewDrawing(HttpServletRequest req, Model model, @RequestParam String name,
                                  @RequestParam int versionId) {

        try {
            // Obtenir l'usuari de la sessió actual
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            name = "CopyOf-" + name;

            // Obtindre la versió mitjançant l'id
            Version version = versionServices.getVersionById(user, versionId);
            version.setDateModify(Timestamp.from(Instant.now()));

            // Copiam el dibuix a la base de dades
            drawingServices.copyDrawing(version, user, name);

            model.addAttribute("confirmation", "You copy this drawing");
            return "confirmation";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

