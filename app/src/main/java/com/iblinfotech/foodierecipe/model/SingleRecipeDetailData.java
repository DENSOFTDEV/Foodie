package com.iblinfotech.foodierecipe.model;


import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

@JsonObject
public class SingleRecipeDetailData implements Serializable {

    @JsonField(name = "token")
    private String token;

    @JsonField(name = "recipe_name")
    private String recipe_name;

    @JsonField(name = "recipe_image")
    private ArrayList<String> recipe_image;

    @JsonField(name = "recipe_description_name")
    private String recipe_description_name;

    @JsonField(name = "recipe_description")
    private String recipe_description;

    @JsonField(name = "how_much")
    private ArrayList<RecipeHowMuchData> how_much;

    @JsonField(name = "category")
    private String category;

    @JsonField(name = "category_color_code")
    private String category_color_code;

    @JsonField(name = "category_background")
    private String category_background;

    @JsonField(name = "cuisine")
    private String cuisine;

    @JsonField(name = "type")
    private String type;

    @JsonField(name = "method")
    private ArrayList<RecipeMethodData> method;

    @JsonField(name = "review")
    private ArrayList<RecipeReviewData> review;

    @JsonField(name = "serving_suggesstion")
    private String serving_suggesstion;

    @JsonField(name = "variation")
    private String variation;

    @JsonField(name = "preparation_time")
    private String preparation_time;

    @JsonField(name = "cooking_time")
    private String cooking_time;

    @JsonField(name = "soaking_time")
    private String soaking_time;

    @JsonField(name = "fermentation_time")
    private String fermentation_time;

    @JsonField(name = "review_count")
    private String review_count;

    @JsonField(name = "star_rating")
    private String star_rating;

    @JsonField(name = "is_favourite")
    private String is_favourite;

    public String getCategory_color_code() {
        return category_color_code;
    }

    public void setCategory_color_code(String category_color_code) {
        this.category_color_code = category_color_code;
    }

    public boolean isMyFavourite() {
        return myFavourite;
    }

    public void setMyFavourite(boolean myFavourite) {
        this.myFavourite = myFavourite;
    }

    public ArrayList<RecipeReviewData> getReview() {
        return review;
    }

    public void setReview(ArrayList<RecipeReviewData> review) {
        this.review = review;
    }

    public String getCategory_background() {
        return category_background;
    }

    public void setCategory_background(String category_background) {
        this.category_background = category_background;
    }

    public boolean ismyFavourite() {
        return myFavourite;
    }

    public void setmyFavourite(boolean favourite) {
        myFavourite = favourite;
    }

    boolean myFavourite = false;

    public String getIs_favourite() {
        return is_favourite;
    }

    public ArrayList<String> getRecipe_image() {
        return recipe_image;
    }

    public void setRecipe_image(ArrayList<String> recipe_image) {
        this.recipe_image = recipe_image;
    }

    public void setIs_favourite(String is_favourite) {
        this.is_favourite = is_favourite;
    }

    public String getStar_rating() {
        return star_rating;
    }

    public void setStar_rating(String star_rating) {
        this.star_rating = star_rating;
    }

    public String getReview_count() {
        return review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public String getFermentation_time() {
        return fermentation_time;
    }

    public void setFermentation_time(String fermentation_time) {
        this.fermentation_time = fermentation_time;
    }

    public String getSoaking_time() {
        return soaking_time;
    }

    public void setSoaking_time(String soaking_time) {
        this.soaking_time = soaking_time;
    }

    public String getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(String cooking_time) {
        this.cooking_time = cooking_time;
    }

    public String getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(String preparation_time) {
        this.preparation_time = preparation_time;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public String getServing_suggesstion() {
        return serving_suggesstion;
    }

    public void setServing_suggesstion(String serving_suggesstion) {
        this.serving_suggesstion = serving_suggesstion;
    }

    public ArrayList<RecipeMethodData> getMethod() {
        return method;
    }

    public void setMethod(ArrayList<RecipeMethodData> method) {
        this.method = method;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<RecipeHowMuchData> getHow_much() {
        return how_much;
    }

    public void setHow_much(ArrayList<RecipeHowMuchData> how_much) {
        this.how_much = how_much;
    }

    public String getRecipe_description() {
        return recipe_description;
    }

    public void setRecipe_description(String recipe_description) {
        this.recipe_description = recipe_description;
    }

    public String getRecipe_description_name() {
        return recipe_description_name;
    }

    public void setRecipe_description_name(String recipe_description_name) {
        this.recipe_description_name = recipe_description_name;
    }


    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
