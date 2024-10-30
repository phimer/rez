//package com.aero.demo.tests;
//
//import com.aero.model.Recipe;
//import com.aero.services.RecipeService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class DatabaseTest {
//
//    @Autowired
//    private RecipeService recipeService;
//
//    @Test
//    public void testDbGet() {
//        List<Recipe> recipes = recipeService.getAll();
//
//        log.info(recipes.toString());
//
//        log.info("RECIPES SIZE: " + recipes.size());
//
//        Assert.assertNotEquals(recipes.size(), 0);
//    }
//
//
//}
//
