package com.poc.trainingmanager.service.helper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.DepartmentUsers;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentUsersRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;

@Service
public class UserInsertHelper {
	@Autowired
	DepartmentUsersRepository departmentUsersRepository;

	@Autowired
	RoleUsersRepository roleUsersRepository;

	@Autowired
	UserRepository userRepository;

	public void insertUser(User user){
		if(user != null){
			userRepository.save(user);
		}
	}
	
	public void insertIntoDepartmentUsers(User user) {
		DepartmentUsers departmentUsers = null;
		if (user.getDepartments() != null)
			departmentUsers = departmentUsersRepository
					.findByDepartmentId(user.getDepartments().iterator().next().getDepartmentId());
		if (departmentUsers != null) {
			Set<UserUdt> usersInDepartment = departmentUsers.getUserDepartmentsUdt();
			if (usersInDepartment == null) {
				usersInDepartment = new HashSet<UserUdt>();
			}

			usersInDepartment.add(WrapperUtil.userToUserUdt(user));
			departmentUsers.setUserDepartmentsUdt(usersInDepartment);
			departmentUsersRepository.save(departmentUsers);
		}
	}
	
	
	public void insertIntoRoleUsers(User user){
		/*
		 * setting the current user's userRolesUdt that is to be added to the set in
		 * RoleUsers fetch an entry from roleUsers table with the role id obtained from
		 * the user
		 */
		RoleUsers roleUsers = roleUsersRepository.findByRoleId(user.getRoles().iterator().next().getRoleId());
		// insert user only if the role exists
		if (roleUsers != null) { 
		Set<UserUdt> userList = new HashSet<UserUdt>();
		userList.add(WrapperUtil.userToUserUdt(user));
		// set the updated set of users belonging to that role
		roleUsers.setUserRolesUdt(userList);
		roleUsersRepository.save(roleUsers);
		}
	}
}
