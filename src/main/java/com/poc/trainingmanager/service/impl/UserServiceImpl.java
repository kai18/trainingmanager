package com.poc.trainingmanager.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.constants.Constants;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.search.SearchEngine;
import com.poc.trainingmanager.service.UserService;
import com.poc.trainingmanager.utils.PasswordUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	DepartmentRolesRepository departmentRolesRepository;

	@Autowired
	SearchEngine searchEngine;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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

	/**
	 * <p>
	 * Insert method is used to insert into a new user who is trying to register
	 * into the database. For the insert operation the user should not be null.
	 * Also, the user should not already exist in the table. The unique constraint
	 * is set for Email Id. User ID is randomly generated and the password is hashed
	 * before storing into the database.
	 * </p>
	 */
	@Override
	public StandardResponse insert(User user) {
		StandardResponse standardResponse = new StandardResponse();
		if (user == null) {
			logger.error("Inserted user was null, hence failed to Add user");
			standardResponse.setCode(422);
			standardResponse.setStatus("Failed");
			standardResponse.setMessage("User cant be null, failed to add this user");
			return standardResponse;
		}
		if (userRepository.findByEmailId(user.getEmailId()) != null) {
			logger.error("User {" + user.getEmailId() + "} already exists, duplicate user cannot be inserted");
			standardResponse.setCode(409);
			standardResponse.setStatus("Failed");
			standardResponse.setMessage("user already exists, failed to add this user");
			return standardResponse;
		}
		user.setId(UUID.randomUUID());
		user.setPassword(PasswordUtil.getPasswordHash(user.getPassword()));
		userRepository.save(user);
		standardResponse.setStatus(Constants.SUCCESS);
		standardResponse.setCode(200);
		standardResponse.setMessage("User added successfully");
		logger.info("User {" + user.getEmailId() + "} successfully added");
		return standardResponse;
	}

	@Override
	public StandardResponse grantrole(UUID userId, UUID roleId) {
		StandardResponse stdResponse = new StandardResponse();
		User user = userRepository.findById(userId);
		Set<RoleUdt> assignedRoles = user.getRoles();
		Role grantRole = roleRepository.findByRoleId(roleId);
		if (departmentRolesRepository.findByRoles(WrapperUtil.roleToRoleUdt(grantRole)) == null) {
			stdResponse.setCode(404);
			stdResponse.setStatus("Failed");
			stdResponse.setMessage("Role not granted");
		}
		assignedRoles.add(WrapperUtil.roleToRoleUdt(grantRole));
		user.setRoles(assignedRoles);
		user = userRepository.save(user);
		stdResponse.setCode(200);
		stdResponse.setStatus("success");
		stdResponse.setMessage("Role granted");
		stdResponse.setElement(user);
		return stdResponse;

	}

	@Override
	public StandardResponse revokerole(UUID userId, UUID roleId) {
		StandardResponse stdResponse = new StandardResponse();
		User user = userRepository.findById(userId);
		Set<RoleUdt> assignedRoles = user.getRoles();
		Role removeRole = roleRepository.findByRoleId(roleId);
		if (departmentRolesRepository.findByRoles(WrapperUtil.roleToRoleUdt(removeRole)) == null) {
			stdResponse.setCode(404);
			stdResponse.setStatus("Failed");
			stdResponse.setMessage("Role not revoked");
		}
		assignedRoles.remove(WrapperUtil.roleToRoleUdt(removeRole));
		user.setRoles(assignedRoles);
		user = userRepository.save(user);
		stdResponse.setCode(200);
		stdResponse.setStatus("success");
		stdResponse.setMessage("Role Revoked");
		stdResponse.setElement(user);
		return stdResponse;
	}

}
