package com.esliceu.Drawings.Services;

import com.esliceu.Drawings.DTO.DrawingDTO;
import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Entities.Version;
import com.esliceu.Drawings.Exceptions.YouCantModifyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DrawingDTOServices {
    @Autowired
    VersionServices versionServices;

    @Autowired
    DrawingServices drawingServices;

    // Transformam la llista anterior al objecte DrawingDTO per mostrar a l'usuari només el que volem
    public List<DrawingDTO> transformListDrawingToDrawingDTO (List<Drawing> drawings) {

        List<DrawingDTO> drawingsDTOS = new ArrayList<>();

        for (Drawing drawing : drawings) {
            Version version = versionServices.getLastVersion(drawing.getId());
            DrawingDTO drawingDTO = new DrawingDTO(drawing.getId(), version.getNumFigures(), drawing.getIdUser(),
                    drawing.isView(), drawing.getName(), drawing.getUser().getUsername(),
                    version.getFigures(), drawing.getDate(), version.getDateModify());
            drawingsDTOS.add(drawingDTO);
        }

        return drawingsDTOS;
    }

    public DrawingDTO getDTODrawing(int drawingId, User user) throws YouCantModifyException {


            // Obtindre el dibuix des del servei de dibuixos
            Drawing drawing = drawingServices.getDrawing(drawingId);

            // Obtenir la versió més recient del dibuix
            Version version = versionServices.getLastVersion(drawingId);

            // Si intenten modificar el dibuix desde la paperera no es podrà fer
            if (drawing.isTrash()) {
                throw new YouCantModifyException();
            }

            // Comprovar si el dibuix es del mateix usuari que l'intenta modificar
            if (!Objects.equals(user.getUsername(), drawing.getUser().getUsername())) {
                throw new YouCantModifyException();
            }


        DrawingDTO drawingDTO = new DrawingDTO();
        drawingDTO.setFigures(version.getFigures());
        drawingDTO.setName(drawing.getName());
        drawingDTO.setId(drawing.getId());
        drawingDTO.setVisualization(drawing.isView());
        return drawingDTO;
    }
}
