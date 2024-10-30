package com.aero.repositories;


import com.aero.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

  List<Recipe> findByNameLike(String name);

  @Query("FROM Recipe r WHERE r.name LIKE %?1% OR r.name LIKE %?2%")
  List<Recipe> findRecipesByNameLike(String searchNameOne, String searchNameTwo);


}
