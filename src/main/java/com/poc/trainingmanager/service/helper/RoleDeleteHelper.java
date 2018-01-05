package com.poc.trainingmanager.service.helper;

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
public class RoleDeleteHelper {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	DepartmentRolesRepository departmentRolesRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleUsersRepository roleUsersRepository;

	public void deleteFromRole(Role role) {
		// delete from role table
		roleRepository.delete(role);
	}

	public void deleteFromDepartmentRoles(UUID departmentId, Role role) {
		DepartmentRoles departmentRoles = departmentRolesRepository
				.findByDepartmentId(departmentId);
		Set<RoleUdt> roleList = departmentRoles.getRoles();

		roleList.remove(WrapperUtil.roleToRoleUdt(role));
		departmentRoles.setRoles(roleList);
		// update the departmentRoles table
		departmentRolesRepository.save(departmentRoles);
	}

	public void deleteFromRoleUsers(Role role) {
		RoleUsers roleUsers = roleUsersRepository.findByRoleId(role.getRoleId());
		// if roleUsers entry found, then delete from it
		if (roleUsers != null) {
			roleUsersRepository.delete(roleUsers);
			// fetch the set of users belonging to this role
			Set<UserUdt> userList = roleUsers.getUserUdt();
			/*
			 * if there are users associated to the role, then remove this Role from each
			 * User entry.
			 */
			if (userList != null) {
				User user = new User();
				Set<RoleUdt> setOfUserRoles = new HashSet<RoleUdt>();
				UserUdt userUdt = new UserUdt();

				/*
				 * for each user, fetch the roles present and delete this role and save back the
				 * updated user
				 */
				for (int i = 0; i < userList.size(); i++) {
					userUdt = userList.iterator().next();
					user = WrapperUtil.userUdtToUser(userUdt);
					setOfUserRoles = userUdt.getRoles();
					setOfUserRoles.remove(WrapperUtil.roleToRoleUdt(role));
					user.setRoles(setOfUserRoles);
					userRepository.save(user);
				}
			}
		}
	}
}
