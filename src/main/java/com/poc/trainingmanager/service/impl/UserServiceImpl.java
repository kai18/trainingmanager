package com.poc.trainingmanager.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.constants.Constants;
import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.search.SearchEngine;
import com.poc.trainingmanager.service.UserService;
import com.poc.trainingmanager.utils.PasswordUtil;

/**
 * //TODO
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	DepartmentRepository departmentRespository;

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
	public StandardResponse<List<UserSearchWrapper>> search(String firstName, String lastName, String email,
			String departments, String roles) {

		List<String> departmentNameList = null;
		List<String> roleNameList = null;
		List<Department> departmentList = new ArrayList<Department>();
		List<Role> roleList = new ArrayList<Role>();

		List<RoleUdt> roleUdtList = null;
		List<DepartmentUdt> departmentUdtList = null;

		if (departments != null) {
			departmentNameList = Arrays.asList(departments.split(","));
			for (String departmentName : departmentNameList) {
				departmentList.add(departmentRespository.findByDepartmentName(departmentName));

			}

			departmentUdtList = WrapperUtil.departmentToDepartmentUdt(departmentList);

		}

		if (roles != null) {
			roleNameList = Arrays.asList(roles.split(","));
			for (String roleName : roleNameList) {
				roleList.add(roleRepository.findByRoleName(roleName));
			}

			roleUdtList = WrapperUtil.roleToRoleUdt(roleList);

		}

		List<User> unwrappedResults = searchEngine.searchByAllParameters(email, firstName, lastName, departmentUdtList,
				roleUdtList);

		List<UserSearchWrapper> wrappedResult = WrapperUtil.wrapUserToUserSearchWrapper(unwrappedResults);

		StandardResponse<List<UserSearchWrapper>> searchResponse = new StandardResponse<List<UserSearchWrapper>>();
		System.out.println(wrappedResult);
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

		/*
		 * setting the current user's userRolesUdt that is to be added to the set in
		 * RoleUsers fetch an entry from roleUsers table with the role id obtained from
		 * the user
		 */
		RoleUsers roleUsers = roleUsersRepository.findByRoleId(user.getRoles().iterator().next().getRoleId());

		// add this user to the set of users in the roleUsers mapping that role
		Set<UserUdt> userList = new HashSet<UserUdt>();
		if (roleUsers != null) {
			userRepository.save(user); // insert user only if the role exists
			userList.add(WrapperUtil.userToUserUdt(user));
			// set the updated set of users belonging to that role
			roleUsers.setUserRolesUdt(userList);
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
		User tempUser = new User();
		BeanUtils.copyProperties(oldUser, tempUser);
		User newUser = WrapperUtil.wrappedUserToUser(user, oldUser);
		userRepository.save(newUser);
		// these are the set of roles this user belongs to
		Set<RoleUdt> roleUdtList = oldUser.getRoles();

		Set<UserUdt> userUdtList = new HashSet<UserUdt>();
		if (roleUdtList != null) {
			// for each role search for the user and replace with updated user
			for (int i = 0; i < roleUdtList.size(); i++) {
				role = roleUdtList.iterator().next();
				roleUsers = roleUsersRepository.findByRoleId(role.getRoleId());
				userUdtList = roleUsers.getUserUdt();
				userUdtList.remove(WrapperUtil.userToUserUdt(tempUser));
				userUdtList.add(WrapperUtil.userToUserUdt(newUser));
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
	public StandardResponse<User> grantRole(String uId, String rId) {
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
			if (userUdtList == null)
				userUdtList = new HashSet<UserUdt>();
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
	public StandardResponse<User> revokeRole(String uId, String rId) {
		StandardResponse<User> stdResponse = new StandardResponse<User>();
		UUID userId = UUID.fromString(uId);
		UUID roleId = UUID.fromString(rId);
		User user = userRepository.findById(userId);
		Role role = new Role();
		role = roleRepository.findByRoleId(roleId);
		Date date = new Date();

		RoleUsers roleUsers = roleUsersRepository.findByRoleId(role.getRoleId());
		Set<UserUdt> userUdtList = roleUsers.getUserUdt();
		if (userUdtList == null)
			userUdtList = new HashSet<UserUdt>();
		userUdtList.remove(WrapperUtil.userToUserUdt(user));
		roleUsers.setUserRolesUdt(userUdtList);
		roleUsersRepository.save(roleUsers);

		Set<RoleUdt> roleUdtList = user.getRoles();
		roleUdtList.remove(WrapperUtil.roleToRoleUdt(role));
		user.setRoles(roleUdtList);
		user.setUpdatedDtm(date);
		userRepository.save(user);
		stdResponse.setCode(200);
		stdResponse.setStatus("success");
		stdResponse.setMessage("Role Revoked");
		stdResponse.setElement(user);
		return stdResponse;
	}
	
	@Override
	public StandardResponse<UserSearchWrapper> getUserById(String id) {
		UUID userId = UUID.fromString(id);
		UserSearchWrapper wrappedResult = WrapperUtil.wrapUserToUserSearchWrapper(userRepository.findById(userId));
		StandardResponse<UserSearchWrapper> searchResponse = new StandardResponse<UserSearchWrapper>();
		searchResponse.setElement(wrappedResult);
		searchResponse.setCode(200);
		searchResponse.setStatus("success");
		searchResponse.setMessage("User fetched");
		return searchResponse;
	}
}
