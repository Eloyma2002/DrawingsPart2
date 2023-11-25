package com.esliceu.Drawings.Repositories;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import java.util.List;

public interface DrawingDAO {

    boolean save(Drawing dr);

    List<Drawing> loadAllLists();

    List<Drawing> loadMyList(User user);

    boolean deleteDrawing(int id, User user);

    Drawing getDrawing(int id);

    boolean modifyFigures(int id, String figures, String newName, int size, User user);


}
