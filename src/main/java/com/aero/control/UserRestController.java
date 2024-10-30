package com.aero.control;


import com.aero.security.error.ErrorMessage;
import com.aero.model.User;
import com.aero.services.UserService;
import com.google.gson.Gson;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserRestController {

  @Autowired
  private UserService userService;

  @Autowired
  private ErrorMessage errorMessage;

  @Autowired
  PasswordEncoder passwordEncoder;

  @CrossOrigin
  @RequestMapping(value = {"/api/user/{name}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getUserByName(@PathVariable("name") String userName) {

    Optional<User> optionalUser = userService.findOne(userName);
    User user = optionalUser.isPresent() ? optionalUser.get() : null;

    String userJson = new Gson().toJson(user);

    log.info(userJson);

    return new ResponseEntity<String>(userJson, HttpStatus.OK);

  }

  @CrossOrigin
  @RequestMapping(value = {
      "/api/user"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> createUser(@RequestBody User user) {


    log.info("adding user: " + user.getUsername());


    user.setRoles("ROLE_USER");

    //Password p = new Password();

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    Optional<User> optionalUser = userService.findOne(user.getUsername());
    User checkUserFromDb = optionalUser.isPresent() ? optionalUser.get() : null;

    if (checkUserFromDb != null) {

      log.info("did not create user - user already exists");

      String jsonErrorMessage = errorMessage
          .setMessage("Username already exists")
          .setError(HttpStatus.BAD_REQUEST)
          .toJson();

      return new ResponseEntity<String>(jsonErrorMessage, HttpStatus.BAD_REQUEST);

    }

    User userFromDb = userService.createUser(user);
    String userJson = new Gson().toJson(userFromDb);


    log.info("user added: " + userFromDb.getUsername());

    return new ResponseEntity<String>(userJson, HttpStatus.OK);


  }

  @CrossOrigin
  @RequestMapping(value = {"/api/users"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getAllUsers() {

    log.info("users");

    List<User> userList = userService.getAll();
    String userListJson = new Gson().toJson(userList);

    //if list is empty return no_content status
    if (userList.size() == 0) {
      return new ResponseEntity<String>(userListJson, HttpStatus.NO_CONTENT);
    }

    return new ResponseEntity<String>(userListJson, HttpStatus.OK);

  }


}
