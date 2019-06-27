package com.example.android.bakingrecipes.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model used to hold each Ingredient details
 */
public class Ingredient implements Parcelable {

    private String ingredient;
    private float quantity;
    private String measure;

    public Ingredient(String ingredient, float quantity, String measure) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.measure = measure;
    }

    public Ingredient(Parcel parcel) {
        ingredient = parcel.readString();
        quantity = parcel.readFloat();
        measure = parcel.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getIngredient() {
        return ingredient;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    @Override
    public String toString() {
        String recipeIngredient = ingredient;
        recipeIngredient += (" (")
                + (String.valueOf(quantity))
                + (" ")
                + (measure)
                + (")");
        return recipeIngredient;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ingredient);
        parcel.writeFloat(quantity);
        parcel.writeString(measure);
    }
}
