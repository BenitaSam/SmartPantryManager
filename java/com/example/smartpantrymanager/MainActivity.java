package com.example.smartpantrymanager;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiClient.getService().getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_TEST", "Recipes loaded: " + response.body().size()
                            + ", first has " + response.body().get(0).getIngredients().size() + " ingredients");
                } else {
                    Log.d("API_TEST", "Failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d("API_TEST", "Error: " + t.getMessage());
            }
        });
    }
}