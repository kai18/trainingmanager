package com.poc.test.trainingmanager.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

import com.poc.test.trainingmanager.testdata.DepartmentRolesData;
import com.poc.test.trainingmanager.testdata.RoleData;
import com.poc.test.trainingmanager.testdata.RoleUsersData;
import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.helper.RoleDeleteHelper;
import com.poc.trainingmanager.service.helper.RoleInsertHelper;
import com.poc.trainingmanager.service.helper.RoleUpdateHelper;
import com.poc.trainingmanager.service.impl.RoleServiceImpl;
import com.poc.trainingmanager.utils.PrivilegeChecker;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = { RoleServiceImpl.class })
@ComponentScan
public class RoleServiceTest {

	private static RoleInsertHelper roleInsertHelper;

	private static RoleDeleteHelper roleDeleteHelper;

	private static RoleUpdateHelper roleUpdateHelper;

	private static RoleRepository roleRepository;
	
	private static PrivilegeChecker privilegeChecker;

	private static RoleUsersRepository roleUsersRepository;
	
	private static DepartmentRolesRepository departmentRolesRepository;

	@InjectMocks
	private RoleServiceImpl roleService;


	@BeforeClass
	public static void setUp() throws Exception {

		RoleData.setStandardResponseWithRole();
		RoleData.setStandardResponseWithRoleList();
		RoleData.setJsonStringRole();
		RoleUsersData.setRoleUsers();
		
		roleRepository = mock(RoleRepository.class);
		privilegeChecker = mock(PrivilegeChecker.class);
		roleInsertHelper = mock(RoleInsertHelper.class);
		roleUpdateHelper = mock(RoleUpdateHelper.class);
		roleDeleteHelper = mock(RoleDeleteHelper.class);
		roleUsersRepository = mock(RoleUsersRepository.class);
		departmentRolesRepository = mock(DepartmentRolesRepository.class);
		
		Mockito.when(roleRepository.findAll()).thenReturn(RoleData.roleList);
		Mockito.when(roleRepository.findByRoleId(Mockito.any(UUID.class))).thenReturn(RoleData.mockRole);
		Mockito.when(roleRepository.findByRoleName(RoleData.mockRole.getRoleName())).thenReturn(RoleData.mockRole1);
		Mockito.when(privilegeChecker.isAllowedToCreateRole(Mockito.anySetOf(PrivilegeUdt.class))).thenReturn(Boolean.TRUE);
		Mockito.when(roleInsertHelper.insertIntoRole(Mockito.any(Role.class))).thenReturn(RoleData.mockRole2);
		//Mockito.when(roleInsertHelper.insertIntoRole(null)).thenReturn(RoleData.mockRole);
		Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(RoleData.mockRole);
		Mockito.when(privilegeChecker.isAllowedToEditRole(Mockito.anySetOf(PrivilegeUdt.class))).thenReturn(Boolean.TRUE);
		Mockito.when(roleUpdateHelper.updateRole(Mockito.any(Role.class), Mockito.any(Role.class))).thenReturn(RoleData.mockRole1);
		Mockito.when(privilegeChecker.isAllowedToDeleteRole(Mockito.anySetOf(PrivilegeUdt.class))).thenReturn(Boolean.TRUE);
		Mockito.doNothing().when(roleRepository).delete(Mockito.isA(Role.class));
		Mockito.doNothing().when(roleDeleteHelper).deleteFromRole(Mockito.isA(Role.class));
		Mockito.doNothing().when(roleDeleteHelper).deleteFromDepartmentRoles(Mockito.isA(UUID.class),Mockito.isA(Role.class));
		Mockito.doNothing().when(roleDeleteHelper).deleteFromRoleUsers(Mockito.isA(Role.class));
		Mockito.when(roleUsersRepository.save(Mockito.any(RoleUsers.class))).thenReturn(RoleUsersData.roleUsers);
		Mockito.when(departmentRolesRepository.findByDepartmentId(Mockito.any(UUID.class))).thenReturn(DepartmentRolesData.mockDepartmentRoles);
	}
	
	@Test
	public void getAllRoleTest() throws Exception {

		StandardResponse<List<Role>> standardResponse = roleService.getAllRoles();
		System.out.println(standardResponse);
		assertNotNull(standardResponse);
		assertEquals(3, standardResponse.getElement().size());
		assertEquals("success", standardResponse.getStatus());
		
		Mockito.verify(roleRepository).findAll();
	}
	
	@Test
	public void getAllRoleNameTest() throws Exception {

		StandardResponse<Role> standardResponse = roleService.getRoleByName(RoleData.mockRole.getRoleName());
		System.out.println(standardResponse);
		assertNotNull(standardResponse);
		assertNotNull(standardResponse.getElement());
		assertEquals("success", standardResponse.getStatus());
		
		Mockito.verify(roleRepository).findByRoleName(RoleData.mockRole.getRoleName());
	}
	
	@Test
	public void getDepartmentRolesTest() throws Exception {

		StandardResponse<Set<RoleUdt>> standardResponse = roleService.getDepartmentRoles(RoleData.mockRole.getRoleId());
		System.out.println(standardResponse);
		assertNotNull(standardResponse);
		assertNotNull(standardResponse.getElement());
		assertEquals("success", standardResponse.getStatus());
		
		Mockito.verify(roleRepository).findByRoleName(RoleData.mockRole.getRoleName());
	}
	
	@Test
	public void insertRoleTest() throws Exception{
		StandardResponse<Role> standardResponse = roleService.addRole(RoleData.mockRole, new LoggedInUserWrapper());
		System.out.println(standardResponse);
		assertNotNull(standardResponse);
		assertEquals("success", standardResponse.getStatus());
	}
	
	@Test
	public void roleInsertHelperTest() throws Exception{
		roleInsertHelper.insertIntoRoleUsers(RoleData.mockRole);
		Mockito.verify(roleInsertHelper).insertIntoRole(RoleData.mockRole);
		
//		roleInsertHelper.insertIntoRoleUsers(RoleData.mockRole);
//		Mockito.verify(roleInsertHelper).insertIntoRoleUsers(RoleData.mockRole);
//		
//		roleInsertHelper.insertIntoRoleUsers(RoleData.mockRole);
//		Mockito.verify(roleInsertHelper).insertIntoRole(RoleData.mockRole);
	}
	
	@Test
	public void updateRoleTest() throws Exception{
		StandardResponse<Role> standardResponse = roleService.updateRole(RoleData.mockRole1, new LoggedInUserWrapper());
		System.out.println(standardResponse);
		assertNotNull(standardResponse);
		assertEquals("success", standardResponse.getStatus());
	}
	
	@Test
	public void deleteRoleTest() throws Exception{
		StandardResponse<Role> standardResponse = roleService.deleteRole(RoleData.mockRole1.getRoleId().toString(), new LoggedInUserWrapper());
		System.out.println(standardResponse);
		assertNotNull(standardResponse);
		assertEquals("success", standardResponse.getStatus());
	}
}
