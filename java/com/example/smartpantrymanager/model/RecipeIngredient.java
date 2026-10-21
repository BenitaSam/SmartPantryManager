package com.example.smartpantrymanager.model;

import java.io.Serializable;

/**one ingredient line in a recipe*/
public class RecipeIngredient implements Serializable {
    private String name;
    private double quantity;
    private String unit;

    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
}