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
import android.widget.ImageView;
import android.widget.TextView;

import com.fjoglar.bakingapp.R;
import com.fjoglar.bakingapp.data.model.Recipe;
import com.fjoglar.bakingapp.util.ui.UiUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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
        final Recipe recipe = mRecipesList.get(position);
        if (recipe.getImage().isEmpty()) {
            Picasso.get()
                    .load(UiUtils.getImageResource(recipe.getId()))
                    .into(holder.mImageViewItemRecipeBanner);
        } else {
            Picasso.get()
                    .load(recipe.getImage())
                    .into(holder.mImageViewItemRecipeBanner);
        }
        holder.mTextViewItemRecipeName.setText(recipe.getName());
        holder.mCardViewItemRecipeCard
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

        @BindView(R.id.cardview_item_recipe_card)
        CardView mCardViewItemRecipeCard;
        @BindView(R.id.imageview_item_recipe_banner)
        ImageView mImageViewItemRecipeBanner;
        @BindView(R.id.textview_item_recipe_name)
        TextView mTextViewItemRecipeName;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface RecipeClickListener {
        void onRecipeClicked(Recipe recipe);
    }
}