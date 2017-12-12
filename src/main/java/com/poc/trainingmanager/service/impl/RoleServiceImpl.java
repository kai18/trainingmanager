package com.poc.trainingmanager.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;

	@Override
	public StandardResponse<List<Role>> getAllRoles(List<PrivilegeUdt> assignedPrevileges) {
		StandardResponse<List<Role>> standardResponse = new StandardResponse<List<Role>>();
		List<Role> allRoles = roleRepository.findAll();
		if(allRoles == null)
		{
			return null;
		}
		standardResponse.setCode(200);
		standardResponse.setMessage("All roles fetched");
		standardResponse.setElement(allRoles);
		return standardResponse;
	}

	@Override
	public StandardResponse addRole(List<PrivilegeUdt> assignedPrevileges, Role role) {
		StandardResponse standardResponse = new StandardResponse();
		Role roleAdded = roleRepository.save(role);
		if(roleAdded == null)
		{
			standardResponse.setMessage("not inserted");
			return standardResponse;
		}
		standardResponse.setCode(201);
		standardResponse.setMessage("Role inserted successfully");
		standardResponse.setElement(roleAdded);
		return standardResponse;
	}

	@Override
	public StandardResponse updateRole(List<PrivilegeUdt> assignedPrevileges, Role role) {
		StandardResponse standardResponse = new StandardResponse();
		Role roleAdded = roleRepository.save(role);
		if(roleAdded == null)
		{
			standardResponse.setMessage("not updated");
			return standardResponse;
		}
		standardResponse.setCode(200);
		standardResponse.setMessage("Role updated successfully");
		standardResponse.setElement(roleAdded);
		return standardResponse;
	}

	@Override
	public StandardResponse deleteRole(List<PrivilegeUdt> assignedPrevileges, Role role) {
		StandardResponse standardResponse = new StandardResponse();
		roleRepository.delete(role);;
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
