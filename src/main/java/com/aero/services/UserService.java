package com.aero.services;

import com.aero.model.User;
import com.aero.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public List<User> getAll() {return this.userRepository.findAll();}

  public Optional<User> findOne(String userName) {return this.userRepository.findById(userName);}

  public User createUser(final User user) {return this.userRepository.save(user);}

//  public Optional<User> findUserByName(String userName) {
//    return this.userRepository.findByUserName(userName);
//  }
}
