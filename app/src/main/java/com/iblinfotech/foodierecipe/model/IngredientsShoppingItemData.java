package com.iblinfotech.foodierecipe.model;

import java.io.Serializable;

public class IngredientsShoppingItemData implements Serializable {


    public String name;
    public String image;
    public String amount;
    public String measure;

    public boolean isAlreadyinList() {
        return isAlreadyinList;
    }

    public void setAlreadyinList(boolean alreadyinList) {
        isAlreadyinList = alreadyinList;
    }

    private boolean isAlreadyinList;

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
