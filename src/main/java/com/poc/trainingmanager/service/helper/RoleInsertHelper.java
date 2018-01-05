package com.poc.trainingmanager.service.helper;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;

@Service
public class RoleInsertHelper {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	DepartmentRolesRepository departmentRolesRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleUsersRepository roleUsersRepository;
	
	public Role insertIntoRole(Role role) {
		Date date = new Date();
		role.setRoleId(UUID.randomUUID());
		role.setCreatedDtm(date);
		role.setUpdatedDtm(date);
		return roleRepository.save(role);
	}
	
	public void insertIntoRoleUsers(Role role) {
		RoleUsers roleUsers = new RoleUsers();
		roleUsers.setRoleId(role.getRoleId());
		roleUsers.setUserRolesUdt(null);
		roleUsers = roleUsersRepository.save(roleUsers);
	}
	
	public void insertIntoDepartmentRoles(UUID departmentId, Role role) {
		// fetch the departmentRoles record with the departmentId of role
		DepartmentRoles departmentRoles = departmentRolesRepository
				.findByDepartmentId(departmentId);
		Set<RoleUdt> roleList = departmentRoles.getRoles();
		roleList.add(WrapperUtil.roleToRoleUdt(role));
		departmentRoles.setRoles(roleList);
		departmentRolesRepository.save(departmentRoles);
	}
}
