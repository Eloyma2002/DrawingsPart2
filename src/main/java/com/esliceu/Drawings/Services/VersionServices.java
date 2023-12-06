package com.esliceu.Drawings.Services;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Entities.Version;
import com.esliceu.Drawings.Repositories.VersionREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VersionServices {

    @Autowired
    VersionREPO versionREPO;

    // Obtenir l'ultima versió d'un dibuix
    public Version getLastVersion(int idDrawing) {
        return versionREPO.getLastVersion(idDrawing);
    }

    // Obtenir totes les versions d'un dibuix
    public List<Version> getAllVersionsOfADrawing(Drawing drawing) {
        if (drawing.isTrash()) {
            return null;
        }
        return versionREPO.getAllVersion(drawing.getId());
    }

    public Version getVersionById(User user, int versionId) {

        return versionREPO.getVersionById(user, versionId);
    }
}
