package com.poc.trainingmanager.model;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import com.datastax.driver.core.utils.UUIDs;

@Table("users")
public class User {
	@PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
	private UUIDs id;
	private String firstName;
	private String lastName;
	private boolean isActive;

	Address addres;

	public UUIDs getId() {
		return id;
	}

	public void setId(UUIDs id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
