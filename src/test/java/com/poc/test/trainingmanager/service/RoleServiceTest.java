package com.poc.test.trainingmanager.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.poc.test.trainingmanager.config.RoleData;
import com.poc.trainingmanager.controller.RoleController;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.RoleService;
import com.poc.trainingmanager.service.helper.RoleDeleteHelper;
import com.poc.trainingmanager.service.helper.RoleInsertHelper;
import com.poc.trainingmanager.service.helper.RoleUpdateHelper;
import com.poc.trainingmanager.service.impl.RoleServiceImpl;
import com.poc.trainingmanager.utils.PrivilegeChecker;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RoleServiceImpl.class })
public class RoleServiceTest {

	@Mock
	RoleInsertHelper roleInsertHelper;

	@Mock
	RoleDeleteHelper roleDeleteHelper;

	@Mock
	RoleUpdateHelper roleUpdateHelper;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private DepartmentRolesRepository departmentRolesRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleUsersRepository roleUsersRepository;

	@Mock
	private PrivilegeChecker privilegeChecker;

	@InjectMocks
	private RoleServiceImpl roleService;

	private MockMvc mockMvc;

	@BeforeClass
	public static void setUp() throws Exception {

		RoleData.setStandardResponseWithRole();
		RoleData.setStandardResponseWithRoleList();
		RoleData.setJsonStringRole();
	}
	
	@Test
	public void getAllRoleTest() throws Exception {

		StandardResponse<List<Role>> standardResponseMock = new StandardResponse<List<Role>>();

		Role mockRole = new Role(UUID.fromString("de6d54a0-df0f-11e7-8dac-bb71b9eabcd5"), "Sys admin", "System",
				"Manages all roles", new PrivilegeUdt(1, 1, 1, 1, null), new Date(), new Date());
		List<Role> roleList = new ArrayList<Role>();
		roleList.add(mockRole);
		standardResponseMock.setCode(201);
		standardResponseMock.setStatus("success");
		standardResponseMock.setElement(roleList);

		Mockito.when(roleRepository.findAll()).thenReturn(roleList);

		Assert.assertEquals(roleService.getAllRoles().getElement().size(), standardResponseMock.getElement().size());
		Assert.assertEquals(roleService.getAllRoles().getStatus(), "success");
	}

}
