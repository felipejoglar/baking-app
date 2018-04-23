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
 * Model class for a recipe Step.
 */
@Entity(tableName = "steps",
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = CASCADE))
public class Step {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "step_id")
    private int stepId;
    @ColumnInfo(name = "short_description")
    private String shortDescription;
    private String description;
    @ColumnInfo(name = "video_url")
    private String videoUrl;
    @ColumnInfo(name = "thumbnail_url")
    private String thumbnailUrl;
    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    /**
     * Use this constructor to create a new Step.
     *
     * @param id               id of the step
     * @param shortDescription short description of the step
     * @param description      description of the step
     * @param videoUrl         video URL of the step
     * @param thumbnailUrl     thumbnail URL of the step
     */
    public Step(int id, int stepId, String shortDescription, String description, String videoUrl,
                String thumbnailUrl, int recipeId) {
        this.id = id;
        this.stepId = stepId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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
        Step step = (Step) o;
        return id == step.id &&
                stepId == step.stepId &&
                recipeId == step.recipeId &&
                Objects.equals(shortDescription, step.shortDescription) &&
                Objects.equals(description, step.description) &&
                Objects.equals(videoUrl, step.videoUrl) &&
                Objects.equals(thumbnailUrl, step.thumbnailUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, stepId, shortDescription, description, videoUrl, thumbnailUrl, recipeId);
    }

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", stepId=" + stepId +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", recipeId=" + recipeId +
                '}';
    }
}