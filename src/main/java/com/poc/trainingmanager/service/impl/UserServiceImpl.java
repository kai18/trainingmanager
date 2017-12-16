package com.poc.trainingmanager.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.AddressUdt;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.search.SearchEngine;
import com.poc.trainingmanager.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	SearchEngine searchEngine;

	@Override
	public StandardResponse<List<UserSearchWrapper>> search(Map<String, String> searchParameters) {

		String firstName = searchParameters.get("firstName");
		String lastName = searchParameters.get("lastName");
		String email = searchParameters.get("email");
		String departments = searchParameters.get("departments");
		String roles = searchParameters.get("role");

		List<User> unwrappedResults = null;
		List<String> departmentList = null;
		List<String> roleList = null;

		departmentList = Arrays.asList(departments.split(","));
		roleList = Arrays.asList(roles.split(","));

		unwrappedResults = searchEngine.searchByAllParameters(email, firstName, lastName, departmentList, roleList);
		List<UserSearchWrapper> wrappedResult = WrapperUtil.wrapUserToUserSearchWrapper(unwrappedResults);

		StandardResponse<List<UserSearchWrapper>> searchResponse = new StandardResponse<List<UserSearchWrapper>>();
		searchResponse.setElement(wrappedResult);
		searchResponse.setMessage("X results found");

		return searchResponse;
	}

	@Override
	public StandardResponse insert(User user, AddressUdt address, RoleUdt role, DepartmentUdt department) {
		// TODO Auto-generated method stub
		return null;
	}

}
