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

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

/**
 * Model class for a Recipe.
 */
@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey
    private int id;
    private String name;
    private int servings;
    private String image;

    /**
     * Use this constructor to create a new Recipe.
     *
     * @param id       id of the recipe
     * @param name     name of the recipe
     * @param servings number of sering of he recipe
     * @param image    image of the recipe
     */
    public Recipe(int id, String name, int servings, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public Recipe() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id &&
                servings == recipe.servings &&
                Objects.equals(name, recipe.name) &&
                Objects.equals(image, recipe.image);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, servings, image);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", servings=" + servings +
                ", image='" + image + '\'' +
                '}';
    }
}
