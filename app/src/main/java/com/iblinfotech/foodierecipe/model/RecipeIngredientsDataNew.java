package com.iblinfotech.foodierecipe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

@JsonObject
public class RecipeIngredientsDataNew implements Serializable {

    @JsonField(name = "name")
    String name;
    @JsonField(name = "image")
    String image;
    @JsonField(name = "amount")
    String amount;

    public String getServingsPerson() {
        return servingsPerson;
    }

    public void setServingsPerson(String servingsPerson) {
        this.servingsPerson = servingsPerson;
    }

    @JsonField(name = "servingsPerson")
    String servingsPerson;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
