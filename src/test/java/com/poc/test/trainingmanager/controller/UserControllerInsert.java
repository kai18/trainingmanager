
package com.poc.test.trainingmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.Assert;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.trainingmanager.constants.Constants;
import com.poc.trainingmanager.controller.UserController;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.AddressUdt;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerInsert {
	@Mock
	UserService userServiceMock;

	@InjectMocks
	UserController userController;

	private MockMvc mockMvc;

	@Test
	public void testInsertController() throws Exception {
		StandardResponse<User> standardResponse = new StandardResponse<User>();

		AddressUdt address = new AddressUdt("876", "juhj", "hgj", "hjgj", "uyufrd", "india", "567890");
		Date date = new Date();

		String roleId = "ab361ad0-df10-11e7-8dac-bb71b9eabcd5";
		RoleUdt role = new RoleUdt(UUID.fromString(roleId), "Java Administrator", "skj", null, null, date, date);
		Set<RoleUdt> roles = new HashSet<RoleUdt>();
		roles.add(role);

		String departmentId = "07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5";
		DepartmentUdt department = new DepartmentUdt(UUID.fromString(departmentId), "Java", "Java Department", date,
				date);
		Set<DepartmentUdt> departments = new HashSet<DepartmentUdt>();
		departments.add(department);

		String id = "de6d54a0-df0f-11e7-8dac-bb71b9eabdc6";
		User user = new User(UUID.fromString(id), "Spo", "Arun", "12345", "Female", "spo@g.c", "1234567890", true, date,
				date, address, roles, departments);

		standardResponse.setCode(200);
		standardResponse.setElement(user);
		standardResponse.setMessage("User added successfully");
		standardResponse.setStatus(Constants.SUCCESS);

		String userJsonString = "";

		ObjectMapper mapper = new ObjectMapper();
		userJsonString = mapper.writeValueAsString(user);

		userJsonString = mapper.writeValueAsString(user);

		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

		Mockito.when(userServiceMock.insert(Mockito.any(User.class))).thenReturn(standardResponse);

		MvcResult result = mockMvc
				.perform(post("/users/").contentType(MediaType.APPLICATION_JSON).content(userJsonString))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(4)))
				.andExpect(jsonPath("$.status", Matchers.is("success")))
				.andExpect(jsonPath("$.element.firstName", Matchers.is("Spo"))).andReturn();
		Assert.assertNotNull(result);
	}

}
