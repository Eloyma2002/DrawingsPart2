package com.esliceu.Drawings.Repositories;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Entities.Version;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrawingREPO {

    void save(Drawing dr, User user, Version version);

    List<Drawing> loadAllLists(User user);

    List<Drawing> loadMyTrash(User user);

    boolean deleteDrawing(Drawing drawing);

    boolean deleteTrash(Drawing drawing);

    Drawing getDrawing(int id);

    User getUserById(int idUser);

    void addVersion(Version version);

    void changeDrawingName(Drawing drawing, String name);

    boolean recoverDrawingFromTrash(int drawingId);
}
