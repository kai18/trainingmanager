package com.poc.test.trainingmanager.servicetest.departmentservicetest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.DepartmentUsers;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.AddressUdt;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.DepartmentUsersRepository;
import com.poc.trainingmanager.service.helper.DepartmentDeleteHelper;
import com.poc.trainingmanager.service.helper.DepartmentInsertHelper;
import com.poc.trainingmanager.service.helper.DepartmentUpdateHelper;
import com.poc.trainingmanager.service.impl.DepartmentServiceImpl;
import com.poc.trainingmanager.utils.PrivilegeChecker;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = { DepartmentServiceImpl.class })
@ComponentScan
public class DepartmentServiceTest {

	@Mock
	DepartmentRepository departmentMock;

	@Mock
	DepartmentUsersRepository departmentUserRepositoryMock;

	@Mock
	DepartmentRolesRepository departmentRolesRepositoryMock;

	@Mock
	DepartmentInsertHelper departmentInsertHelperMock;

	@InjectMocks
	DepartmentInsertHelper departmentInsertHelper;

	@Mock
	DepartmentUpdateHelper departmentUpdateHelperMock;

	@InjectMocks
	DepartmentUpdateHelper departmentUpdateHelper;

	@Mock
	DepartmentDeleteHelper departmentDeleteHelplerMock;

	@InjectMocks
	DepartmentDeleteHelper departmentDeleteHelpler;

	@Mock
	LoggedInUserWrapper loggedInUserMock;

	@Mock
	PrivilegeChecker privilegeCheckerMock;

	@InjectMocks
	PrivilegeChecker privilegeChecker;

	@Autowired
	@InjectMocks
	DepartmentServiceImpl departmentServiceMock;

	// private OngoingStubbing<Department> thenReturn;

	@Test
	public void testGetAllDpartments() {
		StandardResponse<List<Department>> standardResponse = new StandardResponse<List<Department>>();
		StandardResponse<List<Department>> std = new StandardResponse<List<Department>>();
		Date date = new Date();
		String id = "07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5";
		Department department = new Department();
		department.setDepartmentId(UUID.fromString(id));
		department.setDepartmentName("Angular");
		department.setDepartmentDescription("Angular department");
		department.setDepartmentCreatedDtm(date);
		department.setDepartmentUpdatedDtm(null);
		List<Department> departmentList = new ArrayList<Department>();
		departmentList.add(department);
		// when(departmentMock.findAll()).thenReturn(null);
		// standardResponse.setCode(420);
		// std = departmentServiceMock.getAllDepartments();
		// assertEquals(standardResponse.getCode(),std.getCode());

		// departmentList.add(department);
		when(departmentMock.findAll()).thenReturn(departmentList);
		standardResponse.setCode(200);
		std = departmentServiceMock.getAllDepartments();
		assertEquals(standardResponse.getCode(), std.getCode());
	}

