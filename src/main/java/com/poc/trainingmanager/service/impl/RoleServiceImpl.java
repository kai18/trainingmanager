package com.poc.trainingmanager.service.impl;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.poc.trainingmanager.constants.Constants;
import com.poc.trainingmanager.exceptions.DuplicateDataException;
import com.poc.trainingmanager.exceptions.InvalidRequestDataException;
import com.poc.trainingmanager.exceptions.ResourceNotFoundException;
import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.RoleService;
import com.poc.trainingmanager.service.helper.RoleDeleteHelper;
import com.poc.trainingmanager.service.helper.RoleInsertHelper;
import com.poc.trainingmanager.service.helper.RoleUpdateHelper;
import com.poc.trainingmanager.utils.PrivilegeChecker;

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

	@Autowired
	PrivilegeChecker privilegeChecker;

	@CrossOrigin()
	@Override
	public StandardResponse<List<Role>> getAllRoles() {
		StandardResponse<List<Role>> standardResponse = new StandardResponse<List<Role>>();
		List<Role> allRoles = roleRepository.findAll();
		if (allRoles == null) {
			throw new ResourceNotFoundException("No roles exist");
		}
		logger.info("All roles fetched successfully");
		standardResponse.setCode(200);
		standardResponse.setStatus(Constants.SUCCESS);
		standardResponse.setMessage("All roles fetched");
		standardResponse.setElement(allRoles);
		return standardResponse;
	}

	@Override
	public StandardResponse<Role> getRoleByName(String roleName) {
		StandardResponse<Role> standardResponse = new StandardResponse<Role>();
		Role role = roleRepository.findByRoleName(roleName);
		if (role == null) {
			throw new ResourceNotFoundException("Specified role " + roleName + " not found");
		}
		logger.info("Role {" + role + "} fetched with role name {" + roleName + "}");
		standardResponse.setCode(200);
		standardResponse.setMessage("All roles fetched");
		standardResponse.setStatus(Constants.SUCCESS);
		standardResponse.setElement(role);
		return standardResponse;
	}

	@Override
	public StandardResponse<Role> addRole(Role role, LoggedInUserWrapper loggedInUser) {

		privilegeChecker.isAllowedToCreateRole(loggedInUser.getPrivileges());

		StandardResponse<Role> standardResponse = new StandardResponse<Role>();

		if (role == null) {
			throw new InvalidRequestDataException("Invalid role passed in the request, check if all fields are okay");
		}
		if (roleRepository.findByRoleName(role.getRoleName()) != null) {
			throw new DuplicateDataException("Duplicate role, already exists");
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
		standardResponse.setStatus(Constants.SUCCESS);
		standardResponse.setMessage("Role inserted successfully");
		standardResponse.setElement(roleInserted);
		return standardResponse;
	}

	@Override
	public StandardResponse<Role> updateRole(Role role, LoggedInUserWrapper loggedInUser) {

		privilegeChecker.isAllowedToEditRole(loggedInUser.getPrivileges());

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
		standardResponse.setStatus(Constants.SUCCESS);
		standardResponse.setMessage("Role updated successfully");
		standardResponse.setElement(updatedRole);
		return standardResponse;
	}

	@Override
	public StandardResponse<Role> deleteRole(String roleId, LoggedInUserWrapper loggedInUser) {

		privilegeChecker.isAllowedToEditRole(loggedInUser.getPrivileges());

		StandardResponse<Role> standardResponse = new StandardResponse<Role>();
		Role role = roleRepository.findByRoleId(UUID.fromString(roleId));
		if (role == null) {
			throw new ResourceNotFoundException("Role with id " + roleId + " does not exists");
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
		standardResponse.setStatus(Constants.SUCCESS);
		standardResponse.setMessage("Role deleted successfully");
		return standardResponse;
	}

	@Override
	public StandardResponse<List<RoleUsers>> getAllRoleUsers() {

		List<RoleUsers> allRoleUsers = roleUsersRepository.findAll();
		StandardResponse<List<RoleUsers>> standardResponse = new StandardResponse<List<RoleUsers>>();
		standardResponse.setCode(200);
		standardResponse.setStatus(Constants.SUCCESS);
		standardResponse.setElement(allRoleUsers);
		standardResponse.setMessage("RoleUsers fetched successfully");
		return standardResponse;
	}

	@Override
	public StandardResponse<List<DepartmentRoles>> getAllDepartmentRoles(List<PrivilegeUdt> assignedPrivileges) {
		StandardResponse<List<DepartmentRoles>> standardResponse = new StandardResponse<List<DepartmentRoles>>();
		List<DepartmentRoles> allDepartmentRoles = departmentRolesRepository.findAll();
		standardResponse.setCode(200);
		standardResponse.setStatus(Constants.SUCCESS);
		standardResponse.setElement(allDepartmentRoles);
		standardResponse.setMessage("DepartmentRoles fetched successfully");
		return standardResponse;
	}

	@Override
	public StandardResponse<Set<RoleUdt>> getDepartmentRoles(UUID departmentId) {
		Set<RoleUdt> departments = departmentRolesRepository.findByDepartmentId(departmentId).getRoles();
		if (departments == null) {
			throw new ResourceNotFoundException("No roles associated with this department");
		} else {
			StandardResponse<Set<RoleUdt>> departmentResponse = new StandardResponse<Set<RoleUdt>>();
			departmentResponse.setCode(200);
			departmentResponse.setElement(departments);
			departmentResponse.setMessage("successfuly fetched all departments");
			departmentResponse.setStatus(Constants.SUCCESS);
			return departmentResponse;
		}

	}
}
