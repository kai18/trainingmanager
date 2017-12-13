package com.poc.trainingmanager.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.utils.UUIDs;
import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	DepartmentRolesRepository departmentRolesRepository;

	@Override
	public StandardResponse<List<Role>> getAllRoles(List<PrivilegeUdt> assignedPrevileges) {
		StandardResponse<List<Role>> standardResponse = new StandardResponse<List<Role>>();
		List<Role> allRoles = roleRepository.findAll();
		if(allRoles == null)
		{
			return null;
		}
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("All roles fetched");
		standardResponse.setElement(allRoles);
		return standardResponse;
	}

	@Override
	public StandardResponse<Role> addRole(List<PrivilegeUdt> assignedPrevileges, Role role) {
		StandardResponse<Role> standardResponse = new StandardResponse<Role>();
		Date date = new Date();
		role.setRoleId(UUID.randomUUID());
		role.setCreatedDtm(date);
		role.setUpdatedDtm(date);
		Role roleAdded = roleRepository.save(role);
		
		DepartmentRoles departmentRoles = departmentRolesRepository.findByDepartmentId(role.getPrivilege().getDepartment_id());
		Set<Role> roleList = departmentRoles.getRoles();
		roleList.add(roleAdded);
		departmentRoles.setRoles(roleList);
		DepartmentRoles departmentRolesAdded = departmentRolesRepository.save(departmentRoles);
		if(roleAdded == null && departmentRolesAdded == null)
		{
			standardResponse.setMessage("Role not inserted");
			return standardResponse;
		}
		standardResponse.setCode(201);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Role inserted successfully");
		standardResponse.setElement(roleAdded);
		return standardResponse;
	}

	@Override
	public StandardResponse<Role> updateRole(List<PrivilegeUdt> assignedPrevileges, Role role) {
		StandardResponse<Role> standardResponse = new StandardResponse<Role>();
		Role roleUpdated = roleRepository.save(role);
		if(roleUpdated == null)
		{
			standardResponse.setMessage("Role not updated");
			return standardResponse;
		}
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Role updated successfully");
		standardResponse.setElement(roleUpdated);
		return standardResponse;
	}

	@Override
	public StandardResponse deleteRole(List<PrivilegeUdt> assignedPrevileges, Role role) {
		StandardResponse standardResponse = new StandardResponse();
		roleRepository.delete(role);
		standardResponse.setCode(202);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Role deleted successfully");
		return null;
	}

	@Override
	public StandardResponse<Role> getRoleByName(List<PrivilegeUdt> assignedPrevileges, String roleName) {
		StandardResponse<Role> standardResponse = new StandardResponse<Role>();
		Role role = roleRepository.findByRoleName(roleName);
		if(role == null)
		{
			return null;
		}
		standardResponse.setCode(200);
		standardResponse.setMessage("All roles fetched");
		standardResponse.setElement(role);
		return standardResponse;
	}
}