	@Test
	public void testInsertDepartment() {
		StandardResponse<Department> standardResponse = new StandardResponse<Department>();
		StandardResponse<Department> std = new StandardResponse<Department>();
		Date date = new Date();
		String id = "07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5";
		Department department = new Department();
		department.setDepartmentId(UUID.fromString(id));
		department.setDepartmentName("Angular");
		department.setDepartmentDescription("Angular department");
		department.setDepartmentCreatedDtm(date);
		department.setDepartmentUpdatedDtm(null);

		String departmentId = "ab361ad0-df10-11e7-8dac-bb71b9eabcd5";
		Department departmentAnother = new Department();
		departmentAnother.setDepartmentId(UUID.fromString(departmentId));
		departmentAnother.setDepartmentName("JAVA");
		departmentAnother.setDepartmentDescription("JAVA department");
		departmentAnother.setDepartmentCreatedDtm(date);
		departmentAnother.setDepartmentUpdatedDtm(null);
		AddressUdt address = new AddressUdt("876", "juhj", "hgj", "hjgj", "uyufrd", "india", "567890");
		// Role rol = new Role();
		RoleUdt role = new RoleUdt(UUID.fromString(id), "Java Administrator", "skj", null, null, date, date);
		Set<RoleUdt> roles = new HashSet<RoleUdt>();
		roles.add(role);

		DepartmentUdt departmentUdtMock = new DepartmentUdt(UUID.fromString(id), "Java", "Java Department", date, date);
		// Department dept = new Department();
		Set<DepartmentUdt> departments = new HashSet<DepartmentUdt>();
		departments.add(departmentUdtMock);
		User user = new User(UUID.fromString(id), "Spoorthi", "Arun", "12345", "Female", "spo@g.c", "1234567890", true,
				date, date, address, roles, departments);

		Set<UserUdt> userSet = new HashSet<UserUdt>();
		userSet.add(WrapperUtil.userToUserUdt(user));

		DepartmentUsers departmentUsers = new DepartmentUsers();
		departmentUsers.setDepartmentId(UUID.fromString(id));
		departmentUsers.setUserDepartmentsUdt(userSet);
		DepartmentRoles departmentRoles = new DepartmentRoles();

		String userId = "06d6f888-1890-4605-9a77-2af50e49e113";
		DepartmentUdt departmentUdt = WrapperUtil.departmentToDepartmentUdt(department);
		Set<DepartmentUdt> departmentUdtSet = new HashSet<DepartmentUdt>();
		departmentUdtSet.add(departmentUdt);
		PrivilegeUdt privileges = new PrivilegeUdt(1, 1, 1, 1, UUID.fromString(id));
		Set<PrivilegeUdt> privilegeSet = new HashSet<PrivilegeUdt>();
		privilegeSet.add(privileges);
		loggedInUserMock.setUserId(UUID.fromString(userId));
		loggedInUserMock.setDepartments(departmentUdtSet);
		loggedInUserMock.setPrivileges(privilegeSet);

		// when(departmentMock.save(department)).thenReturn(department);
		// System.out.println(privileges.getCreationPrivilege());
		// System.out.println(privilegeCheckerMock);
		when(privilegeCheckerMock.isSuperAdmin(privilegeSet)).thenReturn(true);
		when(privilegeCheckerMock.isAllowedToCreateDepartment(privilegeSet)).thenReturn(true);

		standardResponse.setCode(422);
		std = departmentServiceMock.addDepartment(null, loggedInUserMock);
		assertEquals(standardResponse.getCode(), std.getCode());

		when(departmentMock.findByDepartmentName(departmentAnother.getDepartmentName())).thenReturn(departmentAnother);
		when(departmentInsertHelperMock.insertIntoDepartment(department)).thenReturn(department);
		when(departmentMock.save(department)).thenReturn(department);
		departmentInsertHelperMock.insertIntoDepartmentUsers(department);
		when(departmentUserRepositoryMock.save(departmentUsers)).thenReturn(departmentUsers);
		departmentInsertHelperMock.insertIntoDepartmentRoles(department);
		when(departmentRolesRepositoryMock.save(departmentRoles)).thenReturn(departmentRoles);

		standardResponse.setCode(200);
		std = departmentServiceMock.addDepartment(department, loggedInUserMock);
		assertEquals(standardResponse.getCode(), std.getCode());

	}

	@Test
	public void testInsertHelper() {
		Department department = new Department();
		Department departmentAnother = new Department();
		departmentInsertHelper.insertIntoDepartment(department);
		departmentInsertHelper.insertIntoDepartmentRoles(department);
		departmentInsertHelper.insertIntoDepartmentUsers(department);
		departmentUpdateHelper.updateDepartment(department, departmentAnother);
		departmentUpdateHelper.updateDepartmentUsers(department, departmentAnother);
	}

	@Test
	public void testPrivilegeChecker() {
		String id = "07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5";
		PrivilegeUdt privileges = new PrivilegeUdt(1, 1, 1, 1, UUID.fromString(id));
		Set<PrivilegeUdt> privilegeSet = new HashSet<PrivilegeUdt>();
		privilegeSet.add(privileges);
		privilegeCheckerMock.isSuperAdmin(privilegeSet);

		privilegeCheckerMock.isAllowedToCreateDepartment(privilegeSet);
	}

