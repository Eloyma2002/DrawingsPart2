package com.esliceu.Drawings.Repositories;

import com.esliceu.Drawings.Entities.Drawing;
import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Entities.Version;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersionREPO {

    Version getLastVersion(int drawing);

    List<Version> getAllVersion(int id);

}
