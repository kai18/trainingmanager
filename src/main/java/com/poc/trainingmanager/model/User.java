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
import com.poc.trainingmanager.model.cassandraudt.AddressUdt;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
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

	@Column(value = "EMAIL")
	@Indexed
	private String emailId;

	@Column(value = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(value = "IS_ACTIVE")
	private boolean isActive;

	@Column(value = "CREATED_DTM")
	private Date createdDtm;

	@Column(value = "UPDATED_DTM")
	private Date updatedDtm;

	private AddressUdt address;

	@Indexed
	private Set<RoleUdt> roles;

	@Indexed
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

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean b) {
		this.isActive = b;
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

	public AddressUdt getAddress() {
		return address;
	}

	public void setAddress(AddressUdt address) {
		this.address = address;
	}

	public Set<RoleUdt> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleUdt> roles) {
		this.roles = roles;
	}

	public Set<DepartmentUdt> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<DepartmentUdt> departments) {
		this.departments = departments;
	}

	public User(UUID id, String firstName, String lastName, String password, String gender, String emailId,
			String phoneNumber, boolean isActive, Date createdDtm, Date updatedDtm, AddressUdt address, Set<RoleUdt> roles,
			Set<DepartmentUdt> departments) {
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

	public User() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", password=" + password
				+ ", gender=" + gender + ", emailId=" + emailId + ", phoneNumber=" + phoneNumber + ", isActive="
				+ isActive + ", createdDtm=" + createdDtm + ", updatedDtm=" + updatedDtm + ", address=" + address
				+ ", roles=" + roles + ", departments=" + departments + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((createdDtm == null) ? 0 : createdDtm.hashCode());
		result = prime * result + ((departments == null) ? 0 : departments.hashCode());
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (isActive ? 1231 : 1237);
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((updatedDtm == null) ? 0 : updatedDtm.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (createdDtm == null) {
			if (other.createdDtm != null)
				return false;
		} else if (!createdDtm.equals(other.createdDtm))
			return false;
		if (departments == null) {
			if (other.departments != null)
				return false;
		} else if (!departments.equals(other.departments))
			return false;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isActive != other.isActive)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (updatedDtm == null) {
			if (other.updatedDtm != null)
				return false;
		} else if (!updatedDtm.equals(other.updatedDtm))
			return false;
		return true;
	}
}
