package com.poc.trainingmanager.model;

import java.util.Set;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import com.poc.trainingmanager.model.cassandraudt.UserUdt;

@Table("role_users")
public class RoleUsers {


	@PrimaryKeyColumn(name = "role_id", type = PrimaryKeyType.PARTITIONED)
	@Column("role_id")
	private UUID roleId;
	
	@Column("users")
	private Set<UserUdt> userUdt;

	public RoleUsers() {}
	public RoleUsers(UUID roleId, Set<UserUdt> userRolesUdt) {
		super();
		this.roleId = roleId;
		this.userUdt = userRolesUdt;
	}
	public UUID getRoleId() {
		return roleId;
	}
	public void setRoleId(UUID roleId) {
		this.roleId = roleId;
	}
	public Set<UserUdt> getUserUdt() {
		return userUdt;
	}
	public void setUserRolesUdt(Set<UserUdt> userRolesUdt) {
		this.userUdt = userRolesUdt;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
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
		RoleUsers other = (RoleUsers) obj;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (userUdt == null) {
			if (other.userUdt != null)
				return false;
		} else if (!userUdt.equals(other.userUdt))
			return false;
		return true;
	}
}
