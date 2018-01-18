package com.poc.test.trainingmanager.testdata;

	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

	import com.fasterxml.jackson.core.JsonProcessingException;
	import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
    import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
    import com.poc.trainingmanager.model.cassandraudt.RoleUdt;

	public class DepartmentRolesData {

		public static DepartmentRoles mockDepartmentRoles = new DepartmentRoles(UUID.fromString("07aba320-df0c-11e7-8dac-bb71b9eabcd5"), RoleData.roleUdtSet);


		public static String departmentRolesJsonString = "";

		public static void setJsonStringDepartmentRoles() {
			ObjectMapper mapper = new ObjectMapper();

			try {
				departmentRolesJsonString = mapper.writeValueAsString(mockDepartmentRoles);
			} catch (JsonProcessingException e) {
				e.getStackTrace();
			}
		}

		public static StandardResponse<DepartmentRoles> standardResponseMock = new StandardResponse<DepartmentRoles>();

		public static void setStandardResponseWithDepartment() {
			standardResponseMock.setCode(201);
			standardResponseMock.setStatus("success");
			standardResponseMock.setMessage("");
			standardResponseMock.setElement(mockDepartmentRoles);
		}

		public static List<DepartmentRoles> departmentRolesList = new ArrayList<DepartmentRoles>(Arrays.asList(mockDepartmentRoles));

		public static StandardResponse<List<DepartmentRoles>> standardResponseMockList = new StandardResponse<List<DepartmentRoles>>();

		public static void setStandardResponseWithDepartmentList() {
			standardResponseMockList.setCode(200);
			standardResponseMockList.setStatus("success");
			standardResponseMockList.setMessage("");
			standardResponseMockList.setElement(departmentRolesList);
		}
	}



