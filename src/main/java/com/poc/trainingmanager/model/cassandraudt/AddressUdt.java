package com.poc.trainingmanager.model.cassandraudt;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.UserDefinedType;

/**
 * @author Kaustubh.Kaustubh
 *         <p>
 *         This is the POJO that stores the user's address. It also mapped with
 *         the User Defined Type "address" in Cassandra. Annotations used are
 *         Datastax driver's.
 *         </p>
 */
@UserDefinedType("address")
public class AddressUdt {

	@Column("door_number")
	String doorNumber;

	@Column("street_name")
	String streetName;

	String area;

	String city;

	String state;

	String country;

	String zipcode;

	public AddressUdt() {
	}

	public AddressUdt(String doorNumber, String streetName, String area, String city, String state, String country,
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
