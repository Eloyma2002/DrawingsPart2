package com.esliceu.Drawings.Repositories;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Entities.Version;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrawingREPO {

    void save(Drawing dr, User user, Version version);

    List<Drawing> loadAllLists();

    List<Drawing> loadMyTrash(User user);

    boolean deleteDrawing(Drawing drawing);

    boolean deleteTrash(Drawing drawing);

    Drawing getDrawing(int id);

    boolean modifyFigures(Version version);


}
