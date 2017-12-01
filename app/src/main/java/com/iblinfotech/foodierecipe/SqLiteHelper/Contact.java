package com.iblinfotech.foodierecipe.SqLiteHelper;

import com.iblinfotech.foodierecipe.model.RecipeIngredientsData;

import java.util.ArrayList;
import java.util.HashMap;

public class Contact {


	public Contact() {

	}

	public HashMap<Object, ArrayList<RecipeIngredientsData>> getChildDetail() {
		return childDetail;
	}

	public void setChildDetail(HashMap<Object, ArrayList<RecipeIngredientsData>> childDetail) {
		this.childDetail = childDetail;
	}

	private HashMap<Object, ArrayList<RecipeIngredientsData>> childDetail;
	//private variables
	int _id;
	String _name;
	String _phone_number;
	
	// Empty constructor
	public Contact(String name){
		this._name = name;
	}
	// constructor
	public Contact(int id, String name, String _phone_number){
		this._id = id;
		this._name = name;
		this._phone_number = _phone_number;
	}
	
	// constructor
	public Contact(String name, String _phone_number){
		this._name = name;
		this._phone_number = _phone_number;
	}

	public Contact(String name, String _phone_number, HashMap<Object, ArrayList<RecipeIngredientsData>> childDetail) {
		this._name = name;
		this._phone_number = _phone_number;
		this.childDetail = childDetail;
	}

	// getting ID
	public int getID(){
		return this._id;
	}
	
	// setting id
	public void setID(int id){
		this._id = id;
	}
	
	// getting name
	public String getName(){
		return this._name;
	}
	
	// setting name
	public void setName(String name){
		this._name = name;
	}
	
	// getting phone number
	public String getPhoneNumber(){
		return this._phone_number;
	}
	
	// setting phone number
	public void setPhoneNumber(String phone_number){
		this._phone_number = phone_number;
	}
}
