package com.example.smartpantrymanager.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**serializable so can be passed between screens in an intent*/
public class PantryItem implements Serializable {
    private int id;
    private String name;
    private double quantity;
    private String unit;

    //json uses expiry_date android uses expiryDate. @SerializedName maps dem to e/o
    @SerializedName("expiry_date")
    private String expiryDate;

    public PantryItem() { }

    public PantryItem(String name, double quantity, String unit, String expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}