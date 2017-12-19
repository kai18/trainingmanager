package com.poc.trainingmanager.model.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;

public class WrapperUtil {

	public static UserSearchWrapper wrapUserToUserSearchWrapper(User user) {

		if (user != null) {
			UserSearchWrapper userSearchWrapper = new UserSearchWrapper();
			BeanUtils.copyProperties(user, userSearchWrapper);
			return userSearchWrapper;
		}

		return null;
	}

	public static List<UserSearchWrapper> wrapUserToUserSearchWrapper(List<User> users) {

		List<UserSearchWrapper> userList = new ArrayList<UserSearchWrapper>();
		for (User user : users) {
			if (user != null) {
				UserSearchWrapper userSearchWrapper = new UserSearchWrapper();
				BeanUtils.copyProperties(user, userSearchWrapper);
				userList.add(userSearchWrapper);
			}
		}

		return userList;
	}

	public static List<DepartmentUdt> departmentToDepartmentUdt(List<Department> departmentList) {
		List<DepartmentUdt> departmentUdtList = new ArrayList<DepartmentUdt>();

		for (Department department : departmentList) {
			DepartmentUdt departmentUdt = new DepartmentUdt();
			BeanUtils.copyProperties(department, departmentUdt);
			departmentUdtList.add(departmentUdt);
		}
		return departmentUdtList;
	}

	public static RoleUdt roleToRoleUdt(Role role) {
		RoleUdt roleUdt = new RoleUdt();

		roleUdt.setRoleId(role.getRoleId());
		roleUdt.setRoleName(role.getRoleName());
		roleUdt.setRoleType(role.getRoleType());
		roleUdt.setRoleDescription(role.getRoleDescription());
		roleUdt.setPrivilege(role.getPrivilege());
		roleUdt.setCreatedDtm(role.getCreatedDtm());
		roleUdt.setUpdatedDtm(role.getUpdatedDtm());

		return roleUdt;
	}

	public static List<RoleUdt> roleToRoleUdt(List<Role> roleList) {

		List<RoleUdt> roleUdtList = new ArrayList<RoleUdt>();
		if (roleList != null && !roleList.isEmpty())
			for (Role role : roleList) {

				RoleUdt roleUdt = new RoleUdt();
				roleUdt.setRoleId(role.getRoleId());
				roleUdt.setRoleName(role.getRoleName());
				roleUdt.setRoleType(role.getRoleType());
				roleUdt.setRoleDescription(role.getRoleDescription());
				roleUdt.setPrivilege(role.getPrivilege());
				roleUdt.setCreatedDtm(role.getCreatedDtm());
				roleUdt.setUpdatedDtm(role.getUpdatedDtm());

				roleUdtList.add(roleUdt);

			}
		return roleUdtList;
	}

	public static User userUdtToUser(UserUdt userUdt) {
		User user = new User();
		user.setId(userUdt.getId());
		user.setFirstName(userUdt.getFirstName());
		user.setLastName(userUdt.getLastName());
		user.setEmailId(userUdt.getEmailId());
		user.setPassword(userUdt.getPassword());
		user.setPhoneNumber(userUdt.getPhoneNumber());
		user.setIsActive(userUdt.getIsActive());
		user.setAddress(userUdt.getAddress());
		user.setGender(userUdt.getGender());
		user.setDepartments(userUdt.getDepartments());
		user.setRoles(userUdt.getRoles());
		user.setCreatedDtm(userUdt.getCreatedDtm());
		user.setUpdatedDtm(userUdt.getUpdatedDtm());

		return user;
	}

	public static User wrappedUserToUser(User user, User oldUser) {
		oldUser.setFirstName(user.getFirstName());
		oldUser.setLastName(user.getLastName());
		oldUser.setPhoneNumber(user.getPhoneNumber());
		oldUser.setAddress(user.getAddress());
		oldUser.setUpdatedDtm(user.getUpdatedDtm());

		return oldUser;
	}

	public static UserUdt userToUserUdt(User user) {
		UserUdt userUdt = new UserUdt();
		BeanUtils.copyProperties(user, userUdt);
		return userUdt;
	}
}
