package com.example.smartpantrymanager.network;

import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**describing apis the app can call. meth = one url on node.js server*/
public interface ApiService {

    @GET("pantry")
    Call<List<PantryItem>> getPantry();                       // READ

    @POST("pantry")
    Call<PantryItem> addPantry(@Body PantryItem item);        // CREATE

    @PUT("pantry/{id}")
    Call<PantryItem> updatePantry(@Path("id") int id, @Body PantryItem item);  // UPDATE

    @DELETE("pantry/{id}")
    Call<Void> deletePantry(@Path("id") int id);              // DELETE

    @GET("recipes")
    Call<List<Recipe>> getRecipes();

    @GET("recipes/{id}")
    Call<Recipe> getRecipe(@Path("id") int id);
}