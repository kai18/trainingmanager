package com.poc.trainingmanager.model;

import java.util.Date;

import com.datastax.driver.core.utils.UUIDs;

/**
 * @author Kaustubh.Kaustubh
 *         <p>
 *         This is the POJO for the Role table. It is stores the user's role
 *         that can be system administrator or department administrator or a
 *         normal user with only read privileges. This POJO also contains the
 *         user's privilege information.
 * 
 *         </p>
 */
public class Role {

	private UUIDs roleId;
	private String roleName;
	private String roleType;
	private String roleDescription;
	private int creationPrevilege;
	private int readPrevilege;
	private int deletionPrevilege;
	private int updationPrevilege;
	private Date createdDtm;
	private Date updatedDtm;

	public Role() {
		super();
	}

	public Role(UUIDs roleId, String roleName, String roleType, String roleDescription, int creationPrevilege,
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

	public UUIDs getRoleId() {
		return roleId;
	}

	public void setRoleId(UUIDs roleId) {
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

}
