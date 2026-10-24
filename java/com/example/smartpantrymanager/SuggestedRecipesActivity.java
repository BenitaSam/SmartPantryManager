package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.smartpantrymanager.adapter.RecipeAdapter;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.network.ApiClient;
import com.example.smartpantrymanager.network.ApiErrors;
import com.example.smartpantrymanager.util.NavHelper;
import com.example.smartpantrymanager.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Suggested Recipes screen: loads pantry + recipes, runs the strict matcher, lists only the matches. */
public class SuggestedRecipesActivity extends AppCompatActivity {

    private TextView tvEmpty;
    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested);

        tvEmpty = findViewById(R.id.tvEmpty);

        RecyclerView rvRecipes = findViewById(R.id.rvRecipes);
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(recipe -> {
            // Stage 8 replaces this with an Intent to the Recipe Detail screen
            Toast.makeText(this, recipe.getName(), Toast.LENGTH_SHORT).show();
        });
        rvRecipes.setAdapter(adapter);

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        NavHelper.setup(this, nav, R.id.nav_suggested);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();   //re-run the matching every time, so pantry changes show up immediately
    }

    private void loadData() {
        showMessage("Loading...");
        //step 1- get the pantry
        ApiClient.getService().getPantry().enqueue(new Callback<List<PantryItem>>() {
            @Override
            public void onResponse(Call<List<PantryItem>> call, Response<List<PantryItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loadRecipes(response.body());
                } else {
                    showMessage(ApiErrors.fromResponse(response));
                }
            }

            @Override
            public void onFailure(Call<List<PantryItem>> call, Throwable t) {
                showMessage(ApiErrors.fromFailure(t));
            }
        });
    }

    //step 2-get the recipes, then run the strict matcher against the pantry
    private void loadRecipes(final List<PantryItem> pantry) {
        ApiClient.getService().getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showSuggested(RecipeMatcher.findSuggested(response.body(), pantry));
                } else {
                    showMessage(ApiErrors.fromResponse(response));
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                showMessage(ApiErrors.fromFailure(t));
            }
        });
    }

    private void showSuggested(List<Recipe> suggested) {
        adapter.setData(suggested);
        if (suggested.isEmpty()) {
            // Zero-match feedback, as the brief requires (no blank screen)
            tvEmpty.setText("No recipes match your pantry yet - add more ingredients");
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    private void showMessage(String message) {
        adapter.setData(new ArrayList<>());
        tvEmpty.setText(message);
        tvEmpty.setVisibility(View.VISIBLE);
    }
}