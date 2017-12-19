package com.poc.trainingmanager.model;

import java.util.Set;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import com.datastax.driver.core.DataType;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;

@Table("department_roles")
public class DepartmentRoles {

	@PrimaryKeyColumn(name = "DEPARTMENT_ID", type = PrimaryKeyType.PARTITIONED)
	@Column("DEPARTMENT_ID")
	private UUID departmentId;

	@CassandraType(type = DataType.Name.UDT, userTypeName = "role")
	@Column("roles")
	private Set<RoleUdt> roles;

	public UUID getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(UUID departmentId) {
		this.departmentId = departmentId;
	}

	public Set<RoleUdt> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleUdt> roles) {
		this.roles = roles;
	}

	public DepartmentRoles(UUID departmentId, Set<RoleUdt> roles) {
		super();
		this.departmentId = departmentId;
		this.roles = roles;
	}

	public DepartmentRoles() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((departmentId == null) ? 0 : departmentId.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
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
		DepartmentRoles other = (DepartmentRoles) obj;
		if (departmentId == null) {
			if (other.departmentId != null)
				return false;
		} else if (!departmentId.equals(other.departmentId))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		return true;
	}

}
