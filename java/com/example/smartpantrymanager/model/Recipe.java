package com.example.smartpantrymanager.model;

import java.io.Serializable;
import java.util.List;

/**a recipe with its ingredients.done by GET /recipes */
public class Recipe implements Serializable {
    private int id;
    private String name;
    private String steps;
    private List<RecipeIngredient> ingredients;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSteps() { return steps; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
}