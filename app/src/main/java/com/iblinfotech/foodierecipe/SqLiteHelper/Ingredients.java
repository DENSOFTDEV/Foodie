package com.iblinfotech.foodierecipe.SqLiteHelper;

import com.iblinfotech.foodierecipe.model.RecipeIngredientsData;

import java.util.ArrayList;
import java.util.HashMap;

public class Ingredients {


	public Ingredients() {

	}


	public String getI_mesure() {
		return i_mesure;
	}

	public void setI_mesure(String i_mesure) {
		this.i_mesure = i_mesure;
	}

	public String getI_name() {
		return i_name;
	}

	public void setI_name(String i_name) {
		this.i_name = i_name;
	}

	private String i_mesure;
	private String i_name;

	// constructor
	public Ingredients(String i_name, String i_mesure){
		this.i_mesure = i_mesure;
		this.i_name = i_name;
	}

}
