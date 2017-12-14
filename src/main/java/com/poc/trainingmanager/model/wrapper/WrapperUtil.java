package com.poc.trainingmanager.model.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.User;
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
	
	public static User wrappedUserToUser(User user, User oldUser) {
		oldUser.setFirstName(user.getFirstName());
		oldUser.setLastName(user.getLastName());
		oldUser.setPhoneNumber(user.getPhoneNumber());
		oldUser.setAddress(user.getAddress());
		
		return oldUser;
	}
}
