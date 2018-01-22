package com.poc.test.trainingmanager.testdata;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.poc.test.trainingmanager.testdata.*;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;

public class RoleUsersData {

	public static RoleUsers roleUsers = new RoleUsers();
	
	public static void setRoleUsers() {
		SearchData userList = new SearchData();
		userList.setUp();
		roleUsers.setRoleId(RoleData.mockRole.getRoleId());
		Set<UserUdt> myset = new HashSet<UserUdt>();
		myset.add(WrapperUtil.userToUserUdt(SearchData.dbUsers.iterator().next()));
		roleUsers.setUserRolesUdt(myset);
	}
}
