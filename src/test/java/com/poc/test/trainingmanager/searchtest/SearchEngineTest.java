package com.poc.test.trainingmanager.searchtest;

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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
import com.poc.trainingmanager.search.SearchEngine;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SearchEngine.class })
@ComponentScan
public class SearchEngineTest {

	@MockBean
	UserRepository userRepository;

	@MockBean
	RoleUsersRepository roleUsersRepository;

	@MockBean
	DepartmentUsersRepository departmentUsersRepository;

	@Autowired
	@InjectMocks
	SearchEngine searchEngine;

	List<RoleUdt> dbRoles;
	List<DepartmentUdt> dbDepartments;
	List<User> dbUsers;
	List<User> expectedResult;

	@Before
	public void setUp() {
		System.out.println("Setting up SearchEngine Test");
		MockitoAnnotations.initMocks(this);

		dbRoles = new ArrayList<RoleUdt>();
		dbDepartments = new ArrayList<DepartmentUdt>();
		dbUsers = new ArrayList<User>();

		DepartmentUdt angularDepartment = mock(DepartmentUdt.class);
		DepartmentUdt javaDepartment = mock(DepartmentUdt.class);
		DepartmentUdt dotNetDepartment = mock(DepartmentUdt.class);

		dbDepartments.addAll(Arrays.asList(angularDepartment, javaDepartment, dotNetDepartment));

		RoleUdt admin = mock(RoleUdt.class);
		RoleUdt student = mock(RoleUdt.class);
		RoleUdt departmentAdmin = mock(RoleUdt.class);

		dbRoles.addAll(Arrays.asList(admin, student, departmentAdmin));

		User rahul = new User();
		User ajay = new User();
		User kshitij = new User();
		User nitin = new User();

		rahul.setFirstName("rahul");
		rahul.setLastName("negi");
		rahul.setRoles(new HashSet<RoleUdt>(Arrays.asList(admin)));
		rahul.setDepartments(new HashSet<DepartmentUdt>(Arrays.asList(angularDepartment)));
		rahul.setEmailId("rahul@rahul.com");
		rahul.setIsActive(true);

		ajay.setFirstName("ajay");
		ajay.setLastName("v");
		ajay.setIsActive(true);
		ajay.setRoles(new HashSet<RoleUdt>(Arrays.asList(student)));
		ajay.setDepartments(new HashSet<DepartmentUdt>(Arrays.asList(dotNetDepartment)));
		ajay.setEmailId("ajay@ajay.com");

		nitin.setFirstName("nitin");
		nitin.setLastName("srinivas");
		nitin.setIsActive(true);
		nitin.setRoles(new HashSet<RoleUdt>(Arrays.asList(departmentAdmin)));
		nitin.setDepartments(new HashSet<DepartmentUdt>(Arrays.asList(javaDepartment)));
		nitin.setEmailId("nitin@ajay.com");

		kshitij.setFirstName("kshitij");
		kshitij.setLastName("singh");
		kshitij.setIsActive(true);
		kshitij.setRoles(new HashSet<RoleUdt>(Arrays.asList(student)));
		kshitij.setDepartments(new HashSet<DepartmentUdt>(Arrays.asList(dotNetDepartment)));
		kshitij.setEmailId("nitin@ajay.com");

		dbUsers.addAll(Arrays.asList(rahul, nitin, ajay, kshitij));

		expectedResult = new ArrayList<User>();
		expectedResult.add(rahul);
		expectedResult.add(nitin);
		expectedResult.add(ajay);
		expectedResult.add(nitin);
		expectedResult.add(nitin);
		expectedResult.add(rahul);
		expectedResult.add(rahul);
	}

	@Test
	public void searchByEmailTest() {

		when(userRepository.findByEmailIdContainingIgnoreCase("kau"))
				.thenReturn(new ArrayList<User>(Arrays.asList(new User())));

		System.out.println(searchEngine);

		searchEngine.searchByPartialEmail("kau");
	}

	@Test
	public void searchByAllParameters() {
		String email = "j";
		String firstName = "kshitij";
		String lastName = "singh";
		String roleId = "af33f034-a76c-4b6a-9e05-49e37d1a2e6e";
		String departmentId = "10bd8c60-7497-45e5-83e5-8ac0383d6e30";

		RoleUsers testRoleUser = new RoleUsers();

		testRoleUser.setRoleId(UUID.fromString(roleId));
		testRoleUser.setUserRolesUdt(new HashSet<UserUdt>(Arrays.asList(WrapperUtil.userToUserUdt(dbUsers.get(0)))));

		DepartmentUsers testDepartmentUser = new DepartmentUsers();

		testDepartmentUser.setDepartmentId(UUID.fromString(departmentId));
		testDepartmentUser
				.setUserDepartmentsUdt(new HashSet<UserUdt>(Arrays.asList(WrapperUtil.userToUserUdt(dbUsers.get(1)))));

		when(userRepository.findByEmailIdContainingIgnoreCase(email)).thenReturn(Arrays.asList(dbUsers.get(0)));

		when(userRepository.findByFirstNameContainingIgnoreCase(firstName)).thenReturn(Arrays.asList(dbUsers.get(1)));

		when(userRepository.findByLastNameContainingIgnoreCase(lastName)).thenReturn(Arrays.asList(dbUsers.get(2)));

		when(roleUsersRepository.findByRoleId(null)).thenReturn(testRoleUser);

		when(departmentUsersRepository.findByDepartmentId(null)).thenReturn(testDepartmentUser);

		if (dbRoles.get(0) == null || dbRoles.isEmpty())
			System.out.println("Null");

		assertThat(expectedResult).isEqualTo((this.searchEngine.searchByAllParameters(email, firstName, lastName,
				Arrays.asList(dbDepartments.get(0), dbDepartments.get(1)),
				Arrays.asList(dbRoles.get(0), dbRoles.get(1)))));
	}
}
