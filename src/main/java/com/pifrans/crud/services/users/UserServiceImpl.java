package com.pifrans.crud.services.users;

import com.pifrans.crud.domains.entities.User;
import com.pifrans.crud.repositories.UserRepository;
import com.pifrans.crud.services.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService {


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
    }
}
