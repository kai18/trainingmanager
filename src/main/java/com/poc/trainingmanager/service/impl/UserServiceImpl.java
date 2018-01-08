package com.poc.trainingmanager.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.poc.trainingmanager.constants.Constants;
import com.poc.trainingmanager.exceptions.ResourceNotFoundException;
import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.search.SearchEngine;
import com.poc.trainingmanager.service.UserService;
import com.poc.trainingmanager.service.helper.UserDeleteHelper;
import com.poc.trainingmanager.utils.PasswordUtil;
import com.poc.trainingmanager.utils.PrivilegeChecker;

/**
 * @author Kaustubh.Kaustubh
 *
 */
@CrossOrigin()
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

	@Autowired
	UserDeleteHelper deleteHelper;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	/**
	 * 
	 * 
	 * @param firstName
	 *            String: Receives the user's first name as search criteria
	 * @param lastName
	 *            String: Receives the user's last name as search criteria
	 * @param email
	 *            String: Receives the user's email id as search criteria
	 * 
	 * 
	 * @return StandardResponse: Contains search result as a user list along with
	 *         message, status code and error code if any
	 */
	@Override
	public StandardResponse<List<UserSearchWrapper>> search(String firstName, String lastName, String email,
			String departments, String roles) {

		List<String> departmentNameList = null;
		List<String> roleNameList = null;
		List<Department> departmentList = new ArrayList<Department>();
		List<Role> roleList = new ArrayList<Role>();

		List<RoleUdt> roleUdtList = null;
		List<DepartmentUdt> departmentUdtList = null;

		/*
		 * get all departments from the database that the user has specified as search
		 * criteria
		 */
		if (departments != null) {
			departmentNameList = Arrays.asList(departments.split(","));
			for (String departmentName : departmentNameList) {
				departmentList.add(departmentRespository.findByDepartmentName(departmentName));

			}

			departmentUdtList = WrapperUtil.departmentToDepartmentUdt(departmentList);
		}

		/*
		 * get all roles from the database that the user has specified as search
		 * criteria
		 */
		if (roles != null) {
			roleNameList = Arrays.asList(roles.split(","));
			for (String roleName : roleNameList) {
				roleList.add(roleRepository.findByRoleName(roleName));
			}

			System.out.println(roleList.size());

			roleUdtList = WrapperUtil.roleToRoleUdt(roleList);
		}

		long start = System.currentTimeMillis();
		List<User> unwrappedResults = searchEngine.searchByAllParameters(email, firstName, lastName, departmentUdtList,
				roleUdtList);
		long end = System.currentTimeMillis();

		Double timeTakenToSearch = (double) ((end - start) / 1000);// time elapsed between calling the search engine adn
																	// receiving the result

		Set<User> distinctUsers = new LinkedHashSet<User>(unwrappedResults);// Remove duplicate results by converting to
																			// a hash
																			// set that does not allow duplicates and
																			// also preserves the ordering
		List<User> distinctUserList = new ArrayList<User>(distinctUsers);
		List<UserSearchWrapper> wrappedResult = WrapperUtil.wrapUserToUserSearchWrapper(distinctUserList);

		StandardResponse<List<UserSearchWrapper>> searchResponse = new StandardResponse<List<UserSearchWrapper>>();
		searchResponse.setElement(wrappedResult);
		searchResponse.setMessage(wrappedResult.size() + " results found in " + timeTakenToSearch + " seconds");

		return searchResponse;
	}

	/**
	 * <p>
	 * Insert method is used to insert a new user who is trying to register into the
	 * database. For the insert operation the user should not be null. Also, the
	 * user should not already exist in the table. The unique constraint is set for
	 * Email Id. User ID is randomly generated and the password is hashed before
	 * storing into the database.
	 * </p>
	 */
	@Override
	public StandardResponse<User> insert(User user) {
		Date date = new Date();
		StandardResponse<User> standardResponse = new StandardResponse<User>();
		if (user == null) {
			LOGGER.error("Inserted user was null, hence failed to Add user");
			standardResponse.setCode(422);
			standardResponse.setStatus("Failed");
			standardResponse.setMessage("User cant be null, failed to add this user");
			return standardResponse;
		}
		if (userRepository.findByEmailId(user.getEmailId()) != null) {
			LOGGER.error("User {" + user.getEmailId() + "} already exists, duplicate user cannot be inserted");
			standardResponse.setCode(409);
			standardResponse.setStatus("Failed");
			standardResponse.setMessage("user already exists, failed to add this user");
			return standardResponse;
		}
		user.setId(UUID.randomUUID());
		user.setPassword(PasswordUtil.getPasswordHash(user.getPassword()));
		user.setCreatedDtm(date);
		user.setUpdatedDtm(date);

		RoleUdt rUdt = WrapperUtil
				.roleToRoleUdt(roleRepository.findByRoleId(user.getRoles().iterator().next().getRoleId()));
		Set<RoleUdt> rUdtSet = new LinkedHashSet<RoleUdt>();
		rUdtSet.add(rUdt);
		user.setRoles(rUdtSet);

		DepartmentUdt dUdt = WrapperUtil.departmentToDepartmentUdt(
				departmentRespository.findByDepartmentId(user.getDepartments().iterator().next().getDepartmentId()));
		Set<DepartmentUdt> dUdtSet = new LinkedHashSet<DepartmentUdt>();
		dUdtSet.add(dUdt);
		user.setDepartments(dUdtSet);
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
			LOGGER.info("User {" + user.getEmailId() + "} successfully added");
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

		if (oldUser == null)
			throw new ResourceNotFoundException("User to be updated does not exist, please first register the user");

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
				if (userUdtList != null) {
					userUdtList.remove(WrapperUtil.userToUserUdt(tempUser));
					userUdtList.add(WrapperUtil.userToUserUdt(newUser));
				}
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
			for (RoleUdt roleUdt : user.getRoles()) {
				BeanUtils.copyProperties(roleRepository.findByRoleId(roleUdt.getRoleId()), roleUdt);
			}

			for (DepartmentUdt departmentUdt : user.getDepartments()) {
				BeanUtils.copyProperties(departmentRespository.findByDepartmentId(departmentUdt.getDepartmentId()),
						departmentUdt);
			}
			user.setIsActive(true);
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
		User user = userRepository.findById(userId);
		if (user == null)
			throw new ResourceNotFoundException("User with id does not exist");
		UserSearchWrapper wrappedResult = WrapperUtil.wrapUserToUserSearchWrapper(user);
		StandardResponse<UserSearchWrapper> searchResponse = new StandardResponse<UserSearchWrapper>();
		searchResponse.setElement(wrappedResult);
		searchResponse.setCode(200);
		searchResponse.setStatus("success");
		searchResponse.setMessage("User fetched");
		return searchResponse;
	}

	/**
	 * 
	 *
	 */
	@Override
	public StandardResponse deleteUser(String userId, LoggedInUserWrapper loggedInUser) {

		UUID userUuid = UUID.fromString(userId);
		User user = userRepository.findById(userUuid);

		PrivilegeChecker privilegeChecker = new PrivilegeChecker();
		privilegeChecker.checkIAllowedToDelete(loggedInUser.getDepartments(), user.getDepartments());

		if (user == null || !user.getIsActive()) {
			throw new ResourceNotFoundException("Requested user does not exist");
		}

		UserUdt userToBeDeleted = WrapperUtil.userToUserUdt(user);

		// Deleting user from department_users table
		Set<DepartmentUdt> departments = user.getDepartments();
		if (departments != null) {
			for (DepartmentUdt department : departments) {
				deleteHelper.deleteFromDepartmentUsers(department.getDepartmentId(), userToBeDeleted);
			}
		}

		// Deleting user from role_users table
		Set<RoleUdt> roles = user.getRoles();
		if (roles != null) {
			for (RoleUdt role : roles) {
				deleteHelper.deleteFromRoleUsers(role.getRoleId(), userToBeDeleted);
			}
		}

		deleteHelper.deactivateUser(user);// setting user's isActive field to false

		StandardResponse<?> deleteResponse = new StandardResponse();
		deleteResponse.setCode(200);
		deleteResponse.setElement(null);
		deleteResponse.setMessage("User successfully deleted");

		return deleteResponse;

	}
}