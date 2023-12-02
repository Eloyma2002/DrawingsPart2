package com.esliceu.Drawings.Controllers;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Entities.Version;
import com.esliceu.Drawings.Services.DrawingServices;
import com.esliceu.Drawings.Services.VersionServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;


// Mapeig del servlet per a la pàgina de la meva llista
@Controller
public class ModifyDrawingController {

    @Autowired
    // Serveis per gestionar els dibuixos
    DrawingServices drawingServices;

    @Autowired
    // Servei per gestionar les versions
    VersionServices versionServices;

    @GetMapping("/modifyDrawing")
    public String getModifyDrawing(Model model, HttpServletRequest req, @RequestParam int drawingId) {
        // Obtindre l'ID del dibuix a visualitzar des del formulari

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // Obtindre el dibuix des del servei de dibuixos
        Drawing drawing = drawingServices.getDrawing(drawingId);

        // Obtenir la versió més recient del dibuix
        Version version = versionServices.getLastVersion(drawingId);

        // Si intenten modificar el dibuix desde la paperera no es podrà fer
        if (drawing.isTrash()) {
            model.addAttribute("error", "You cannot modify a drawing that is in the trash");
            return "error";
        }

        // Comprovar si el dibuix es del mateix usuari que l'intenta modificar
        if (!Objects.equals(user.getUsername(), drawing.getUser().getUsername())) {
                model.addAttribute("error", "You cannot modify a drawing that is not yours");
                return "error";
        }

        // Configurar atributs en la sol·licitud per a la pàgina HTML
        model.addAttribute("json", version.getFigures());
        model.addAttribute("drawingId", drawing.getId());
        model.addAttribute("name", drawing.getName());
        return "modifyDrawing";
    }

    @PostMapping("/modifyDrawing")
    public String postModifyDrawing(Model model, HttpServletRequest req,
                                    @RequestParam int drawingId, @RequestParam String name,
                                    @RequestParam String json) throws ParseException {

        // Obtenir l'usuari actual des de la sessió
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // Obtenir el dibuix mitjançant l'id
        Drawing drawing = drawingServices.getDrawing(drawingId);

        // Obtenir l'ultima versió del dibuix
        Version version = versionServices.getLastVersion(drawingId);

        if (drawingServices.getNumFigures(json) == 0) {
            model.addAttribute("error", "You cannot save a drawing without content");
            return "error";
        }

        if (version.getFigures().equals(json) && drawing.getName().equals(name)) {
            model.addAttribute("error", "You cannot modify a drawing without changes");
            return "error";
        } else if (version.getFigures().equals(json) && !Objects.equals(drawing.getName(), name)) {
            drawingServices.changeDrawingName(drawing, name);
            model.addAttribute("confirmation", "The drawing name has been changed");
            return "confirmation";
        }

        if (drawingServices.modify(drawingId, json, name, user, drawing)) {
            model.addAttribute("confirmation", "Your drawing has been modified");
            return "confirmation";
        }
        return "modifyDrawing";
    }
}

