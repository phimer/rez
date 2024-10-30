package com.aero.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ReducedRecipe {

  private int id;
  private String name;
  private int time;
  private String category;
}
