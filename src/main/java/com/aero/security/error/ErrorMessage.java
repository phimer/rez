package com.aero.security.error;

import com.google.gson.Gson;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

  private final String timestamp = LocalDateTime.now().toString();
  private int status;
  private HttpStatus error;
  private String message;
  private String path;


  public ErrorMessage setStatus(int status) {
    this.status = status;
    return this;
  }

  public ErrorMessage setError(HttpStatus error) {
    this.error = error;
    this.status = error.value();
    return this;
  }

  public ErrorMessage setMessage(String message) {
    this.message = message;
    return this;
  }

  public ErrorMessage setPath(String path) {
    this.path = path;
    return this;
  }

  public String toJson() {
    return new Gson().toJson(this);
  }


}