	@Test
	public void testUpdateDepartment() {
		StandardResponse<Department> standardResponse = new StandardResponse<Department>();
		StandardResponse<Department> std = new StandardResponse<Department>();
		Date date = new Date();
		String id = "07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5";
		Department department = new Department();
		department.setDepartmentId(UUID.fromString(id));
		department.setDepartmentName("Angular");
		department.setDepartmentDescription("Angular department");
		department.setDepartmentCreatedDtm(date);
		department.setDepartmentUpdatedDtm(null);

		String departmentId = "ab361ad0-df10-11e7-8dac-bb71b9eabcd5";
		Department departmentAnother = new Department();
		departmentAnother.setDepartmentId(UUID.fromString(departmentId));
		departmentAnother.setDepartmentName("JAVA");
		departmentAnother.setDepartmentDescription("JAVA department");
		departmentAnother.setDepartmentCreatedDtm(date);
		departmentAnother.setDepartmentUpdatedDtm(null);

		PrivilegeUdt privileges = new PrivilegeUdt(1, 1, 1, 1, UUID.fromString(id));
		Set<PrivilegeUdt> privilegeSet = new HashSet<PrivilegeUdt>();
		privilegeSet.add(privileges);

		when(privilegeCheckerMock.isSuperAdmin(privilegeSet)).thenReturn(true);
		when(privilegeCheckerMock.isAllowedToEditDepartment(privilegeSet, UUID.fromString(id))).thenReturn(true);
		when(departmentMock.findByDepartmentId(UUID.fromString(id))).thenReturn(department);
		when(departmentUpdateHelperMock.updateDepartment(department, department)).thenReturn(department);

		departmentUpdateHelperMock.updateDepartmentUsers(department, department);

		standardResponse.setCode(200);
		std = departmentServiceMock.updateDepartment(department, loggedInUserMock);

		assertEquals(standardResponse.getCode(), std.getCode());
	}

	@Test
	public void testDeleteDepartment() {
		StandardResponse<Department> standardResponse = new StandardResponse<Department>();
		StandardResponse<Department> std = new StandardResponse<Department>();
		Date date = new Date();
		String id = "07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5";
		Department department = new Department();
		department.setDepartmentId(UUID.fromString(id));
		department.setDepartmentName("Angular");
		department.setDepartmentDescription("Angular department");
		department.setDepartmentCreatedDtm(date);
		department.setDepartmentUpdatedDtm(null);

		AddressUdt address = new AddressUdt("876", "juhj", "hgj", "hjgj", "uyufrd", "india", "567890");
		// Role rol = new Role();
		RoleUdt role = new RoleUdt(UUID.fromString(id), "Java Administrator", "skj", null, null, date, date);
		Set<RoleUdt> roles = new HashSet<RoleUdt>();
		roles.add(role);

		DepartmentUdt departmentUdtMock = new DepartmentUdt(UUID.fromString(id), "Java", "Java Department", date, date);
		// Department dept = new Department();
		Set<DepartmentUdt> departments = new HashSet<DepartmentUdt>();
		departments.add(departmentUdtMock);
		User user = new User(UUID.fromString(id), "Spoorthi", "Arun", "12345", "Female", "spo@g.c", "1234567890", true,
				date, date, address, roles, departments);

		Set<UserUdt> userSet = new HashSet<UserUdt>();
		userSet.add(WrapperUtil.userToUserUdt(user));

		DepartmentUsers departmentUsers = new DepartmentUsers();
		departmentUsers.setDepartmentId(UUID.fromString(id));
		departmentUsers.setUserDepartmentsUdt(userSet);
		DepartmentRoles departmentRoles = new DepartmentRoles();

		String userId = "06d6f888-1890-4605-9a77-2af50e49e113";
		DepartmentUdt departmentUdt = WrapperUtil.departmentToDepartmentUdt(department);
		Set<DepartmentUdt> departmentUdtSet = new HashSet<DepartmentUdt>();
		departmentUdtSet.add(departmentUdt);
		PrivilegeUdt privileges = new PrivilegeUdt(1, 1, 1, 1, UUID.fromString(id));
		Set<PrivilegeUdt> privilegeSet = new HashSet<PrivilegeUdt>();
		privilegeSet.add(privileges);
		loggedInUserMock.setUserId(UUID.fromString(userId));
		loggedInUserMock.setDepartments(departmentUdtSet);
		loggedInUserMock.setPrivileges(privilegeSet);

		when(privilegeCheckerMock.isSuperAdmin(privilegeSet)).thenReturn(true);
		when(privilegeCheckerMock.IsAllowedToDeleteDepartment(privilegeSet)).thenReturn(true);
		departmentMock.delete(department);
		departmentUserRepositoryMock.delete(departmentUsers);
		departmentRolesRepositoryMock.delete(departmentRoles);
		departmentDeleteHelplerMock.deleteFromDepartment(id);
		departmentDeleteHelplerMock.deleteFromDepartmentRoles(id);
		departmentDeleteHelplerMock.deleteFromDepartmentUsers(id);

		standardResponse.setCode(200);
		std = departmentServiceMock.deleteDepartment(id, loggedInUserMock);

		assertEquals(standardResponse.getCode(), std.getCode());
	}

}
