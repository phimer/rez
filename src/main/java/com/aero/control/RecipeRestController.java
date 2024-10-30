package com.aero.control;

import com.aero.model.ReducedRecipe;
import com.aero.services.RecipeService;
import com.aero.util.RecipeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import com.aero.model.Recipe;

import java.util.List;
import java.util.Optional;

//
///**
// * This Class is the RestController
// * It handles Http Requests.
// * @author philip
// *
// */
@RestController
@Slf4j
public class RecipeRestController {

  @Autowired
  private RecipeService recipeService;

  /**
   * Returns just id, name, time, category -> for more details fetch single recipe
   * @return
   */
  @CrossOrigin
  @RequestMapping(value = {"/api/recipe"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getAllRecipes() {

    List<Recipe> recipeList = recipeService.getAll();

    //if list is empty return no_content status
    if (recipeList.size() == 0) {
      return new ResponseEntity<String>(new Gson().toJson(recipeList), HttpStatus.NO_CONTENT);
    }


    //return not all fields of recipe to reduce data sent over http
    List<ReducedRecipe> reducedRecipeList = new ArrayList<>();
    recipeList.forEach(elem -> {
      reducedRecipeList.add(new ReducedRecipe(
          elem.getId(), elem.getName(), elem.getTime(),elem.getCategory()
      ));
    });

    String recipeListJson = new Gson().toJson(reducedRecipeList);
    return new ResponseEntity<String>(recipeListJson, HttpStatus.OK);

  }

  @CrossOrigin
  @RequestMapping(value = {
      "/api/recipe/{id}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getRecipeById(@PathVariable("id") String recipeId) {

    int id = Integer.parseInt(recipeId);

    //two database calls?
    //Recipe rec = recipeService.findOne(id).isPresent() ? recipeService.findOne(id).get() : null;

    Optional<Recipe> optionalRecipe = recipeService.findOne(id);
    Recipe recipe = optionalRecipe.isPresent() ? optionalRecipe.get() : null;

    String recipeJson = new Gson().toJson(recipe);

    return new ResponseEntity<String>(recipeJson, HttpStatus.OK);


  }

  @CrossOrigin
  @RequestMapping(value = {
      "/api/recipe"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> createRecipe(@RequestBody Recipe recipe, Authentication authentication) {

    //set user from jwt token (get username from logged in user)
    recipe.setUser(authentication.getName());


    //take the try catch out again (only for angular testing)
    try {
      //get categories in to correct format
      recipe.setCategory(RecipeUtil.cleanupCategoryString(recipe.getCategory()));
    } catch (Exception e) {

    }

    Recipe rec = recipeService.createRecipe(recipe);
    String recipeJson = new Gson().toJson(rec);

    log.info("recipe added: " + rec.getId() + ", " + rec.getName());

    return new ResponseEntity<String>(recipeJson, HttpStatus.OK);


  }

  @CrossOrigin
  @RequestMapping(value = "/api/recipe", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> updateRecipeRest(@RequestBody Recipe recipe, Authentication authentication) {

    log.info("update: " + recipe.getName());

    //get recipe from db to see which user created that recipe
    Optional<Recipe> optionalRecipe = recipeService.findOne(recipe.getId());
    Recipe recipeFromDB = optionalRecipe.isPresent() ? optionalRecipe.get() : null;

    //if logged in user(user in jwt) is not the one who created the recipe -> recipe can not be deleted by this user
    if (!authentication.getName().equals(recipeFromDB.getUser())) {
      String response = new Gson().toJson("logged in user is not creator of this recipe");
      return new ResponseEntity<String>(response, HttpStatus.FORBIDDEN);
    }

    Recipe updatedRecipe = recipeService.updateRecipe(recipe);
    String recipeJson = new Gson().toJson(updatedRecipe);

    return new ResponseEntity<String>(recipeJson, HttpStatus.OK);
  }


  @CrossOrigin
  @RequestMapping(value = "/api/recipe", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> deleteRecipeRest(@RequestBody Recipe recipe, Authentication authentication) {

    log.info("delete: " + recipe.getName());

    //get recipe from db to see which user created that recipe
    Optional<Recipe> optionalRecipe = recipeService.findOne(recipe.getId());
    Recipe recipeFromDB = optionalRecipe.isPresent() ? optionalRecipe.get() : null;

    //if logged in user(user in jwt) is not the one who created the recipe -> recipe can not be deleted by this user
    if (!authentication.getName().equals(recipeFromDB.getUser())) {
      String response = new Gson().toJson("logged in user is not creator of this recipe");
      return new ResponseEntity<String>(response, HttpStatus.FORBIDDEN);
    }

    int id = recipe.getId();
    boolean checkIfDeleted = recipeService.deleteRecipe(id);
    String checkIfUserDeletedJson = new Gson().toJson(checkIfDeleted);

    if (checkIfDeleted) {
      return new ResponseEntity<String>(checkIfUserDeletedJson, HttpStatus.OK);
    }

    return new ResponseEntity<String>(checkIfUserDeletedJson, HttpStatus.NOT_FOUND);


  }

  @CrossOrigin
  @RequestMapping(value = "/api/searchRecipes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> searchRecipeRest(@RequestBody String search) {

    //turn json list into arraylist
    TypeToken<List<String>> token = new TypeToken<List<String>>() {
    };
    List<String> searchWords = new Gson().fromJson(search, token.getType());

    //get recipes that fit searchWords
    List<Recipe> recipeList = recipeService.findRecipesCustomQuery(searchWords);

    //turn list into json
    String matchingRecipesJson = new Gson().toJson(recipeList);

    return new ResponseEntity<String>(matchingRecipesJson, HttpStatus.OK);

  }



}
