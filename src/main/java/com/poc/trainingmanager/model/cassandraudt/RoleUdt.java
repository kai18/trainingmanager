package com.poc.trainingmanager.model.cassandraudt;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.UserDefinedType;

import com.datastax.driver.core.DataType;

@UserDefinedType("role")
public class RoleUdt {

	@CassandraType(type = DataType.Name.UUID)
	@Column("role_id")
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

	public RoleUdt(UUID roleId, String roleName, String roleType, String roleDescription, PrivilegeUdt privilege,
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

	public RoleUdt() {}

	@Override
	public String toString() {
		return "RoleUdt [roleId=" + roleId + ", roleName=" + roleName + ", roleType=" + roleType + ", roleDescription="
				+ roleDescription + ", privilege=" + privilege + ", createdDtm=" + createdDtm + ", updatedDtm="
				+ updatedDtm + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdDtm == null) ? 0 : createdDtm.hashCode());
		result = prime * result + ((privilege == null) ? 0 : privilege.hashCode());
		result = prime * result + ((roleDescription == null) ? 0 : roleDescription.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
		result = prime * result + ((roleType == null) ? 0 : roleType.hashCode());
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
		RoleUdt other = (RoleUdt) obj;
		if (createdDtm == null) {
			if (other.createdDtm != null)
				return false;
		} else if (!createdDtm.equals(other.createdDtm))
			return false;
		if (privilege == null) {
			if (other.privilege != null)
				return false;
		} else if (!privilege.equals(other.privilege))
			return false;
		if (roleDescription == null) {
			if (other.roleDescription != null)
				return false;
		} else if (!roleDescription.equals(other.roleDescription))
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		if (roleType == null) {
			if (other.roleType != null)
				return false;
		} else if (!roleType.equals(other.roleType))
			return false;
		if (updatedDtm == null) {
			if (other.updatedDtm != null)
				return false;
		} else if (!updatedDtm.equals(other.updatedDtm))
			return false;
		return true;
	}
}
