package com.aero.security;

import com.aero.model.User;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


//@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class MyUserDetails implements UserDetails {


  private String userName;
  private String password;
  private List<GrantedAuthority> authorities;

  public MyUserDetails(User user) {
    this.userName = user.getUsername();
    this.password = user.getPassword();
    this.authorities = Arrays.stream(user.getRoles().split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }
//
//  @Override
//  public Collection<? extends GrantedAuthority> getAuthorities() {
//    Set<Role> roles = user.getRoles();
//    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//
//    for (Role role : roles) {
//      authorities.add(new SimpleGrantedAuthority(role.getName()));
//    }


    @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
