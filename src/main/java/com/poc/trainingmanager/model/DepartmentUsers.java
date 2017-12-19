package com.poc.trainingmanager.model;

import java.util.Set;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import com.poc.trainingmanager.model.cassandraudt.UserUdt;

@Table("department_users")
public class DepartmentUsers {

	@PrimaryKeyColumn(name = "DEPARTMENT_ID", type = PrimaryKeyType.PARTITIONED)
	@Column("DEPARTMENT_ID")
	private UUID departmentId;
	
	@Column("users")
	private Set<UserUdt> userUdt;

	public DepartmentUsers() {}
	public DepartmentUsers(UUID departmentId, Set<UserUdt> userUdt) {
		super();
		this.departmentId = departmentId;
		this.userUdt = userUdt;
	}
	public UUID getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(UUID departmentId) {
		this.departmentId = departmentId;
	}
	public Set<UserUdt> getUserDepartmentsUdt() {
		return userUdt;
	}
	public void setUserUdt(Set<UserUdt> userUdt) {
		this.userUdt = userUdt;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((departmentId == null) ? 0 : departmentId.hashCode());
		result = prime * result + ((userUdt == null) ? 0 : userUdt.hashCode());
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
		DepartmentUsers other = (DepartmentUsers) obj;
		if (departmentId == null) {
			if (other.departmentId != null)
				return false;
		} else if (!departmentId.equals(other.departmentId))
			return false;
		if (userUdt == null) {
			if (other.userUdt != null)
				return false;
		} else if (!userUdt.equals(other.userUdt))
			return false;
		return true;
	}
}
