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

package com.fjoglar.bakingapp.recipes;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fjoglar.bakingapp.R;
import com.fjoglar.bakingapp.data.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.List;


/**
 * Adapter that manages a collection of {@link Recipe}.
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private static final String TAG = RecipesAdapter.class.getSimpleName();

    private final RecipeClickListener mOnClickListener;
    private List<Recipe> mRecipesList;

    public RecipesAdapter(RecipeClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO: by now, we just put the name of the recipe. It must show a more visually appealing
        // layout.
        final Recipe recipe = mRecipesList.get(position);
        holder.mTextViewRecipeItemName.setText(recipe.getName());
        holder.mCardviewItemRecipeCard
                .setOnClickListener(v -> mOnClickListener.onRecipeClicked(recipe));
    }

    @Override
    public int getItemCount() {
        return (mRecipesList == null) ? 0 : mRecipesList.size();
    }

    public List<Recipe> getRecipesList() {
        return mRecipesList;
    }

    public void setRecipesList(List<Recipe> recipesList) {
        validateRecipesList(recipesList);
        mRecipesList = recipesList;
        notifyDataSetChanged();
    }

    private void validateRecipesList(List<Recipe> recipesList) {
        if (recipesList == null) {
            throw new IllegalArgumentException("The recipes list cannot be null");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_recipe_item_name)
        TextView mTextViewRecipeItemName;
        @BindView(R.id.cardview_item_recipe_card)
        CardView mCardviewItemRecipeCard;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface RecipeClickListener {
        void onRecipeClicked(Recipe recipe);
    }
}