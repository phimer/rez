package com.aero.services;

//like RecipeRepositoryImpl

import com.aero.model.Recipe;
import com.aero.model.ReducedRecipe;
import com.aero.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

  @Autowired
  private RecipeRepository recipeRepository;

  @PersistenceContext
  private EntityManager em;

  public List<Recipe> getAll() {
    return recipeRepository.findAll();
  }

  public Optional<Recipe> findOne(int id) {
    return this.recipeRepository.findById(id);
  }

  public Recipe createRecipe(final Recipe recipe) {
    return this.recipeRepository.save(recipe);
  }

  @Modifying(clearAutomatically = true, flushAutomatically = true) //not sure yet
  @Transactional
  public Recipe updateRecipe(final Recipe recipe) {

    Optional<Recipe> optionalRecipe = this.recipeRepository.findById(recipe.getId());

    Recipe rec = optionalRecipe.get();

    rec.setName(recipe.getName());
    rec.setTime(recipe.getTime());
    rec.setIngredients(recipe.getIngredients());
    rec.setCategory(recipe.getCategory());
    rec.setPreparation(recipe.getPreparation());

    return this.recipeRepository.save(rec);
  }

  @Transactional
  public boolean deleteRecipe(final int id) {

    try {
      Optional<Recipe> optionalRecipe = this.recipeRepository.findById(id);
      Recipe rec = optionalRecipe.get();
      this.recipeRepository.delete(rec);
      return true;
    } catch (Exception e) {
      return false;
    }

  }

//    public List<Recipe> findByNameLike(String name) {
//        return recipeRepository.findByNameLike(name);
//    }

  public List<Recipe> findRecipesByNameLike(String nameOne, String nameTwo) {
    return recipeRepository.findRecipesByNameLike(nameOne, nameTwo);
  }

  public List<Recipe> findRecipesCustomQuery(List<String> searchWords) {

    StringBuilder customQuery = new StringBuilder();
    customQuery.append("SELECT r FROM Recipe r WHERE");

    searchWords.forEach(elem -> customQuery.append(" r.name LIKE %" + elem + "% OR"));
    customQuery.setLength(customQuery.length() - 3);

    String query = customQuery.toString();

    List<Recipe> resultList = em.createQuery("SELECT r FROM Recipe r")
        .getResultList();

    return resultList;

//        public List findWithName(String name) {
//            return em.createQuery(
//                            "SELECT c FROM Customer c WHERE c.name LIKE :custName")
//                    .setParameter("custName", name)
//                    .setMaxResults(10)
//                    .getResultList();
//        }
  }
}