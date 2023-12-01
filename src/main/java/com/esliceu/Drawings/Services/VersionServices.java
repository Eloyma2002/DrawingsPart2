package com.esliceu.Drawings.Services;

import com.esliceu.Drawings.Entities.Version;
import com.esliceu.Drawings.Repositories.VersionREPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VersionServices {

    @Autowired
    VersionREPO versionREPO;

    public Version getLastVersion(int idDrawing) {
        return versionREPO.getLastVersion(idDrawing);
    }

}
