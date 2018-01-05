package com.poc.trainingmanager.service.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.RoleService;
import com.poc.trainingmanager.service.helper.RoleDeleteHelper;
import com.poc.trainingmanager.service.helper.RoleInsertHelper;
import com.poc.trainingmanager.service.helper.RoleUpdateHelper;

@CrossOrigin()
@Service
public class RoleServiceImpl implements RoleService {

	private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
	@Autowired
	RoleInsertHelper roleInsertHelper;

	@Autowired
	RoleDeleteHelper roleDeleteHelper;

	@Autowired
	RoleUpdateHelper roleUpdateHelper;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	DepartmentRolesRepository departmentRolesRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleUsersRepository roleUsersRepository;

	@CrossOrigin()
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

		// insert into Role table
		Role roleInserted = roleInsertHelper.insertIntoRole(role);
		logger.info("Role {" + role + "} successfully added");

		// insert into Role_Users table
		roleInsertHelper.insertIntoRoleUsers(roleInserted);
		logger.info("Role {" + role + "} successfully added to Role_Users");

		// insert into Department_Roles table if it is a department type role
		if (role.getPrivilege().getDepartment_id() != null) {
			roleInsertHelper.insertIntoDepartmentRoles(role.getPrivilege().getDepartment_id(), roleInserted);
			logger.info("DepartmentRoles successfully updated with the new role");
		}

		if (roleInserted == null) {
			logger.error("Role {" + role + "} insertion failed");
			standardResponse.setMessage("Role not inserted");
			return standardResponse;
		}

		standardResponse.setCode(201);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Role inserted successfully");
		standardResponse.setElement(roleInserted);
		return standardResponse;
	}

	@Override
	public StandardResponse<Role> updateRole(List<PrivilegeUdt> assignedPrevileges, Role role) {
		StandardResponse<Role> standardResponse = new StandardResponse<Role>();
		// fetch the old role from role table
		Role oldRole = roleRepository.findByRoleId(role.getRoleId());

		// update the role in role table
		Role updatedRole = roleUpdateHelper.updateRole(oldRole, role);
		logger.info("Role {" + updatedRole + "} successfully updated");

		UUID departmentId = updatedRole.getPrivilege().getDepartment_id();
		// update the role in the departmentRoles table
		if (departmentId != null) {
			roleUpdateHelper.updateDepartmentRoles(departmentId, oldRole, updatedRole);
			logger.info("Role in DepartmentRoles successfully updated");
		}
		
		// update the role in user who had this role
		roleUpdateHelper.updateRoleUsers(oldRole, updatedRole);
		logger.info("Role in RoleUsers successfully updated");

		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Role updated successfully");
		standardResponse.setElement(updatedRole);
		return standardResponse;
	}

	@Override
	public StandardResponse<Role> deleteRole(List<PrivilegeUdt> assignedPrevileges, String roleId) {
		StandardResponse<Role> standardResponse = new StandardResponse<Role>();
		Role role = roleRepository.findByRoleId(UUID.fromString(roleId));
		if (role == null) {
			logger.error("Role delete failed, Role is null");
			standardResponse.setCode(404);
			standardResponse.setStatus("Failed");
			standardResponse.setMessage("No such role found to delete");
			return standardResponse;
		}

		// delete from Role table
		roleDeleteHelper.deleteFromRole(role);
		logger.info("Role {" + role + "} successfully deleted");

		// remove the role from departmentRoles table
		if (role.getPrivilege().getDepartment_id() != null) {
			roleDeleteHelper.deleteFromDepartmentRoles(role.getPrivilege().getDepartment_id(), role);
			logger.info("Role removed from DepartmentRoles");
		}

		// to delete from the roleUsers table
		roleDeleteHelper.deleteFromRoleUsers(role);
		logger.info("Role removed from RoleUsers and each User");

		standardResponse.setCode(202);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Role deleted successfully");
		return standardResponse;
	}

	@Override
	public StandardResponse<List<RoleUsers>> getAllRoleUsers(List<PrivilegeUdt> assignedPrivileges) {
		
		List<RoleUsers> allRoleUsers = roleUsersRepository.findAll();
		StandardResponse<List<RoleUsers>> standardResponse = new StandardResponse<List<RoleUsers>>();
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setElement(allRoleUsers);
		standardResponse.setMessage("RoleUsers fetched successfully");
		return standardResponse;
	}

	@Override
	public StandardResponse<List<DepartmentRoles>> getAllDepartmentRoles(List<PrivilegeUdt> assignedPrivileges) {
		StandardResponse<List<DepartmentRoles>> standardResponse = new StandardResponse<List<DepartmentRoles>>();
		List<DepartmentRoles> allDepartmentRoles = departmentRolesRepository.findAll();
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setElement(allDepartmentRoles);
		standardResponse.setMessage("DepartmentRoles fetched successfully");
		return standardResponse;
	}
}
