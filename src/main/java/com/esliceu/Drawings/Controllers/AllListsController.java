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
public class AllListsController {

    @Autowired
    // Serveis per gestionar els dibuixos
    DrawingServices drawingServices;

    @Autowired
    // Servei per gestionar els dibuixos DTO
    DrawingDTOServices drawingDTOServices;

    @GetMapping("/allLists")
    public String getAllLists(Model model, HttpServletRequest req) {

        // Obtenim la sessió actual
        HttpSession session = req.getSession();

        // Obté l'usuari actual des de la sessió
        User user = (User) session.getAttribute("user");

        // Carregar la llista de tots els dibuixos
        List<Drawing> allDrawings = drawingServices.loadAll(user);

        if (allDrawings == null) {
            // Gestionar l'excepció per si hi ha un dibuix que no hi hauria d'estar a aquesta llista
            model.addAttribute("error", "An error occurred while trying to obtain the list of drawings");
            return "error";
        }

        // Transformam la llista anterior al objecte DrawingDTO per mostrar a l'usuari només el que volem
        List<DrawingDTO> drawingDTOS = drawingDTOServices.transformListDrawingToDrawingDTO(allDrawings);

        // Configurar l'atribut a la sol·licitud amb la llista de tots els dibuixos
        model.addAttribute("drawings", drawingDTOS);
        return "allLists";
    }

    @PostMapping("/allLists")
    public String postAllLists(Model model, @RequestParam int drawingId, HttpServletRequest req) {
        // Obté l'ID del dibuix a eliminar des del formulari

        // Obtenim la sessió actual
        HttpSession session = req.getSession();

        // Obté l'usuari actual des de la sessió
        User user = (User) session.getAttribute("user");

        // Obtenim el dibuix mitjançant l'Id
        Drawing drawing = drawingServices.getDrawing(drawingId);

        if (drawingServices.delete(drawing, user)) {
            // Indiquem que l'usuari ha esborrat correctament el dibuix
            model.addAttribute("confirmation", "Your drawing has been moved to your trash");
            return "confirmation";
        }

        // Gestionar l'excepció per si intenten esborrar un dibuix que no es seu
        model.addAttribute("error", "You cant delete this drawing, is not yours");
        return "error";
    }
}

