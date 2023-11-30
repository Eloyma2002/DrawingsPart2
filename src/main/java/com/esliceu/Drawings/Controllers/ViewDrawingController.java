package com.esliceu.Drawings.Controllers;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Services.DrawingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


// Mapeig del servlet per a la pàgina de la meva llista
@Controller
public class ViewDrawingController {

    @Autowired
    // Serveis per gestionar els dibuixos
    DrawingServices drawingServices;

    @GetMapping("/viewDrawing")
    public String getViewDrawing(Model model, @RequestParam int drawingId) {
        // Obtindre l'ID del dibuix a visualitzar des del formulari

        // Obtindre el dibuix des del servei de dibuixos
        Drawing drawing = drawingServices.getDrawing(drawingId);

        if (!drawing.getView() && )

        // Configurar l'atribut en la sol·licitud amb el JSON del dibuix
        model.addAttribute("json", drawing.getFigures());
        return "viewDrawing";
    }

    @PostMapping("/viewDrawing")
    public String postViewDrawing() {
        return "viewDrawing";
    }
}

