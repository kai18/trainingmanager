package com.poc.test.trainingmanager.searchtest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
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

	Set<RoleUdt> dbRoles;
	Set<DepartmentUdt> dbDepartments;
	List<User> dbUsers;

	@Before
	public void setUp() {
		System.out.println("Setting up SearchEngine Test");
		MockitoAnnotations.initMocks(this);

		dbRoles = new HashSet<RoleUdt>();
		dbDepartments = new HashSet<DepartmentUdt>();
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
	}
}
