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
import org.springframework.web.bind.annotation.CrossOrigin;

import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.RoleService;

@CrossOrigin()
@Service
public class RoleServiceImpl implements RoleService {

	private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
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

		RoleUsers roleUsers = new RoleUsers();
		roleUsers.setRoleId(role.getRoleId());
		roleUsers.setUserRolesUdt(null);
		roleUsers = roleUsersRepository.save(roleUsers);

		// fetch the departmentRoles record with the departmentId of role
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
		// fetch the old role from role table
		Role oldRole = roleRepository.findByRoleId(role.getRoleId());
		RoleUdt oldRoleUdt = WrapperUtil.roleToRoleUdt(oldRole);
		Role newRole = oldRole;
		// set the updated description and updatedDtm
		newRole.setRoleDescription(role.getRoleDescription());
		newRole.setUpdatedDtm(date);
		// update the role in role table
		Role roleUpdated = roleRepository.save(newRole);

		// update the role in the departmentRoles table
		// fetch the departmentRoles entry that has this role
		DepartmentRoles departmentRoles = departmentRolesRepository
				.findByDepartmentId(role.getPrivilege().getDepartment_id());
		// extract the roles that the department has
		Set<RoleUdt> roleList = departmentRoles.getRoles();
		// remove the old role from the list of roles from the departmentRoles table
		roleList.remove(oldRoleUdt);
		// add the new role to it
		roleList.add(WrapperUtil.roleToRoleUdt(role));
		// set the new list of roles to the departmentRoles table
		departmentRoles.setRoles(roleList);
		DepartmentRoles departmentRolesAdded = departmentRolesRepository.save(departmentRoles);

		// update the role in user who had this role
		UserUdt userUdt = new UserUdt();
		User user = new User();
		UUID userId;
		Set<UserUdt> userList = new HashSet<UserUdt>();
		RoleUsers roleUsers = new RoleUsers(); // roleId and set<id,set<roles>>
		roleUsers = roleUsersRepository.findByRoleId(role.getRoleId());
		if (roleUsers != null) {
			userList = roleUsers.getUserUdt();
			// set of users belonging to this role

			// if users exist in a role
			if (userList != null) {
				// for each user, role has to be updated
				for (int i = 0; i < userList.size(); i++) {
					// get the id of user to find the whole original user object
					userUdt = userList.iterator().next();
					user = userRepository.findById(userUdt.getId());

					// get all the roles of that user
					roleList = userUdt.getRoles();

					// remove the entry with this old user in roleUsers table
					// userList.remove(userRolesUdt);//check this

					// remove the old role in user table entry
					roleList.remove(oldRoleUdt);

					// add the updated role in user table entry
					roleList.add(WrapperUtil.roleToRoleUdt(newRole));

					// set the new roles list to userUdt
					userUdt.setRoles(roleList);
					user.setRoles(roleList);
					userRepository.save(user);

					// update the user's role in the userRoles of roleUsers table
					userList.add(userUdt);
				}
				// updated roleUsers table entry
				roleUsers.setUserRolesUdt(userList);
				roleUsersRepository.save(roleUsers);
			}
			if (roleUpdated == null || departmentRolesAdded == null) {
				standardResponse.setCode(409);
				standardResponse.setMessage("Failed");
				standardResponse.setMessage("Role not updated");
				return standardResponse;
			}
		}

		logger.info("Role {" + role + "} successfully updated");
		logger.info("Role in DepartmentRoles {" + departmentRolesAdded + "} successfully updated");

		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Role updated successfully");
		standardResponse.setElement(role);
		return standardResponse;
	}

	@Override
	public StandardResponse<?> deleteRole(List<PrivilegeUdt> assignedPrevileges, Role role) {
		StandardResponse<?> standardResponse = new StandardResponse<Object>();
		// remove the role from departmentRoles table
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
		// update the departmentRoles table
		departmentRolesRepository.save(departmentRoles);
		// delete from role table
		roleRepository.delete(role);

		// to delete from the roleUsers table and then remove the role entry from user
		// table
		// fetch the roleUsers entry with the users belonging to that role
		RoleUsers roleUsers = roleUsersRepository.findByRoleId(role.getRoleId());
		// if roleUsers entry found, then delete form it
		if (roleUsers != null) {
			roleUsersRepository.delete(roleUsers);

			User user = new User();
			Set<RoleUdt> roleUdtList = new HashSet<RoleUdt>();
			UUID userId;
			UserUdt userUdt = new UserUdt();
			// fetch the set of users belonging to this role
			Set<UserUdt> userList = roleUsers.getUserUdt();

			// for each user, fetch the roles present and delete this role and save back the
			// updated user
			for (int i = 0; i < userList.size(); i++) {
				userUdt = userList.iterator().next();
				user = WrapperUtil.userUdtToUser(userUdt);
				roleUdtList = userUdt.getRoles();
				roleUdtList.remove(WrapperUtil.roleToRoleUdt(role));
				user.setRoles(roleUdtList);
				userRepository.save(user);
			}
		}
		logger.info("Role {" + role + "} successfully deleted");
		logger.info("Deleted role was removed from DepartmentRoles {" + departmentRoles + "}");

		standardResponse.setCode(202);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Role deleted successfully");
		return standardResponse;
	}
}
