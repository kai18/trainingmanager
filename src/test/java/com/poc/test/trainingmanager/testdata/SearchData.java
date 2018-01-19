package com.poc.test.trainingmanager.testdata;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;

public class SearchData {

	public static List<RoleUdt> dbRoles;
	public static List<DepartmentUdt> dbDepartments;
	public static List<User> dbUsers;
	public static List<User> expectedResult;

	public static void setUp() {
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

}
