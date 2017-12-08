package com.poc.trainingmanager.model;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Indexed;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import com.datastax.driver.mapping.annotations.Frozen;
import com.poc.trainingmanager.model.cassandraudt.Address;
import com.poc.trainingmanager.model.cassandraudt.Department;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;

@Table(value = "user")
public class User {

	@PrimaryKeyColumn(name = "ID", type = PrimaryKeyType.PARTITIONED)
	private UUID id;

	@Column(value = "FIRST_NAME")
	@Indexed
	private String firstName;

	@Column(value = "LAST_NAME")
	@Indexed
	private String lastName;

	@Column(value = "PASSWORD")
	private String password;

	@Column(value = "GENDER")
	private String gender;

	@Column(value = "EMAIL_ID")
	@Indexed
	private String emailId;

	@Column(value = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(value = "IS_ACTIVE")
	private String isActive;

	@Column(value = "CREATED_DTM")
	private Date createdDtm;

	@Column(value = "UPDATED_DTM")
	private Date updatedDtm;

	private Address address;
	
	@Frozen
	@Indexed
	private Set<RoleUdt> roles;
	
	@Frozen
	@Indexed
	private Set<Department> departments;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedDtm() {
		return createdDtm;
	}

	public void setCreatedDtm(Date createdDtm) {
		this.createdDtm = createdDtm;
	}

	public Date getUpdatedDtm() {
		return updatedDtm;
	}

	public void setUpdatedDtm(Date updatedDtm) {
		this.updatedDtm = updatedDtm;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<RoleUdt> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleUdt> roles) {
		this.roles = roles;
	}

	public Set<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}

	public User(UUID id, String firstName, String lastName, String password, String gender, String emailId,
			String phoneNumber, String isActive, Date createdDtm, Date updatedDtm, Address address, Set<RoleUdt> roles,
			Set<Department> departments) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.gender = gender;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.isActive = isActive;
		this.createdDtm = createdDtm;
		this.updatedDtm = updatedDtm;
		this.address = address;
		this.roles = roles;
		this.departments = departments;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", password=" + password
				+ ", gender=" + gender + ", emailId=" + emailId + ", phoneNumber=" + phoneNumber + ", isActive="
				+ isActive + ", createdDtm=" + createdDtm + ", updatedDtm=" + updatedDtm + ", address=" + address
				+ ", roles=" + roles + ", departments=" + departments + "]";
	}
	
}
