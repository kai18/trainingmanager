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

import com.poc.test.trainingmanager.testdata.DepartmentData;
import com.poc.trainingmanager.controller.DepartmentController;
import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.service.DepartmentService;;

@RunWith(SpringJUnit4ClassRunner.class)
public class DepartmentControllerTest {

	@Mock
	private DepartmentService departmentService;

	@InjectMocks
	private DepartmentController departmentController;

	private MockMvc mockMvc;

	@BeforeClass
	public static void setUpClass() throws Exception{
		
		DepartmentData.setStandardResponseWithDepartment();
		DepartmentData.setStandardResponseWithDepartmentList();
		DepartmentData.setJsonStringDepartment();
	}
	
	@Before
	public void setUp() throws Exception {

		mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
		
		Mockito.when(departmentService.getAllDepartments())
		.thenReturn(DepartmentData.standardResponseMockList);

		Mockito.when(departmentService.addDepartment(Mockito.any(Department.class), Mockito.any(LoggedInUserWrapper.class)))
		.thenReturn(DepartmentData.standardResponseMock);
		
		Mockito.when(departmentService.updateDepartment(Mockito.any(Department.class), Mockito.any(LoggedInUserWrapper.class)))
		.thenReturn(DepartmentData.standardResponseMock);
		
		Mockito.when(departmentService.deleteDepartment(Mockito.anyString(), Mockito.any(LoggedInUserWrapper.class)))
		.thenReturn(DepartmentData.standardResponseMock);
	}

	@Test
	public void getAllDepartmentTest() throws Exception {

				MvcResult result = mockMvc
				.perform(get("/departments")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(4)))
				.andExpect(jsonPath("$.status", Matchers.is("success")))
				.andExpect(jsonPath("$.element").isNotEmpty())
				.andExpect(jsonPath("$.element[0].departmentName", Matchers.is("Angular"))).andReturn();
		Assert.assertNotNull(result);
	}
	
	@Test
	public void insertDepartmentTest() throws Exception {

		MvcResult result = mockMvc
				.perform(post("/departments").requestAttr("loggedInUser", new LoggedInUserWrapper())
						.contentType(MediaType.APPLICATION_JSON).content(DepartmentData.departmentJsonString))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(4)))
				.andExpect(jsonPath("$.status", Matchers.is("success")))
				.andExpect(jsonPath("$.element.departmentName", Matchers.is("Angular"))).andReturn();
		Assert.assertNotNull(result);
	}
	
	@Test
	public void updateDepartmentTest() throws Exception {

				MvcResult result = mockMvc
				.perform(put("/departments").requestAttr("loggedInUser", new LoggedInUserWrapper())
						.contentType(MediaType.APPLICATION_JSON).content(DepartmentData.departmentJsonString))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(4)))
				.andExpect(jsonPath("$.status", Matchers.is("success")))
				.andExpect(jsonPath("$.element.departmentName", Matchers.is("Angular"))).andReturn();
		Assert.assertNotNull(result);
	}

	@Test
	public void deleteDepartmentTest() throws Exception {

		MvcResult result = mockMvc
				.perform(delete("/departments/{departmentId}","07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5")
						.requestAttr("loggedInUser", new LoggedInUserWrapper())
						.contentType(MediaType.APPLICATION_JSON).content(DepartmentData.departmentJsonString))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*", Matchers.hasSize(4)))
				.andExpect(jsonPath("$.status", Matchers.is("success")))
				.andExpect(jsonPath("$.element.departmentName", Matchers.is("Angular"))).andReturn();
		Assert.assertNotNull(result);
	}
} 
