package com.poc.trainingmanager.service.impl;

import java.util.Date;
import java.util.HashSet;
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
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
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
	RoleUsersRepository roleUsersRepository;

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
	public StandardResponse<User> insert(User user) {
		Date date = new Date();
		StandardResponse<User> standardResponse = new StandardResponse<User>();
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
		user.setCreatedDtm(date);
		user.setUpdatedDtm(date);

		// setting the current user's userRolesUdt that is to be added to the set in
		// RoleUsers
		// fetch an entry from roleUsers table with the role id obtained from the user
		RoleUsers roleUsers = roleUsersRepository.findByRoleId(user.getRoles().iterator().next().getRoleId());
		// add this user to the set of users in the roleUsers mapping that role
		Set<UserUdt> userList = new HashSet<UserUdt>();
		if (roleUsers != null) {
			userRepository.save(user); // insert user only if the role exists
			userList.add(WrapperUtil.userToUserUdt(user));
			// set the updated set of users belonging to that role
			roleUsers.setUserRolesUdt(userList);
			;
			roleUsersRepository.save(roleUsers);
			standardResponse.setStatus(Constants.SUCCESS);
			standardResponse.setCode(200);
			standardResponse.setElement(user);
			standardResponse.setMessage("User added successfully");
			logger.info("User {" + user.getEmailId() + "} successfully added");
		}
		return standardResponse;
	}

	@Override
	public StandardResponse<User> update(User user) {
		Date date = new Date();
		RoleUdt role = new RoleUdt();
		RoleUsers roleUsers = new RoleUsers();
		StandardResponse<User> stdResponse = new StandardResponse<User>();
		User oldUser = userRepository.findById(user.getId());
		user.setUpdatedDtm(date);
		userRepository.save(WrapperUtil.wrappedUserToUser(user, oldUser));
		// these are the set of roles this user belongs to
		Set<RoleUdt> roleUdtList = oldUser.getRoles();

		Set<UserUdt> userUdtList = new HashSet<UserUdt>();
		if (roleUdtList != null) {
			// for each role search for the user and replace with updated user
			for (int i = 0; i < roleUdtList.size(); i++) {
				role = roleUdtList.iterator().next();
				roleUsers = roleUsersRepository.findByRoleId(role.getRoleId());
				userUdtList = roleUsers.getUserUdt();
				userUdtList.remove(WrapperUtil.userToUserUdt(oldUser));
				userUdtList.add(WrapperUtil.userToUserUdt(user));
				roleUsers.setUserRolesUdt(userUdtList);
				roleUsersRepository.save(roleUsers);
			}
			stdResponse.setStatus(Constants.SUCCESS);
			stdResponse.setCode(200);
			stdResponse.setElement(user);
			stdResponse.setMessage("User updated successfully");
		}
		return stdResponse;
	}

	@Override
	public StandardResponse<User> grantrole(String uId, String rId) {
		StandardResponse<User> stdResponse = new StandardResponse<User>();
		UUID userId = UUID.fromString(uId);
		UUID roleId = UUID.fromString(rId);
		User user = userRepository.findById(userId);
		Role role = new Role();
		role = roleRepository.findByRoleId(roleId);
		Date date = new Date();
		Set<RoleUdt> roleUdtList = user.getRoles();

		if (role != null) {
			roleUdtList.add(WrapperUtil.roleToRoleUdt(role));
			user.setRoles(roleUdtList);
			user.setUpdatedDtm(date);
			userRepository.save(user);

			RoleUsers roleUsers = roleUsersRepository.findByRoleId(role.getRoleId());
			Set<UserUdt> userUdtList = roleUsers.getUserUdt();
			userUdtList.add(WrapperUtil.userToUserUdt(user));
			roleUsers.setUserRolesUdt(userUdtList);
			roleUsersRepository.save(roleUsers);
			stdResponse.setCode(200);
			stdResponse.setStatus("success");
			stdResponse.setMessage("Role granted");
			stdResponse.setElement(user);
			return stdResponse;
		}
		stdResponse.setCode(400);
		stdResponse.setStatus("failed");
		stdResponse.setMessage("Role not granted");
		return stdResponse;
	}

	@Override
	public StandardResponse<User> revokerole(String uId, String rId) {
		StandardResponse<User> stdResponse = new StandardResponse<User>();
		UUID userId = UUID.fromString(uId);
		UUID roleId = UUID.fromString(rId);
		User user = userRepository.findById(userId);
		Role role = new Role();
		role = roleRepository.findByRoleId(roleId);
		Date date = new Date();

		RoleUsers roleUsers = roleUsersRepository.findByRoleId(role.getRoleId());
		Set<UserUdt> userUdtList = roleUsers.getUserUdt();
		userUdtList.remove(WrapperUtil.userToUserUdt(user));
		roleUsers.setUserRolesUdt(userUdtList);
		roleUsersRepository.save(roleUsers);

		Set<RoleUdt> roleUdtList = user.getRoles();
		roleUdtList.remove(role);
		user.setRoles(roleUdtList);
		user.setUpdatedDtm(date);
		userRepository.save(user);
		stdResponse.setCode(200);
		stdResponse.setStatus("success");
		stdResponse.setMessage("Role Revoked");
		stdResponse.setElement(user);
		return stdResponse;
	}
}
