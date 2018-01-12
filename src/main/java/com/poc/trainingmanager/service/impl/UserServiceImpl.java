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
import com.poc.trainingmanager.exceptions.AccessDeniedException;
import com.poc.trainingmanager.exceptions.DuplicateDataException;
import com.poc.trainingmanager.exceptions.ResourceNotFoundException;
import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.DepartmentUsers;
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
import com.poc.trainingmanager.repository.DepartmentUsersRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.search.SearchEngine;
import com.poc.trainingmanager.service.UserService;
import com.poc.trainingmanager.service.helper.UserDeleteHelper;
import com.poc.trainingmanager.service.helper.UserInsertHelper;
import com.poc.trainingmanager.service.helper.UserUpdateHelper;
import com.poc.trainingmanager.utils.FieldValidator;
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
	DepartmentUsersRepository departmentUsersRepository;

	@Autowired
	PrivilegeChecker privilegeChecker;

	@Autowired
	SearchEngine searchEngine;

	@Autowired
	UserDeleteHelper deleteHelper;

	@Autowired
	UserInsertHelper insertHelper;

	@Autowired
	UserUpdateHelper userUpdateHelper;

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
	 * 
	 * @author Spoorthi.A Insert method is used to insert a new user who is trying
	 *         to register into the database. For the insert operation the user
	 *         should not be null. Also, the user should not already exist in the
	 *         table. The unique constraint is set for Email Id. User ID is randomly
	 *         generated and the password is hashed before storing into the
	 *         database.
	 *         </p>
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
		if (userRepository.findByEmailId(user.getEmailId()) != null)
			throw new DuplicateDataException("User with this email Id already exists. Duplicate data found");

		FieldValidator.validateForUserInsert(user);

		user.setId(UUID.randomUUID());
		user.setPassword(PasswordUtil.getPasswordHash(user.getPassword()));
		user.setCreatedDtm(date);
		user.setUpdatedDtm(date);
		user.setIsActive(true);

		// setting user's role from role table
		RoleUdt rUdt = WrapperUtil
				.roleToRoleUdt(roleRepository.findByRoleId(user.getRoles().iterator().next().getRoleId()));
		Set<RoleUdt> rUdtSet = new LinkedHashSet<RoleUdt>();
		rUdtSet.add(rUdt);
		user.setRoles(rUdtSet);

		// setting user's department from department table
		DepartmentUdt dUdt = null;

		if (user.getDepartments() != null)
			dUdt = WrapperUtil.departmentToDepartmentUdt(departmentRespository
					.findByDepartmentId(user.getDepartments().iterator().next().getDepartmentId()));

		if (dUdt != null) {
			Set<DepartmentUdt> dUdtSet = new HashSet<DepartmentUdt>();
			dUdtSet.add(dUdt);
			user.setDepartments(dUdtSet);
		}

		insertHelper.insertUser(user);
		insertHelper.insertIntoRoleUsers(user);
		insertHelper.insertIntoDepartmentUsers(user);

		standardResponse.setStatus(Constants.SUCCESS);
		standardResponse.setCode(200);
		standardResponse.setElement(user);
		standardResponse.setMessage("User added successfully");
		LOGGER.info("User {" + user.getEmailId() + "} successfully added");
		return standardResponse;
	}

	@Override
	public StandardResponse<User> update(User userToBeUpdated, LoggedInUserWrapper loggedInUserWrapper) {
		StandardResponse<User> stdResponse = new StandardResponse<User>();

		User oldUser = userRepository.findById(userToBeUpdated.getId());

		if (oldUser == null)
			throw new ResourceNotFoundException("User to be updated does not exist, please first register the user");

		User updatedUser = userUpdateHelper.updateUserInUser(oldUser, userToBeUpdated);

		userUpdateHelper.updateUserInDepartmentUsers(oldUser, updatedUser);

		userUpdateHelper.updateUserInRoleUsers(oldUser, updatedUser);

		stdResponse.setStatus(Constants.SUCCESS);
		stdResponse.setCode(200);
		stdResponse.setElement(updatedUser);
		stdResponse.setMessage("User updated successfully");

		return stdResponse;
	}

	@Override
	public StandardResponse<User> grantRole(String uId, String rId, LoggedInUserWrapper loggedInUser) {

		this.privilegeChecker.isAllowedToEditRole(loggedInUser.getPrivileges());

		StandardResponse<User> stdResponse = new StandardResponse<User>();
		UUID userId = UUID.fromString(uId);
		UUID roleId = UUID.fromString(rId);
		User user = userRepository.findById(userId);
		Role role = new Role();
		role = roleRepository.findByRoleId(roleId);
		Date date = new Date();
		Set<RoleUdt> roleUdtList = new HashSet<RoleUdt>();
		if (user.getRoles() != null)
			roleUdtList = user.getRoles();

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
			stdResponse.setStatus(Constants.SUCCESS);
			stdResponse.setMessage("Role granted");
			stdResponse.setElement(user);
			return stdResponse;
		}
		stdResponse.setCode(400);
		stdResponse.setStatus(Constants.ERROR);
		stdResponse.setMessage("Role not granted");
		return stdResponse;
	}

	@Override
	public StandardResponse<User> revokeRole(String uId, String rId, LoggedInUserWrapper loggedInUser) {

		this.privilegeChecker.isAllowedToEditRole(loggedInUser.getPrivileges());

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
		stdResponse.setStatus(Constants.SUCCESS);
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
		searchResponse.setStatus(Constants.SUCCESS);
		searchResponse.setMessage("User fetched");
		return searchResponse;
	}

	/**
	 * Deletion has to be carried on multiple tables. User entries are deleted from
	 * department_users table, role_users table and finally isActive is set to false
	 * in the users table.
	 */
	@Override
	public StandardResponse<Object> deleteUser(String userId, LoggedInUserWrapper loggedInUser) {

		UUID userUuid = UUID.fromString(userId);
		User user = userRepository.findById(userUuid);

		this.privilegeChecker.isAllowedToDeleteUser(user.getDepartments(), loggedInUser.getPrivileges());

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

		StandardResponse<Object> deleteResponse = new StandardResponse<Object>();
		deleteResponse.setCode(200);
		deleteResponse.setElement(null);
		deleteResponse.setStatus(Constants.SUCCESS);
		deleteResponse.setMessage("User successfully deleted");

		return deleteResponse;

	}

	@Override
	public StandardResponse<Set<RoleUdt>> getRolesByDepartment(String userId) {
		StandardResponse<Set<RoleUdt>> stdResponse = new StandardResponse<Set<RoleUdt>>();
		User user = userRepository.findById(UUID.fromString(userId));

		Set<RoleUdt> availableRoles = new HashSet<RoleUdt>();
		DepartmentRoles departmentRoles = new DepartmentRoles();
		if (user != null) {
			Set<DepartmentUdt> deptList = user.getDepartments();
			if (deptList != null) {
				for (DepartmentUdt dept : deptList) {
					departmentRoles = departmentRolesRepository.findByDepartmentId(dept.getDepartmentId());
					if (departmentRoles.getRoles() != null)
						availableRoles.addAll(departmentRoles.getRoles());
				}
				if (user.getRoles() != null)
					availableRoles.removeAll(user.getRoles());
			}
			stdResponse.setCode(200);
			stdResponse.setStatus(Constants.SUCCESS);
			stdResponse.setMessage("Roles Fetched succesfuly");
			stdResponse.setElement(availableRoles);
			return stdResponse;
		}

		stdResponse.setCode(400);
		stdResponse.setStatus(Constants.ERROR);
		stdResponse.setMessage("User cant be null");
		return stdResponse;
	}

	@Override
	public StandardResponse<Object> addUserToDepartment(String userId, String departmentId,
			LoggedInUserWrapper loggedInUser) {

		if (!this.privilegeChecker.isSuperAdmin(loggedInUser.getPrivileges())) {
			throw new AccessDeniedException("You are not an administrator");
		}

		User user = userRepository.findById(UUID.fromString(userId));
		if (user == null)
			throw new ResourceNotFoundException("User with id " + userId + "does not exist");

		Department department = departmentRespository.findByDepartmentId(UUID.fromString(departmentId));
		if (department == null)
			throw new ResourceNotFoundException("Department with id " + departmentId + "does not exist");

		DepartmentUsers departmentUsers = departmentUsersRepository.findByDepartmentId(UUID.fromString(departmentId));
		if (departmentUsers == null)
			throw new ResourceNotFoundException("Department with id " + departmentId + "does not exist");

		Set<DepartmentUdt> departments = user.getDepartments();
		if (departments == null)
			departments = new HashSet<DepartmentUdt>();

		if (departments.contains(WrapperUtil.departmentToDepartmentUdt(department))) {
			throw new DuplicateDataException("Department Already allocated to user");
		}
		departments.add(WrapperUtil.departmentToDepartmentUdt(department));
		user.setDepartments(departments);

		Set<UserUdt> users = departmentUsers.getUserDepartmentsUdt();
		if (users == null)
			users = new HashSet<UserUdt>();
		users.add(WrapperUtil.userToUserUdt(user));
		departmentUsers.setUserDepartmentsUdt(users);

		userRepository.save(user);
		departmentUsersRepository.save(departmentUsers);

		StandardResponse<Object> response = new StandardResponse<Object>();
		response.setCode(200);
		response.setMessage("Department successfully alloted");
		response.setStatus(Constants.SUCCESS);
		return response;
	}

	@Override
	public StandardResponse<Object> removeUserFromDepartment(String userId, String departmentId,
			LoggedInUserWrapper loggedInUser) {

		if (!this.privilegeChecker.isSuperAdmin(loggedInUser.getPrivileges())) {
			throw new AccessDeniedException("You are not an administrator");
		}

		User user = userRepository.findById(UUID.fromString(userId));
		if (user == null)
			throw new ResourceNotFoundException("User with id " + userId + "does not exist");

		Department department = departmentRespository.findByDepartmentId(UUID.fromString(departmentId));
		if (department == null)
			throw new ResourceNotFoundException("Department with id " + departmentId + "does not exist");

		DepartmentUsers departmentUsers = departmentUsersRepository.findByDepartmentId(UUID.fromString(departmentId));
		if (departmentUsers == null)
			throw new ResourceNotFoundException("Department with id " + departmentId + "does not exist");

		Set<DepartmentUdt> departments = user.getDepartments();
		if (departments == null) {
			departments = new HashSet<DepartmentUdt>();
			throw new ResourceNotFoundException("Specified department does not exist for the specified user");
		}
		departments.remove(WrapperUtil.departmentToDepartmentUdt(department));
		user.setDepartments(departments);

		Set<UserUdt> users = departmentUsers.getUserDepartmentsUdt();
		if (users == null) {
			users = new HashSet<UserUdt>();
			throw new ResourceNotFoundException("Specified department does not exist for the specified user");

		}

		users.remove(WrapperUtil.userToUserUdt(user));
		departmentUsers.setUserDepartmentsUdt(users);

		userRepository.save(user);
		departmentUsersRepository.save(departmentUsers);

		StandardResponse<Object> response = new StandardResponse<Object>();
		response.setCode(200);
		response.setMessage("Department successfully removed");
		response.setStatus(Constants.SUCCESS);
		return response;
	}

}