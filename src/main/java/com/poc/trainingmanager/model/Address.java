package com.poc.trainingmanager.model;

import java.util.UUID;

import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

public class Address {

	@PrimaryKeyColumn
	private UUID id;
	private String street;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

}
