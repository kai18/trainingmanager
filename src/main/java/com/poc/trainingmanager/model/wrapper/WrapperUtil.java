package com.poc.trainingmanager.model.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;

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
	
	public static DepartmentUdt departmentToDepartmentUdt(Department department) {
		DepartmentUdt departmentUdt = new DepartmentUdt();
		
		departmentUdt.setDepartmentId(department.getDepartmentId());
		departmentUdt.setDepartmentDescription(department.getDepartmentDescription());
		departmentUdt.setDepartmentName(department.getDepartmentName());
		departmentUdt.setDepartmentCreatedDtm(department.getDepartmentCreatedDtm());
		departmentUdt.setDepartmentUpdatedDtm(department.getDepartmentUpdatedDtm());
		
		return departmentUdt;
	}
	
}
