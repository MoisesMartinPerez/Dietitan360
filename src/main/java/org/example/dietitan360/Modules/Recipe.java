package org.example.dietitan360.Modules;

import java.util.List;

/**
 * Clase que representa una reecta.
 */
public class Recipe {

    private String id;

    private String name;

    private List<Ingredient> ingredients;
    private List<String> steps;
    private String nutritionistId;

    public Recipe(String name, List<Ingredient> ingredients, List<String> steps, String nutritionistId) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.nutritionistId = nutritionistId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getNutritionistId() {
        return nutritionistId;
    }

    public void setNutritionistId(String nutritionistId) {
        this.nutritionistId = nutritionistId;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", nutritionistId='" + nutritionistId + '\'' +
                '}';
    }
}
