package com.poc.trainingmanager.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.constants.Constants;
import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
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
		userRepository.save(user);
		standardResponse.setStatus(Constants.SUCCESS);
		standardResponse.setCode(200);
		standardResponse.setElement(user);
		standardResponse.setMessage("User added successfully");
		logger.info("User {" + user.getEmailId() + "} successfully added");
		return standardResponse;
	}

	@Override
	public StandardResponse<User> update(User user) {
		Date date = new Date();
		StandardResponse<User> stdResponse = new StandardResponse<User>();
		User oldUser = userRepository.findById(user.getId());
		user.setUpdatedDtm(date);
		userRepository.save(WrapperUtil.wrappedUserToUser(user, oldUser));
		stdResponse.setStatus(Constants.SUCCESS);
		stdResponse.setCode(200);
		stdResponse.setElement(user);
		stdResponse.setMessage("User updated successfully");
		return stdResponse;
	}

	@Override
	public StandardResponse<User> grantrole(UUID userId, UUID roleId) {
		Date date = new Date();
		StandardResponse<User> stdResponse = new StandardResponse<User>();
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
		user.setUpdatedDtm(date);
		user = userRepository.save(user);
		stdResponse.setCode(200);
		stdResponse.setStatus("success");
		stdResponse.setMessage("Role granted");
		stdResponse.setElement(user);
		return stdResponse;

	}

	@Override
	public StandardResponse<User> revokerole(UUID userId, UUID roleId) {
		Date date = new Date();
		StandardResponse<User> stdResponse = new StandardResponse<User>();
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
		user.setUpdatedDtm(date);
		user = userRepository.save(user);
		stdResponse.setCode(200);
		stdResponse.setStatus("success");
		stdResponse.setMessage("Role Revoked");
		stdResponse.setElement(user);
		return stdResponse;
	}

}
