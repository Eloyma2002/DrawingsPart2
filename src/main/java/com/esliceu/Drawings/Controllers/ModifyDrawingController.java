package com.esliceu.Drawings.Controllers;

import com.esliceu.Drawings.DTO.DrawingDTO;
import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Entities.Version;
import com.esliceu.Drawings.Services.DrawingDTOServices;
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



// Mapeig del servlet per a la pàgina de la meva llista
@Controller
public class ModifyDrawingController {

    @Autowired
    // Serveis per gestionar els dibuixos
    DrawingServices drawingServices;

    @Autowired
    // Servei per gestionar les versions
    VersionServices versionServices;

    @Autowired
    // Servei per gestionar els dibuixosDTO
    DrawingDTOServices drawingDTOServices;

    @GetMapping("/modifyDrawing")
    public String getModifyDrawing(Model model, HttpServletRequest req, @RequestParam int drawingId) {
        // Obtindre l'ID del dibuix a visualitzar des del formulari

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        try {
            DrawingDTO drawingDTO = drawingDTOServices.getDTODrawing(drawingId, user);

            // Configurar atributs en la sol·licitud per a la pàgina HTML
            model.addAttribute("json", drawingDTO.getFigures());
            model.addAttribute("drawingId", drawingDTO.getId());
            model.addAttribute("name", drawingDTO.getName());
            model.addAttribute("viewType", drawingDTO.isVisualization());

            return "modifyDrawing";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/modifyDrawing")
    public String postModifyDrawing(Model model, HttpServletRequest req,
                                    @RequestParam String drawingId, @RequestParam String name,
                                    @RequestParam String json,
                                    @RequestParam String viewType) {


        try {

            int id = Integer.parseInt(drawingId);

            // Obtenir l'usuari actual des de la sessió
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            // Obtenir el dibuix mitjançant l'id
            Drawing drawing = drawingServices.getDrawing(id);

            // Obtenir l'ultima versió del dibuix
            Version version = versionServices.getLastVersion(id);

            drawingServices.getNumFigures(json);

            if (drawingServices.confirmDrawingChanges(version, drawing, json, name)){
                model.addAttribute("confirmation", "The drawing name has been changed");
                return "confirmation";
            }

            if (drawingServices.addDrawingVersion(id, json, user, drawing)) {
                model.addAttribute("confirmation", "Your drawing has been modified");
                return "confirmation";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "modifyDrawing";
    }
}