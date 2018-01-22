package com.poc.test.trainingmanager.servicetest.userservicetest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.poc.trainingmanager.constants.Constants;
import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.DepartmentUsers;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.AddressUdt;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.DepartmentUsersRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.helper.UserInsertHelper;
import com.poc.trainingmanager.service.impl.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = { UserServiceImpl.class })
@ComponentScan
public class UserServiceInsertTest {

	@Mock
	UserRepository userMock;

	@Mock
	RoleRepository roleMock;

	@Mock
	DepartmentRepository departmentMock;

	@Autowired
	@InjectMocks
	UserServiceImpl userService;

	@Autowired
	@InjectMocks
	UserInsertHelper userInsertHelper;

	@Mock
	DepartmentUsersRepository departmentUsersMock;

	@Mock
	RoleUsersRepository roleUsersMock;

	@Mock
	UserInsertHelper userInsertHelperMock;

	@Test
	public void testInsert() {
		StandardResponse<User> standardResponse = new StandardResponse<User>();
		StandardResponse<User> s = new StandardResponse<User>();

		AddressUdt address = new AddressUdt("876", "juhj", "hgj", "hjgj", "uyufrd", "india", "567890");
		Date date = new Date();

		String roleId = "ab361ad0-df10-11e7-8dac-bb71b9eabcd5";
		Role rol = new Role();
		RoleUdt role = new RoleUdt(UUID.fromString(roleId), "Java Administrator", "skj", null, null, date, date);
		Set<RoleUdt> roles = new HashSet<RoleUdt>();
		roles.add(role);

		String departmentId = "07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5";
		DepartmentUdt department = new DepartmentUdt(UUID.fromString(departmentId), "Java", "Java Department", date,
				date);
		Department dept = new Department();
		Set<DepartmentUdt> departments = new HashSet<DepartmentUdt>();
		departments.add(department);

		String id = "de6d54a0-df0f-11e7-8dac-bb71b9eabdc6";
		User user = new User(UUID.fromString(id), "Spo", "Arun", "12345", "Female", "spo@g.c", "1234567890", true, date,
				date, address, roles, departments);

		Set<UserUdt> userSet = new HashSet<UserUdt>();
		userSet.add(WrapperUtil.userToUserUdt(user));

		DepartmentUsers departmentUsers = new DepartmentUsers(UUID.fromString(departmentId), userSet);
		RoleUsers roleUsers = new RoleUsers(UUID.fromString(roleId), userSet);

		System.out.println(userMock);

		when(userMock.save(user)).thenReturn(user);
		when(departmentMock.findByDepartmentId(UUID.fromString(departmentId))).thenReturn(dept);
		when(roleMock.findByRoleId(UUID.fromString(roleId))).thenReturn(rol);
		when(departmentUsersMock.findByDepartmentId(UUID.fromString(departmentId))).thenReturn(departmentUsers);
		when(roleUsersMock.findByRoleId(UUID.fromString(roleId))).thenReturn(roleUsers);
		when(departmentUsersMock.save(departmentUsers)).thenReturn(departmentUsers);
		when(roleUsersMock.save(roleUsers)).thenReturn(roleUsers);

		standardResponse.setCode(422);
		standardResponse.setStatus("Failed");
		standardResponse.setMessage("User cant be null, failed to add this user");

		s = userService.insert(null);
		assertEquals(standardResponse.getCode(), s.getCode());
		assertEquals(standardResponse.getStatus(), s.getStatus());

		standardResponse.setCode(200);
		standardResponse.setElement(user);
		standardResponse.setMessage("User added successfully");
		standardResponse.setStatus(Constants.SUCCESS);

		userInsertHelper.insertUser(user);
		userInsertHelper.insertIntoDepartmentUsers(user);
		userInsertHelper.insertIntoRoleUsers(user);

		s = userService.insert(user);
		assertEquals(standardResponse.getCode(), s.getCode());
		assertEquals(standardResponse.getStatus(), s.getStatus());
	}
}
