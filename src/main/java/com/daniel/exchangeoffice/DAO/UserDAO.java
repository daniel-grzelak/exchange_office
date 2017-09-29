package com.daniel.exchangeoffice.DAO;

import com.daniel.exchangeoffice.classes.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
