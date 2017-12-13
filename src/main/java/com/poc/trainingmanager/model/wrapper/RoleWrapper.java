package com.poc.trainingmanager.model.wrapper;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;

public class RoleWrapper {

	public RoleUdt roleToRoleUdt(Role role) {
		RoleUdt roleUdt = new RoleUdt();
		
		roleUdt.setRoleId(role.getRoleId());
		roleUdt.setRoleName(role.getRoleName());
		roleUdt.setRoleType(role.getRoleType());
		roleUdt.setRoleDescription(role.getRoleDescription());
		roleUdt.setPrivilege(role.getPrivilege());
		roleUdt.setCreatedDtm(role.getCreatedDtm());
		roleUdt.setUpdatedDtm(role.getUpdatedDtm());
		
		return roleUdt;
	}
}
