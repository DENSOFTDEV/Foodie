package com.iblinfotech.foodierecipe.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

@JsonObject
public class RecipeReviewData implements Serializable {

    @JsonField(name = "review_id")
    String review_id;
    @JsonField(name = "review")
    String review;
    @JsonField(name = "total_star")
    String total_star;
    @JsonField(name = "username")
    String username;
    @JsonField(name = "user_image")
    String user_image;

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTotal_star() {
        return total_star;
    }

    public void setTotal_star(String total_star) {
        this.total_star = total_star;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }


}
