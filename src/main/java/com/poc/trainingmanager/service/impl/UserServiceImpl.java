package com.poc.trainingmanager.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.constants.Constants;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.search.SearchEngine;
import com.poc.trainingmanager.service.UserService;
import com.poc.trainingmanager.utils.PasswordUtil;

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

		unwrappedResults = searchEngine.searchByAllParameters(email, firstName, lastName, departmentList, roleList);
		List<UserSearchWrapper> wrappedResult = WrapperUtil.wrapUserToUserSearchWrapper(unwrappedResults);

		StandardResponse<List<UserSearchWrapper>> searchResponse = new StandardResponse<List<UserSearchWrapper>>();
		searchResponse.setElement(wrappedResult);
		searchResponse.setMessage("X results found");

		return searchResponse;
	}
	 
	@Override
	public StandardResponse insert(User user) {
		StandardResponse stdResponse = new StandardResponse();
		user.setId(UUID.randomUUID());
		user.setPassword(PasswordUtil.getPasswordHash(user.getPassword()));
		userRepository.save(user);
		stdResponse.setStatus(Constants.SUCCESS);
		stdResponse.setCode(200);
		stdResponse.setMessage("User added successfully");
		return stdResponse;
	} 

}
