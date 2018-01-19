package com.poc.test.trainingmanager.testdata;

	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.Date;
	import java.util.List;
	import java.util.UUID;

	import com.fasterxml.jackson.core.JsonProcessingException;
	import com.fasterxml.jackson.databind.ObjectMapper;
	import com.poc.trainingmanager.model.Department;
	import com.poc.trainingmanager.model.StandardResponse;

	public class DepartmentData {

		public static Department mockDepartment = new Department(UUID.fromString("07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5"), "Angular", "Angular department",
				new Date(Long.parseLong("1513062841979")), new Date(Long.parseLong("1513062841979")));

		public static String departmentJsonString = "";

		public static void setJsonStringDepartment() {
			ObjectMapper mapper = new ObjectMapper();

			try {
				departmentJsonString = mapper.writeValueAsString(mockDepartment);
			} catch (JsonProcessingException e) {
				e.getStackTrace();
			}
		}

		public static Department mockDepartment1 = new Department(UUID.fromString("07aba320-df0c-11e7-8dac-bb71b9eabcd5"), "Java",  "Java department",
				new Date(Long.parseLong("1513062841979")), new Date(Long.parseLong("1513062841979")));

		public static Department mockDepartment2 = new Department(UUID.fromString("07b2f620-df0c-11e7-8dac-bb71b9eabcd5"), "Dot Net", "Dot net department", 
				new Date(Long.parseLong("1513062841979")), new Date(Long.parseLong("1513062841979")));

		public static StandardResponse<Department> standardResponseMock = new StandardResponse<Department>();

		public static void setStandardResponseWithDepartment() {
			standardResponseMock.setCode(201);
			standardResponseMock.setStatus("success");
			standardResponseMock.setMessage("");
			standardResponseMock.setElement(mockDepartment);
		}

		public static List<Department> departmentList = new ArrayList<Department>(Arrays.asList(mockDepartment, mockDepartment1, mockDepartment2));

		public static StandardResponse<List<Department>> standardResponseMockList = new StandardResponse<List<Department>>();

		public static void setStandardResponseWithDepartmentList() {
			standardResponseMockList.setCode(200);
			standardResponseMockList.setStatus("success");
			standardResponseMockList.setMessage("");
			standardResponseMockList.setElement(departmentList);
		}
	}


