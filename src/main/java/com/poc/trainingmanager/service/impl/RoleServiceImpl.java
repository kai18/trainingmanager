package com.poc.trainingmanager.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	DepartmentRolesRepository departmentRolesRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public StandardResponse<List<Role>> getAllRoles(List<PrivilegeUdt> assignedPrevileges) {
		StandardResponse<List<Role>> standardResponse = new StandardResponse<List<Role>>();
		List<Role> allRoles = roleRepository.findAll();
		if (allRoles == null) {
			logger.warn("No role found");
			standardResponse.setCode(404);
			standardResponse.setStatus("Success");
			standardResponse.setMessage("No role found");
			return standardResponse;
		}
		logger.info("All roles fetched successfully");
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("All roles fetched");
		standardResponse.setElement(allRoles);
		return standardResponse;
	}

	@Override
	public StandardResponse<Role> getRoleByName(List<PrivilegeUdt> assignedPrevileges, String roleName) {
		StandardResponse<Role> standardResponse = new StandardResponse<Role>();
		Role role = roleRepository.findByRoleName(roleName);
		if (role == null) {
			logger.warn("No role found with role name {" + roleName + "}");
			standardResponse.setCode(404);
			standardResponse.setStatus("Success");
			standardResponse.setMessage("No role found with Role name -" + roleName);
			return standardResponse;
		}
		logger.info("Role {" + role + "} fetched with role name {" + roleName + "}");
		standardResponse.setCode(200);
		standardResponse.setMessage("All roles fetched");
		standardResponse.setElement(role);
		return standardResponse;
	}

	@Override
	public StandardResponse<Role> addRole(List<PrivilegeUdt> assignedPrevileges, Role role) {
		StandardResponse<Role> standardResponse = new StandardResponse<Role>();
		Date date = new Date();

		if (role == null) {
			logger.error("Inserted role was null, hence failed to Add role");
			standardResponse.setCode(422);
			standardResponse.setStatus("Failed");
			standardResponse.setMessage("Role cant by null, failed to add this role");
			return standardResponse;
		}
		if (roleRepository.findByRoleName(role.getRoleName()) != null) {
			logger.error("Role {" + role + "} already exists, duplicate role cannot be inserted");
			standardResponse.setCode(409);
			standardResponse.setStatus("Failed");
			standardResponse.setMessage("Role already exists, failed to add this role");
			return standardResponse;
		}

		role.setRoleId(UUID.randomUUID());
		role.setCreatedDtm(date);
		role.setUpdatedDtm(date);
		Role roleAdded = roleRepository.save(role);

		DepartmentRoles departmentRoles = departmentRolesRepository
				.findByDepartmentId(role.getPrivilege().getDepartment_id());
		Set<RoleUdt> roleList = departmentRoles.getRoles();
		roleList.add(WrapperUtil.roleToRoleUdt(roleAdded));
		departmentRoles.setRoles(roleList);
		DepartmentRoles departmentRolesAdded = departmentRolesRepository.save(departmentRoles);

		if (roleAdded == null || departmentRolesAdded == null) {
			logger.error("Role {" + role + "} insertion failed");
			standardResponse.setMessage("Role not inserted");
			return standardResponse;
		}
		logger.info("Role {" + role + "} successfully added");
		logger.info("DepartmentRoles {" + departmentRolesAdded + "} successfully updated with the new role");
		standardResponse.setCode(201);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Role inserted successfully");
		standardResponse.setElement(roleAdded);
		return standardResponse;
	}

	@Override
	public StandardResponse<Role> updateRole(List<PrivilegeUdt> assignedPrevileges, Role role) {
		Date date = new Date();
		StandardResponse<Role> standardResponse = new StandardResponse<Role>();
		Role oldRole = roleRepository.findByRoleId(role.getRoleId());
		oldRole.setRoleDescription(role.getRoleDescription());
		oldRole.setUpdatedDtm(date);
		RoleUdt oldRoleUdt = WrapperUtil.roleToRoleUdt(oldRole);
		DepartmentRoles departmentRoles = departmentRolesRepository
				.findByDepartmentId(role.getPrivilege().getDepartment_id());
		Set<RoleUdt> roleList = departmentRoles.getRoles();
		roleList.remove(oldRoleUdt);
		roleList.add(WrapperUtil.roleToRoleUdt(role));
		departmentRoles.setRoles(roleList);
		DepartmentRoles departmentRolesAdded = departmentRolesRepository.save(departmentRoles);
		Role roleUpdated = roleRepository.save(oldRole);
		if (roleUpdated == null || departmentRolesAdded == null) {
			standardResponse.setCode(409);
			standardResponse.setMessage("Failed");
			standardResponse.setMessage("Role not updated");
			return standardResponse;
		}

		/*Set<RoleUdt> roleUdtList;
		User user = new User();
		List<User> userList = userRepository.findByRoles(WrapperUtil.roleToRoleUdt(role));

		for (int i = 0; i < userList.size(); i++) {
			user = userList.get(i);
			roleUdtList = user.getRoles();
			if (roleUdtList.contains(oldRoleUdt)) {
				roleUdtList.remove(oldRoleUdt);
				roleUdtList.add(WrapperUtil.roleToRoleUdt(role));
				// userList.remove(user);
				user.setRoles(roleUdtList);
				// userList.add(i, user);

				userRepository.save(user);
			}
		}*/

		logger.info("Role {" + role + "} successfully updated");
		logger.info("Role in DepartmentRoles {" + departmentRolesAdded + "} successfully updated");

		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Role updated successfully");
		standardResponse.setElement(roleUpdated);
		return standardResponse;
	}

	@Override
	public StandardResponse<?> deleteRole(List<PrivilegeUdt> assignedPrevileges, Role role) {
		StandardResponse<?> standardResponse = new StandardResponse<Object>();
		DepartmentRoles departmentRoles = departmentRolesRepository
				.findByDepartmentId(role.getPrivilege().getDepartment_id());
		Set<RoleUdt> roleList = departmentRoles.getRoles();

		if (roleRepository.findByRoleId(role.getRoleId()) == null) {
			standardResponse.setCode(404);
			standardResponse.setStatus("Failed");
			standardResponse.setMessage("No such role found to delete");
			return standardResponse;
		}
		roleList.remove(WrapperUtil.roleToRoleUdt(role));
		departmentRoles.setRoles(roleList);

		departmentRolesRepository.save(departmentRoles);
		roleRepository.delete(role);

		/*Set<RoleUdt> roleUdtList;
		User user = new User();
		List<User> userList = userRepository.findByRoles(WrapperUtil.roleToRoleUdt(role));
		for (int i = 0; i < userList.size(); i++) {
			user = userList.get(i);
			roleUdtList = user.getRoles();
			roleUdtList.remove(WrapperUtil.roleToRoleUdt(role));
			// userList.remove(user);
			user.setRoles(roleUdtList);
			// userList.add(i, user);
			userRepository.save(user);
		}*/

		logger.info("Role {" + role + "} successfully deleted");
		logger.info("Deleted role was removed from DepartmentRoles {" + departmentRoles + "}");

		standardResponse.setCode(202);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Role deleted successfully");
		return standardResponse;
	}
}
