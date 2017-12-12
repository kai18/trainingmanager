package com.poc.trainingmanager.model.wrapper;

import java.util.Set;
import java.util.UUID;

import com.poc.trainingmanager.model.cassandraudt.AddressUdt;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;

public class UserSearchWrapper {
	private UUID id;
	private String firstName;
	private String lastName;
	private String gender;
	private String emailId;
	private String phoneNumber;
	private AddressUdt address;
	private Set<DepartmentUdt> departments;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public AddressUdt getAddress() {
		return address;
	}

	public void setAddress(AddressUdt address) {
		this.address = address;
	}

	public Set<DepartmentUdt> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<DepartmentUdt> departments) {
		this.departments = departments;
	}

}
