package com.esliceu.Drawings.Repositories;

import com.esliceu.Drawings.Entities.User;
import com.esliceu.Drawings.Exceptions.UserDoesntExistException;
import com.esliceu.Drawings.Exceptions.UserExistException;
import org.springframework.stereotype.Repository;

@Repository
public interface UserREPO {

    void saveUser(User user) throws UserExistException;

    User getUser(String userName, String password) throws UserDoesntExistException;
}
