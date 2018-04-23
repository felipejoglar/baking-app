/*
 * Copyright 2018 Felipe Joglar Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fjoglar.bakingapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Model class for a recipe Ingredient.
 */
@Entity(tableName = "ingredients",
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = CASCADE))
public class Ingredient {

    @PrimaryKey
    private int id;
    private double quantity;
    private String measure;
    private String ingredient;
    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    /**
     * Use this constructor to create a new Ingredient.
     *
     * @param quantity   quantity of the ingredient
     * @param measure    measure of the ingredient
     * @param ingredient name of the ingredient
     */
    public Ingredient(int id, double quantity, String measure, String ingredient, int recipeId) {
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
    }

    public Ingredient() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id &&
                Double.compare(that.quantity, quantity) == 0 &&
                recipeId == that.recipeId &&
                Objects.equals(measure, that.measure) &&
                Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, quantity, measure, ingredient, recipeId);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                ", recipeId=" + recipeId +
                '}';
    }
}