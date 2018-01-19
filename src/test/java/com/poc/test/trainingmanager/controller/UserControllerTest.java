package com.poc.test.trainingmanager.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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

import com.poc.test.trainingmanager.testdata.SearchData;
import com.poc.trainingmanager.constants.Constants;
import com.poc.trainingmanager.controller.UserController;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

	@Mock
	UserService userService;

	@InjectMocks
	UserController userController;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		SearchData.setUp();
	}

	@Test
	public void testSearch() throws Exception {

		StandardResponse<List<UserSearchWrapper>> searchResponse = new StandardResponse<List<UserSearchWrapper>>();
		searchResponse.setElement(WrapperUtil.wrapUserToUserSearchWrapper(SearchData.expectedResult));
		searchResponse.setCode(200);
		searchResponse.setStatus(Constants.SUCCESS);

		when(userService.search(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(searchResponse);
		MvcResult result = mockMvc
				.perform(get("/users/search").param("firstName", "kaustubh").param("lastName", "lastname")
						.param("emailId", "skjd@sd.com").param("departments", "departments").param("roles", "roles")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(4)))
				.andExpect(jsonPath("$.status", Matchers.is(Constants.SUCCESS)))
				.andExpect(jsonPath("$.element", Matchers.notNullValue())).andReturn();
		Assert.assertNotNull(result);
	}

}