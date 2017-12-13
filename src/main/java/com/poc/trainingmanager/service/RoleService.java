package com.poc.trainingmanager.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;

@Service
public interface RoleService {

	public StandardResponse<List<Role>> getAllRoles(List<PrivilegeUdt> assignedPrivileges);

	public StandardResponse<Role> addRole(List<PrivilegeUdt> assignedPrivileges, Role role);

	public StandardResponse<Role> updateRole(List<PrivilegeUdt> assignedPrivileges, Role role);

	public StandardResponse deleteRole(List<PrivilegeUdt> assignedPrivileges, Role role);

	public StandardResponse<Role> getRoleByName(List<PrivilegeUdt> assignedPrivileges, String roleName);

}
