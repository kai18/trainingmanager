package com.poc.test.trainingmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.poc.test.trainingmanager.testdata.RoleData;
import com.poc.trainingmanager.controller.RoleController;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.service.RoleService;;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoleControllerTest {

	@Mock
	private RoleService roleService;

	@InjectMocks
	private RoleController roleController;

	private MockMvc mockMvc;

	@BeforeClass
	public static void setUpClass() throws Exception{
		
		RoleData.setStandardResponseWithRole();
		RoleData.setStandardResponseWithRoleList();
		RoleData.setJsonStringRole();
	}
	
	@Before
	public void setUp() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
		
		Mockito.when(roleService.getAllRoles())
		.thenReturn(RoleData.standardResponseMockList);

		Mockito.when(roleService.addRole(Mockito.any(Role.class), Mockito.any(LoggedInUserWrapper.class)))
		.thenReturn(RoleData.standardResponseMock);
		
		Mockito.when(roleService.updateRole(Mockito.any(Role.class), Mockito.any(LoggedInUserWrapper.class)))
		.thenReturn(RoleData.standardResponseMock);
		
		Mockito.when(roleService.deleteRole(Mockito.anyString(), Mockito.any(LoggedInUserWrapper.class)))
		.thenReturn(RoleData.standardResponseMock);
	}

	@Test
	public void getAllRoleTest() throws Exception {

				MvcResult result = mockMvc
				.perform(get("/roles")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(4)))
				.andExpect(jsonPath("$.status", Matchers.is("success")))
				.andExpect(jsonPath("$.element").isNotEmpty())
				.andExpect(jsonPath("$.element[0].roleName", Matchers.is("Sys admin"))).andReturn();
		Assert.assertNotNull(result);
	}
	
	@Test
	public void insertRoleTest() throws Exception {

		MvcResult result = mockMvc
				.perform(post("/roles").requestAttr("loggedInUser", new LoggedInUserWrapper())
						.contentType(MediaType.APPLICATION_JSON).content(RoleData.roleJsonString))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(4)))
				.andExpect(jsonPath("$.status", Matchers.is("success")))
				.andExpect(jsonPath("$.element.roleName", Matchers.is("Sys admin"))).andReturn();
		Assert.assertNotNull(result);
	}
	
	@Test
	public void updateRoleTest() throws Exception {

				MvcResult result = mockMvc
				.perform(put("/roles").requestAttr("loggedInUser", new LoggedInUserWrapper())
						.contentType(MediaType.APPLICATION_JSON).content(RoleData.roleJsonString))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(4)))
				.andExpect(jsonPath("$.status", Matchers.is("success")))
				.andExpect(jsonPath("$.element.roleName", Matchers.is("Sys admin"))).andReturn();
		Assert.assertNotNull(result);
	}

	@Test
	public void deleteRoleTest() throws Exception {

		MvcResult result = mockMvc
				.perform(delete("/roles")
						.param("roleId", "de6d54a0-df0f-11e7-8dac-bb71b9eabcd5")
						.requestAttr("loggedInUser", new LoggedInUserWrapper())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(4)))
				.andExpect(jsonPath("$.status", Matchers.is("success")))
				.andExpect(jsonPath("$.element.roleName", Matchers.is("Sys admin"))).andReturn();
		Assert.assertNotNull(result);
	}
}
