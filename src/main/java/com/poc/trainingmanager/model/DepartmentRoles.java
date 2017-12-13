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
	
	@CassandraType(type = DataType.Name.UDT, userTypeName="role")
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
	
	public DepartmentRoles() {}
}
