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
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;

public class RoleData {

	public static Role mockRole = new Role(UUID.fromString("b7e79dd0-df10-11e7-8dac-bb71b9eabcd5"), "Sys admin",
			"System", "Manages all roles", new PrivilegeUdt(1, 1, 1, 1, null),
			new Date(Long.parseLong("1513062841979")), new Date(Long.parseLong("1513062841979")));

	public static String roleJsonString = "";

	public static void setJsonStringRole() {
		ObjectMapper mapper = new ObjectMapper();

		try {
			roleJsonString = mapper.writeValueAsString(mockRole);
		} catch (JsonProcessingException e) {
			e.getStackTrace();
		}
	}

	public static Role mockRole1 = new Role(UUID.fromString("de6d54a0-df0f-11e7-8dac-bb71b9eabcd5"),
			"Java administrator", "Department", "Manages the Java department",
			new PrivilegeUdt(1, 1, 1, 1, UUID.fromString("07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5")),
			new Date(Long.parseLong("1513062841979")), new Date(Long.parseLong("1513062841979")));

	public static Role mockRole2 = new Role(UUID.fromString("ab3aaeb0-df10-11e7-8dac-bb71b9eabcd5"), "Faculty", "Department",
			"Normal user with only view privilege", new PrivilegeUdt(0, 0, 0, 1, null),
			new Date(Long.parseLong("1513062841979")), new Date(Long.parseLong("1513062841979")));

	public static StandardResponse<Role> standardResponseMock = new StandardResponse<Role>();

	public static void setStandardResponseWithRole() {
		standardResponseMock.setCode(201);
		standardResponseMock.setStatus("success");
		standardResponseMock.setMessage("");
		standardResponseMock.setElement(mockRole);
	}

	public static List<Role> roleList = new ArrayList<Role>(Arrays.asList(mockRole, mockRole1, mockRole2));

	public static StandardResponse<List<Role>> standardResponseMockList = new StandardResponse<List<Role>>();

	public static void setStandardResponseWithRoleList() {
		standardResponseMockList.setCode(200);
		standardResponseMockList.setStatus("success");
		standardResponseMockList.setMessage("");
		standardResponseMockList.setElement(roleList);
	}
	
	public static RoleUdt mockRoleUdt = new RoleUdt(UUID.fromString("b7e79dd0-df10-11e7-8dac-bb71b9eabcd5"), "Sys admin",
			"System", "Manages all roles", new PrivilegeUdt(1, 1, 1, 1, null),
			new Date(Long.parseLong("1513062841979")), new Date(Long.parseLong("1513062841979")));
	
	public static RoleUdt mockRoleUdt1 = new RoleUdt(UUID.fromString("de6d54a0-df0f-11e7-8dac-bb71b9eabcd5"),
			"Java administrator", "Department", "Manages the Java department",
			new PrivilegeUdt(1, 1, 1, 1, UUID.fromString("07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5")),
			new Date(Long.parseLong("1513062841979")), new Date(Long.parseLong("1513062841979")));

	public static RoleUdt mockRoleUdt2 = new RoleUdt(UUID.fromString("ab3aaeb0-df10-11e7-8dac-bb71b9eabcd5"), "Faculty", "Department",
			"Normal user with only view privilege", new PrivilegeUdt(0, 0, 0, 1, UUID.fromString("07b1e4b0-df0c-11e7-8dac-bb71b9eabcd5")),
			new Date(Long.parseLong("1513062841979")), new Date(Long.parseLong("1513062841979")));
	
	public static Set<RoleUdt> roleUdtSet = new HashSet<RoleUdt>(Arrays.asList(mockRoleUdt,mockRoleUdt1, mockRoleUdt2));

	public static StandardResponse<Set<RoleUdt>> standardResponseMockRoleUdtSet = new StandardResponse<Set<RoleUdt>>();

	public static void setStandardResponseWithRoleUdtSet() {
		standardResponseMockRoleUdtSet.setCode(200);
		standardResponseMockRoleUdtSet.setStatus("success");
		standardResponseMockRoleUdtSet.setMessage("");
		standardResponseMockRoleUdtSet.setElement(roleUdtSet);
	}
}
