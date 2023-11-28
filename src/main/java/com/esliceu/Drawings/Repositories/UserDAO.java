package com.esliceu.Drawings.Repositories;

import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Exceptions.UserDoesntExist;
import com.esliceu.Drawings.Exceptions.UserExist;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO {

    void saveUser(User user) throws UserExist;

    User getUser(String userName, String password) throws UserDoesntExist;
}
