package com.aero.security;

import com.aero.model.User;
import com.aero.security.model.AuthenticationRequest;
import com.aero.security.model.AuthenticationResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private MyUserDetailsService userDetailsService;

  @Autowired
  private JwtUtil jwtTokenUtil;

  @CrossOrigin
  @RequestMapping(value = "/api/authenticate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
              authenticationRequest.getPassword())
      );
    } catch (BadCredentialsException e) {
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = userDetailsService
        .loadUserByUsername(authenticationRequest.getUsername());

    final String jwt = jwtTokenUtil.generateToken(userDetails);

    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }

  /**
   * This method gets called to check if client is logged in, eg if client has a valid token in localstorage
   * @param token
   * @return true if token is valid, false if not
   */
  @CrossOrigin
  @RequestMapping(value = "/api/authenticate/valid", method = RequestMethod.GET)
  public ResponseEntity<?> checkIfTokenIsValid() {

    return new ResponseEntity<Boolean>(true, HttpStatus.OK);

  }

  /**
   * Gets username from jwt
   * @return
   */
  @CrossOrigin
  @RequestMapping(value = {
      "/api/authenticate/username"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getUsernameFromToken(Authentication authentication) throws Exception {
    String username = new Gson().toJson(authentication.getName());
    return new ResponseEntity<String>(username, HttpStatus.OK);
  }


}
