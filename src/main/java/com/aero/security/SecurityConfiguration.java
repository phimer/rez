package com.aero.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private MyUserDetailsService myUserDetailsService;

  @Autowired
  private JwtRequestFilter jwtRequestFilter;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService((myUserDetailsService))
        .passwordEncoder(passwordEncoder());


  }

//  @Bean
//  public PasswordEncoder getPasswordEncoder() {
//    return NoOpPasswordEncoder.getInstance();
//  }

  @Bean
  public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

  //disables csrf
  //authorizes requests for /authenticate
  //every other request needs to be authenticated
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests().antMatchers("/api/authenticate").permitAll()
        .and()
        .authorizeRequests().antMatchers(HttpMethod.GET, "/api/recipe/**").permitAll()
        .and()
        .authorizeRequests().antMatchers(HttpMethod.POST, "/api/user").permitAll()
        .anyRequest().authenticated()
        .and().cors()
        .and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS); //don't create a session
    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); //setup security context each time


  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
