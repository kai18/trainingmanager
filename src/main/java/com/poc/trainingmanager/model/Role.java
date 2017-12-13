package com.poc.trainingmanager.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import com.datastax.driver.core.DataType;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;

@Table("role")
public class Role {

	@PrimaryKeyColumn(name = "role_id", type = PrimaryKeyType.PARTITIONED)
	private UUID roleId;

	@Column("role_name")
	private String roleName;

	@Column("role_type")
	private String roleType;

	@Column("role_description")
	private String roleDescription;

	@CassandraType(type = DataType.Name.UDT, userTypeName="privilege")
	private PrivilegeUdt privilege;

	@Column("created_dtm")
	private Date createdDtm;

	@Column("updated_dtm")
	private Date updatedDtm;

	public UUID getRoleId() {
		return roleId;
	}

	public void setRoleId(UUID roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public PrivilegeUdt getPrivilege() {
		return privilege;
	}

	public void setPrivilege(PrivilegeUdt privilege) {
		this.privilege = privilege;
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

	public Role(UUID roleId, String roleName, String roleType, String roleDescription, PrivilegeUdt privilege,
			Date createdDtm, Date updatedDtm) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleType = roleType;
		this.roleDescription = roleDescription;
		this.privilege = privilege;
		this.createdDtm = createdDtm;
		this.updatedDtm = updatedDtm;
	}

	public Role() {}
}
