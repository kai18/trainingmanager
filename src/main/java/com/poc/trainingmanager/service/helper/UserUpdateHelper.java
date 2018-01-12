package com.poc.trainingmanager.service.helper;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.DepartmentUsers;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentUsersRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;

@Service
public class UserUpdateHelper {

	@Autowired
	UserRepository userRepository;

	@Autowired
	DepartmentUsersRepository departmentUsersRepository;

	@Autowired
	RoleUsersRepository roleUsersRepository;

	public User updateUserInUser(User oldUser, User userToBeUpdated) {
		Date date = new Date();
		User tempUser = new User();
		BeanUtils.copyProperties(oldUser, tempUser);
		User newUser = WrapperUtil.wrappedUserToUser(userToBeUpdated, tempUser);
		newUser.setUpdatedDtm(date);
		return userRepository.save(newUser);
	}

	public void updateUserInDepartmentUsers(User oldUser, User newUser) {
		Set<DepartmentUdt> departmentSet = oldUser.getDepartments();
		if (departmentSet == null) {
			departmentSet = new HashSet<DepartmentUdt>();
		}
		Set<UserUdt> userSet = new HashSet<UserUdt>();
		for (DepartmentUdt department : departmentSet) {
			DepartmentUsers departmentUsers = departmentUsersRepository
					.findByDepartmentId(department.getDepartmentId());
			if (departmentUsers != null) {
				if (departmentUsers.getUserDepartmentsUdt() != null) {
					userSet = departmentUsers.getUserDepartmentsUdt();
					userSet = swapUserInASet(oldUser, newUser, userSet);
				} else {
					userSet.add(WrapperUtil.userToUserUdt(newUser));
				}
			} else {
				departmentUsers = new DepartmentUsers();
				departmentUsers.setDepartmentId(department.getDepartmentId());
				userSet.add(WrapperUtil.userToUserUdt(newUser));
				departmentUsers.setUserDepartmentsUdt(userSet);
			}
			departmentUsers.setUserDepartmentsUdt(userSet);
			departmentUsersRepository.save(departmentUsers);
		}
	}

	public void updateUserInRoleUsers(User oldUser, User newUser) {
		Set<RoleUdt> roleSet = oldUser.getRoles();
		if (roleSet == null) {
			roleSet = new HashSet<RoleUdt>();
		}
		Set<UserUdt> userSet = new HashSet<UserUdt>();
		for (RoleUdt role : roleSet) {
			RoleUsers roleUsers = roleUsersRepository.findByRoleId(role.getRoleId());
			if (roleUsers != null) {
				if (roleUsers.getUserUdt() != null) {
					userSet = roleUsers.getUserUdt();
					userSet = swapUserInASet(oldUser, newUser, userSet);
				} else {
					userSet.add(WrapperUtil.userToUserUdt(newUser));
				}
			} else {
				roleUsers = new RoleUsers();
				roleUsers.setRoleId(role.getRoleId());
				userSet.add(WrapperUtil.userToUserUdt(newUser));
				roleUsers.setUserRolesUdt(userSet);
			}
			roleUsers.setUserRolesUdt(userSet);
			roleUsersRepository.save(roleUsers);
		}
	}

	public Set<UserUdt> swapUserInASet(User oldUser, User newUser, Set<UserUdt> userSet) {
		Set<UserUdt> newUserSet = new HashSet<UserUdt>();
		if (userSet != null) {
			newUserSet = userSet;
			newUserSet.remove(WrapperUtil.userToUserUdt(oldUser));
			newUserSet.add(WrapperUtil.userToUserUdt(newUser));
			return newUserSet;
		}
		newUserSet.add(WrapperUtil.userToUserUdt(newUser));
		return newUserSet;
	}
}
