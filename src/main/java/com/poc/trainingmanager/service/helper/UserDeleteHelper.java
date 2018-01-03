package com.poc.trainingmanager.service.helper;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.DepartmentUsers;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.repository.DepartmentUsersRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;

@Service
public class UserDeleteHelper {

	@Autowired
	DepartmentUsersRepository departmentUsersRepository;

	@Autowired
	RoleUsersRepository roleUsersRepository;

	@Autowired
	UserRepository userRepository;

	public void deleteFromDepartmentUsers(UUID departmentId, UserUdt userToBeDeleted) {
		DepartmentUsers departmentUsers = departmentUsersRepository.findByDepartmentId(departmentId);
		Set<UserUdt> users = departmentUsers.getUserDepartmentsUdt();
		if (users != null)
			users.remove(userToBeDeleted);
		departmentUsersRepository.save(departmentUsers);
	}

	public void deleteFromRoleUsers(UUID roleId, UserUdt userToBeDeleted) {
		RoleUsers roleUsers = roleUsersRepository.findByRoleId(roleId);
		Set<UserUdt> users = roleUsers.getUserUdt();
		if (users != null)
			users.remove(userToBeDeleted);
		roleUsersRepository.save(roleUsers);
	}

	public void deactivateUser(User user) {
		user.setIsActive(false);
		userRepository.save(user);
	}

}
