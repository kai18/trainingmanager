package com.poc.test.trainingmanager.servicetest.userservicetest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.poc.trainingmanager.model.DepartmentUsers;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.DepartmentUsersRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.impl.UserServiceImpl;
import com.poc.trainingmanager.utils.PrivilegeChecker;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = { UserServiceImpl.class })
@ComponentScan
public class UserGrantRoleTest {

	@Mock
	RoleRepository roleRepositoryMock;

	@Mock
	RoleUsersRepository roleUsersMock;

	@Mock
	DepartmentUsersRepository departmentUsersMock;

	@Mock
	PrivilegeChecker privilegeCheckerMock;

	@Mock
	LoggedInUserWrapper loggedinuser;

	@Mock
	DepartmentRepository departmentRepositoryMock;

	@Mock
	UserRepository userRepositoryMock;

	@Autowired
	@InjectMocks
	UserServiceImpl userserviceimpl;

	List<RoleUdt> roles;
	List<DepartmentUdt> departments;
	List<User> users;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		roles = new ArrayList<RoleUdt>();
		departments = new ArrayList<DepartmentUdt>();
		users = new ArrayList<User>();

		DepartmentUdt angularDepartment = mock(DepartmentUdt.class);
		DepartmentUdt javaDepartment = mock(DepartmentUdt.class);
		DepartmentUdt dotNetDepartment = mock(DepartmentUdt.class);

		departments.addAll(Arrays.asList(angularDepartment, javaDepartment, dotNetDepartment));

		RoleUdt admin = mock(RoleUdt.class);
		RoleUdt student = mock(RoleUdt.class);
		RoleUdt departmentAdmin = mock(RoleUdt.class);

		roles.addAll(Arrays.asList(admin, student, departmentAdmin));

		User userbeforegrantrole = new User();
		User useraftergrantrole = new User();

		userbeforegrantrole.setFirstName("john");
		userbeforegrantrole.setLastName("doe");
		userbeforegrantrole.setRoles(new HashSet<RoleUdt>(Arrays.asList(student)));
		userbeforegrantrole.setDepartments(new HashSet<DepartmentUdt>(Arrays.asList(angularDepartment)));

		useraftergrantrole.setFirstName("john");
		useraftergrantrole.setLastName("doe");
		useraftergrantrole.setRoles(new HashSet<RoleUdt>(Arrays.asList(student)));
		useraftergrantrole.setRoles(new HashSet<RoleUdt>(Arrays.asList(admin)));
		useraftergrantrole.setDepartments(new HashSet<DepartmentUdt>(Arrays.asList(angularDepartment)));
		users.addAll(Arrays.asList(userbeforegrantrole, useraftergrantrole));
	}

	@Test
	public void grantRoleTest() {
		String roleId = "af33f034-a76c-4b6a-9e05-49e37d1a2e6e";
		String departmentId = "10bd8c60-7497-45e5-83e5-8ac0383d6e30";

		RoleUsers testRoleUser = new RoleUsers();
		testRoleUser.setRoleId(UUID.fromString(roleId));
		testRoleUser.setUserRolesUdt(new HashSet<UserUdt>(Arrays.asList(WrapperUtil.userToUserUdt(users.get(0)))));

		DepartmentUsers testDepartmentUser = new DepartmentUsers();
		testDepartmentUser.setDepartmentId(UUID.fromString(departmentId));
		testDepartmentUser
				.setUserDepartmentsUdt(new HashSet<UserUdt>(Arrays.asList(WrapperUtil.userToUserUdt(users.get(1)))));

		when(privilegeCheckerMock.isAllowedToEditRole(loggedinuser.getPrivileges())).thenReturn(true);
		when(departmentUsersMock.findByDepartmentId(UUID.fromString(departmentId))).thenReturn(testDepartmentUser);
		when(roleUsersMock.findByRoleId(UUID.fromString(roleId))).thenReturn(testRoleUser);
		when(userRepositoryMock.findById(UUID.fromString(roleId))).thenReturn(users.get(0));

		Role adminRole = new Role();
		BeanUtils.copyProperties(roles.get(0), adminRole);
		when(roleRepositoryMock.findByRoleId(UUID.fromString(roleId))).thenReturn(adminRole);

		userserviceimpl.grantRole(roleId, departmentId, new LoggedInUserWrapper());

		assertThat(users.get(0)).isEqualTo(users.get(1));
	}

}
