package com.esliceu.Drawings.Controllers;

import com.esliceu.Drawings.DTO.DrawingDTO;
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

import java.util.ArrayList;
import java.util.List;

// Mapeig del servlet per a la pàgina de la meva llista
@Controller
public class AllListsController {

    @Autowired
    // Serveis per gestionar els dibuixos
    DrawingServices drawingServices;

    @Autowired
    // Servei per gestionar les versions
    VersionServices versionServices;

    @GetMapping("/allLists")
    public String getAllLists(Model model, HttpServletRequest req) {

        // Obtenim la sessió actual
        HttpSession session = req.getSession();

        // Obté l'usuari actual des de la sessió
        User user = (User) session.getAttribute("user");

        // Carregar la llista de tots els dibuixos
        List<Drawing> allDrawings = drawingServices.loadAll();

        // Carrega només els teus dibuixos i els públics s'altres usuaris
        List<Drawing> drawings = new ArrayList<>();
        for (Drawing drawing : allDrawings) {
            if (!drawing.isTrash()) {
                drawings.add(drawing);
                continue;
            }
            if (drawing.getIdUser() != user.getId() && drawing.getView()) {
                drawings.add(drawing);
                continue;
            }
            if (drawing.getIdUser() == user.getId()) {
                drawings.add(drawing);
            }
        }

        // Transformam la llista anterior al objecte DrawingDTO per mostrar a l'usuari només el que volem
        List<DrawingDTO> drawingDTOS = new ArrayList<>();
        for (Drawing drawing : drawings) {
            Version version = versionServices.getLastVersion(drawing.getId());
            DrawingDTO drawingDTO = new DrawingDTO(drawing.getId(), version.getNumFigures(), drawing.getIdUser(),
                                        drawing.isView(), drawing.getName(), drawing.getUser().getUsername(),
                                        version.getFigures(), drawing.getDate(), version.getDateModify());
            drawingDTOS.add(drawingDTO);
            System.out.println("DATE: " + drawingDTO.getDateCreated());
        }


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

        if (drawingServices.delete(drawing)) {
            // Indiquem que l'usuari ha esborrat correctament el dibuix
            model.addAttribute("confirmation", "Your drawing is deleted");
            return "confirmation";
        }

        // Gestionar l'excepció per a un dibuix no existent configurant un atribut d'error i redirigint a la pàgina de registre
        model.addAttribute("error", "You cant delete this drawing, is not yours");
        return "error";
    }
}

