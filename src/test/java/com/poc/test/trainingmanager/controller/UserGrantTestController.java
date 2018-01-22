package com.poc.test.trainingmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.poc.trainingmanager.controller.RoleController;
import com.poc.trainingmanager.controller.UserController;
import com.poc.trainingmanager.model.Department;
//import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.AddressUdt;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
//import com.poc.trainingmanager.service.RoleService;
import com.poc.trainingmanager.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserGrantTestController {

	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	public void grantRoleTest() throws Exception {
		StandardResponse<User> standardResponseMock = new StandardResponse<User>();

		// User mockUser = new User(UUID.randomUUID(), "nitin", "shrinivas","","male",
		// "nitin@gmail.com","1234567890","true",new Date(), new Date(),
		// new AddressUdt("12","ring","mahadev","blore","karnataka","india","987654"));

		Date date = new Date();
		RoleUdt role = new RoleUdt(UUID.fromString("b7e79dd0-df10-11e7-8dac-bb71b9eabcd5"), "Java Administrator", "skj",
				null, null, date, date);
		Set<RoleUdt> roles = new HashSet<RoleUdt>();
		roles.add(role);

		DepartmentUdt department = new DepartmentUdt(UUID.fromString("07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5"), "Java",
				"Java Department", date, date);
		Department dept = new Department();
		Set<DepartmentUdt> departments = new HashSet<DepartmentUdt>();
		departments.add(department);

		User mockUser = new User(UUID.randomUUID(), "nitin", "shrinivas", "", "male", "nitin@gmail.com", "1234567890",
				true, date, date, new AddressUdt("12", "ring", "mahadev", "blore", "karnataka", "india", "987654"),
				roles, departments);

		/**
		 * AddressUdt address= new AddressUdt("876", "juhj", "hgj", "hjgj", "uyufrd",
		 * "india", "567890"); String id="de6d54a0-df0f-11e7-8dac-bb71b9eabdc6"; User
		 * user=new User(UUID.fromString(id), "Spo", "Arun", "12345", "Female",
		 * "spo@g.c","1234567890", true, date, date, address,roles,departments);
		 **/

		standardResponseMock.setCode(201);
		standardResponseMock.setStatus("success");
		standardResponseMock.setElement(mockUser);
		standardResponseMock.setMessage("Grantroleworking");

		String roleJsonString = "";
		ObjectMapper mapper = new ObjectMapper();

		try {
			roleJsonString = mapper.writeValueAsString(mockUser);
		} catch (JsonProcessingException e) {
			e.getStackTrace();
		}

		Mockito.when(
				userService.grantRole(Mockito.anyString(), Mockito.anyString(), Mockito.any(LoggedInUserWrapper.class)))
				.thenReturn(standardResponseMock);

		MvcResult result = mockMvc
				.perform(put("/users/grant/{roleId}/user/{userId}", "ab361ad0-df10-11e7-8dac-bb71b9eabcd5",
						"de6d54a0-df0f-11e7-8dac-bb71b9eabdc6").requestAttr("loggedInUser", new LoggedInUserWrapper())
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(4)))
				.andExpect(jsonPath("$.status", Matchers.is("success"))).andReturn();
		System.out.println(roleJsonString);
		Assert.assertNotNull(result);
	}
}
