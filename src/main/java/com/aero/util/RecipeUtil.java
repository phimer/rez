package com.aero.util;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;


@UtilityClass
public class RecipeUtil {

  public String cleanupCategoryString(String str) {

    String replacedString = str.replaceAll(" ", ",")
        .replaceAll(",,", ",");

    while (replacedString.endsWith(",")) {
      replacedString = replacedString.substring(0, replacedString.length() - 1);
    }

    return replacedString;
  }


}
