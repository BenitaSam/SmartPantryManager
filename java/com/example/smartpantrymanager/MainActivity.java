package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapter.PantryAdapter;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.network.ApiClient;
import com.example.smartpantrymanager.network.ApiErrors;
import com.example.smartpantrymanager.util.AppUtils;
import com.example.smartpantrymanager.util.Prefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**pantry list screen- shows every ingredient stored in sql*/
public class MainActivity extends AppCompatActivity {

    private TextView tvEmpty, tvExpiryBanner;
    private PantryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvEmpty = findViewById(R.id.tvEmpty);
        tvExpiryBanner = findViewById(R.id.tvExpiryBanner);

        RecyclerView rvPantry = findViewById(R.id.rvPantry);
        rvPantry.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(new PantryAdapter.OnItemActionListener() {
            @Override public void onEdit(PantryItem item) { /* Stage 6 */ }
            @Override public void onDelete(PantryItem item) { /* Stage 6 */ }
        });
        rvPantry.setAdapter(adapter);
    }

    //runs whenever screen becomes visible so list always fresh
    @Override
    protected void onResume() {
        super.onResume();
        loadPantry();
    }

    private void loadPantry() {
        ApiClient.getService().getPantry().enqueue(new Callback<List<PantryItem>>() {
            @Override
            public void onResponse(Call<List<PantryItem>> call, Response<List<PantryItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showItems(response.body());
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

    private void showItems(List<PantryItem> items) {
        boolean alertsOn = Prefs.isExpiryAlertsOn(this);
        adapter.setData(items, alertsOn);

        if (items.isEmpty()) {
            tvEmpty.setText("Your pantry is empty.\nTap + to add your first ingredient.");
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }

        int soon = 0;
        for (PantryItem p : items) {
            if (p.getExpiryDate() != null && AppUtils.daysUntil(p.getExpiryDate()) <= 3) soon++;
        }
        if (alertsOn && soon > 0) {
            tvExpiryBanner.setText(soon + (soon == 1 ? " item is" : " items are")+ " expired or expiring within 3 days");
            tvExpiryBanner.setVisibility(View.VISIBLE);
        } else {
            tvExpiryBanner.setVisibility(View.GONE);
        }
    }

    private void showMessage(String message) {
        adapter.setData(new ArrayList<>(), false);
        tvExpiryBanner.setVisibility(View.GONE);
        tvEmpty.setText(message);
        tvEmpty.setVisibility(View.VISIBLE);
    }
}