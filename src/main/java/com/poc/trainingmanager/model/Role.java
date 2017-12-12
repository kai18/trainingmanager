package com.poc.trainingmanager.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import com.poc.trainingmanager.model.cassandraudt.PrevilegeUdt;

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

	private PrevilegeUdt previlege;

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

	public PrevilegeUdt getPrevilege() {
		return previlege;
	}

	public void setPrevilege(PrevilegeUdt previlege) {
		this.previlege = previlege;
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

	public Role(UUID roleId, String roleName, String roleType, String roleDescription, PrevilegeUdt previlege,
			Date createdDtm, Date updatedDtm) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleType = roleType;
		this.roleDescription = roleDescription;
		this.previlege = previlege;
		this.createdDtm = createdDtm;
		this.updatedDtm = updatedDtm;
	}

	public Role() {}
}
