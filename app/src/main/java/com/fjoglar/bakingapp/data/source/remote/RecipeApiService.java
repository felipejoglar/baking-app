package com.fjoglar.bakingapp.data.source.remote;

import com.fjoglar.bakingapp.data.model.Recipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * API for retrieving recipes from the network.
 */
public interface RecipeApiService {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Observable<List<Recipe>> getRecipesFromJson();
}
