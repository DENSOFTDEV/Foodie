package com.iblinfotech.foodierecipe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

@JsonObject
public class CartItemData implements Serializable {
    @JsonField(name = "itemname")
    String itemname;

    @JsonField(name = "cart_quantity")
    String cart_quantity;

    @JsonField(name = "cart_item_image")
    String cart_item_image;

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(String cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public String getCart_item_image() {
        return cart_item_image;
    }

    public void setCart_item_image(String cart_item_image) {
        this.cart_item_image = cart_item_image;
    }
}
