package com.code.deventhusiast.alibaba.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Errors{

	@SerializedName("fname")
	private List<String> fname;

	@SerializedName("country")
	private List<String> country;

	@SerializedName("lname")
	private List<String> lname;

	@SerializedName("enterprise")
	private List<String> enterprise;

	public void setFname(List<String> fname){
		this.fname = fname;
	}

	public List<String> getFname(){
		return fname;
	}

	public void setCountry(List<String> country){
		this.country = country;
	}

	public List<String> getCountry(){
		return country;
	}

	public void setLname(List<String> lname){
		this.lname = lname;
	}

	public List<String> getLname(){
		return lname;
	}

	public void setEnterprise(List<String> enterprise){
		this.enterprise = enterprise;
	}

	public List<String> getEnterprise(){
		return enterprise;
	}

	@Override
 	public String toString(){
		return 
			"Errors{" + 
			"fname = '" + fname + '\'' + 
			",country = '" + country + '\'' + 
			",lname = '" + lname + '\'' + 
			",enterprise = '" + enterprise + '\'' + 
			"}";
		}
}