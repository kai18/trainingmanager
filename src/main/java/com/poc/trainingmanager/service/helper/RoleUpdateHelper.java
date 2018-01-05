package com.poc.trainingmanager.service.helper;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;

@Service
public class RoleUpdateHelper {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	DepartmentRolesRepository departmentRolesRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleUsersRepository roleUsersRepository;

	public Role updateRole(Role oldRole, Role newRole) {
		Date date = new Date();

		Role updatedRole = oldRole;
		// set the updated description and updatedDtm
		updatedRole.setRoleDescription(newRole.getRoleDescription());
		updatedRole.setUpdatedDtm(date);
		// update the role in role table
		return roleRepository.save(updatedRole);

	}

	public void updateDepartmentRoles(UUID departmentId, Role oldRole, Role updatedRole) {
		RoleUdt oldRoleUdt = WrapperUtil.roleToRoleUdt(oldRole);
		// fetch the departmentRoles entry that has this role
		DepartmentRoles departmentRoles = departmentRolesRepository.findByDepartmentId(departmentId);
		// extract the roles that the department has
		Set<RoleUdt> roleList = departmentRoles.getRoles();
		// remove the old role from the list of roles from the departmentRoles table
		roleList.remove(oldRoleUdt);
		// add the new role to it
		roleList.add(WrapperUtil.roleToRoleUdt(updatedRole));
		// set the new list of roles to the departmentRoles table
		departmentRoles.setRoles(roleList);
		departmentRolesRepository.save(departmentRoles);
	}

	public void updateRoleUsers(Role oldRole, Role role) {
		User user = new User();
		Set<RoleUdt> roleList = new HashSet<RoleUdt>();
		Set<UserUdt> userList = new HashSet<UserUdt>();
		Set<UserUdt> updatedUserList = new HashSet<UserUdt>();
		RoleUsers roleUsers = new RoleUsers(); // roleId and set<id,set<roles>>
		roleUsers = roleUsersRepository.findByRoleId(role.getRoleId());
		if (roleUsers != null) {
			// set of users belonging to this role
			userList = roleUsers.getUserUdt();

			// if users exist in the role
			if (userList != null && !userList.isEmpty()) {
				// for each user, role has to be updated
				for (UserUdt userUdt : userList) {
					user = userRepository.findById(userUdt.getId());

					// get all the roles of that user
					roleList = userUdt.getRoles();

					// remove the entry with this old user in roleUsers table
					// userList.remove(userRolesUdt);//check this

					// remove the old role in user table entry
					roleList.remove(WrapperUtil.roleToRoleUdt(oldRole));

					// add the updated role in user table entry
					roleList.add(WrapperUtil.roleToRoleUdt(role));

					// set the new roles list to userUdt
					userUdt.setRoles(roleList);
					user.setRoles(roleList);
					userRepository.save(user);

					// update the user's role in the userRoles of roleUsers table
					updatedUserList.add(userUdt);
				}

				// updated roleUsers table entry
				roleUsers.setUserRolesUdt(updatedUserList);
				roleUsersRepository.save(roleUsers);
			}
		}
	}
}