package com.poc.trainingmanager.model.cassandraudt;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.UserDefinedType;

@UserDefinedType("role")
public class RoleUdt {

	@Column("role_id")
	private UUID roleId;
	
	@Column("role_name")
	private String roleName;
	
	@Column("role_type")
	private String roleType;
	
	@Column("role_description")
	private String roleDescription;
	
	@Column("creation")
	private int creationPrevilege;
	
	@Column("read")
	private int readPrevilege;
	
	@Column("deletion")
	private int deletionPrevilege;
	
	@Column("updation")
	private int updationPrevilege;
	
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

	public int getCreationPrevilege() {
		return creationPrevilege;
	}

	public void setCreationPrevilege(int creationPrevilege) {
		this.creationPrevilege = creationPrevilege;
	}

	public int getReadPrevilege() {
		return readPrevilege;
	}

	public void setReadPrevilege(int readPrevilege) {
		this.readPrevilege = readPrevilege;
	}

	public int getDeletionPrevilege() {
		return deletionPrevilege;
	}

	public void setDeletionPrevilege(int deletionPrevilege) {
		this.deletionPrevilege = deletionPrevilege;
	}

	public int getUpdationPrevilege() {
		return updationPrevilege;
	}

	public void setUpdationPrevilege(int updationPrevilege) {
		this.updationPrevilege = updationPrevilege;
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

	public RoleUdt(UUID roleId, String roleName, String roleType, String roleDescription, int creationPrevilege,
			int readPrevilege, int deletionPrevilege, int updationPrevilege, Date createdDtm, Date updatedDtm) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleType = roleType;
		this.roleDescription = roleDescription;
		this.creationPrevilege = creationPrevilege;
		this.readPrevilege = readPrevilege;
		this.deletionPrevilege = deletionPrevilege;
		this.updationPrevilege = updationPrevilege;
		this.createdDtm = createdDtm;
		this.updatedDtm = updatedDtm;
	}
	
}
