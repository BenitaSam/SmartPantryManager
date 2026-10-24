package com.example.smartpantrymanager.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**feeds pantry items to RecyclerView- decides how many rows,what row looks like,what goes in it*/
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    /**screen implements this so adapter can report taps w/o knowing about Intents or the API*/
    public interface OnItemActionListener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final OnItemActionListener listener;
    private boolean highlightExpiring = true;

    public PantryAdapter(OnItemActionListener listener) {
        this.listener = listener;
    }

    public void setData(List<PantryItem> newItems, boolean highlightExpiring) {
        items.clear();
        items.addAll(newItems);
        this.highlightExpiring = highlightExpiring;
        notifyDataSetChanged();   //tells RecyclerView to redraw
    }

    //how many rows
    @Override
    public int getItemCount() {
        return items.size();
    }

    //build an empty row from item pantry.xml
    // called few times cos rows get recycled
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(v);
    }

    //fill a row w the data for one position
    //called whenever a row scrolls into view (Recyclerview stuff)
    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder h, int position) {
        PantryItem item = items.get(position);

        h.tvName.setText(AppUtils.capitalize(item.getName()));
        h.tvQty.setText(AppUtils.formatQty(item.getQuantity()) + " " + item.getUnit());

        String expiry = item.getExpiryDate();
        h.tvExpiry.setTextColor(h.defaultExpiryColor);   // reset, because rows are reused
        if (expiry == null || expiry.isEmpty()) {
            h.tvExpiry.setText("No expiry date");
        } else {
            long days = AppUtils.daysUntil(expiry);
            if (days < 0) {
                h.tvExpiry.setText("Expired (" + expiry + ")");
                if (highlightExpiring) h.tvExpiry.setTextColor(Color.rgb(198, 40, 40));
            } else if (days <= 3) {
                h.tvExpiry.setText("Expires in " + days + (days == 1 ? " day" : " days") + " (" + expiry + ")");
                if (highlightExpiring) h.tvExpiry.setTextColor(Color.rgb(230, 81, 0));
            } else {
                h.tvExpiry.setText("Expires: " + expiry);
            }
        }

        h.itemView.setOnClickListener(v -> listener.onEdit(item));
        h.ibDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    /**holds refs to the views inside one row so they aren't looked up again and again*/
    static class PantryViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName, tvQty, tvExpiry;
        final ImageButton ibDelete;
        final int defaultExpiryColor;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
            ibDelete = itemView.findViewById(R.id.ibDelete);
            defaultExpiryColor = tvExpiry.getCurrentTextColor();
        }
    }
}