package com.poc.trainingmanager.service;

import java.util.List;
import java.util.UUID;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrevilegeUdt;

public interface RoleService {

	public StandardResponse getAllRoles(List<PrevilegeUdt> assignedPrevileges);

	//public StandardResponse getRole(int roleId);

	public StandardResponse addRole(List<PrevilegeUdt> assignedPrevileges, Role role);

	public StandardResponse updateRole(List<PrevilegeUdt> assignedPrevileges, Role role);

	public StandardResponse deleteRole(List<PrevilegeUdt> assignedPrevileges, UUID roleId);

	public StandardResponse getRoleByName(List<PrevilegeUdt> assignedPrevileges, String roleName);

}
