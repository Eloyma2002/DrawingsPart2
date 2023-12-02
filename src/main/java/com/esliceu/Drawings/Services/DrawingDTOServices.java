package com.esliceu.Drawings.Services;

import com.esliceu.Drawings.DTO.DrawingDTO;
import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DrawingDTOServices {
    @Autowired
    VersionServices versionServices;

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
}
