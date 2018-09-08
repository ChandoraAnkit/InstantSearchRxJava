package com.chandora.androidy.instantsearchrxjava.network.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class Contact{

	@SerializedName("image")
	private String image;

	@SerializedName("phone")
	private String phone;

	@SerializedName("name")
	private String name;

	@SerializedName("source")
	private String source;

	@SerializedName("email")
	private String email;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setSource(String source){
		this.source = source;
	}

	public String getSource(){
		return source;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"image = '" + image + '\'' + 
			",phone = '" + phone + '\'' + 
			",name = '" + name + '\'' + 
			",source = '" + source + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && (obj instanceof Contact)){
			return ((Contact)obj).getEmail().equalsIgnoreCase(email);
		}
		return false;
	}
}