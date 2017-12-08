package com.poc.trainingmanager.model;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;

@UDT(name = "address")
public class Address {
	@Field(name = "door_number")
	String doorNumber;
	@Field(name = "street_name")
	String streetName;
	@Field(name = "area")
	String area;
	@Field(name = "city")
	String city;
	@Field(name = "state")
	String state;
	@Field(name = "country")
	String country;
	@Field(name = "zipcode")
	String zipcode;

	public Address() {
		super();
	}

	public Address(String doorNumber, String streetName, String area, String city, String state, String country,
			String zipcode) {
		super();
		this.doorNumber = doorNumber;
		this.streetName = streetName;
		this.area = area;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipcode = zipcode;
	}

	public String getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(String doorNumber) {
		this.doorNumber = doorNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

}
