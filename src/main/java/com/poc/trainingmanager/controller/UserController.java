package com.poc.trainingmanager.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;
import com.poc.trainingmanager.service.UserService;

@RestController
@RequestMapping("users/")
public class UserController {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@CrossOrigin()
	@GetMapping("search")
	public StandardResponse<List<UserSearchWrapper>> search(
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "departments", required = false) String departments,
			@RequestParam(value = "roles", required = false) String roles) {
		LOGGER.info("Searching using given parameters");
		return userService.search(firstName, lastName, email, departments, roles);

	}

	@CrossOrigin()
	@RequestMapping(method = RequestMethod.POST)
	public StandardResponse<User> insert(@RequestBody User user) {
		return userService.insert(user);
	}

	@CrossOrigin()
	@RequestMapping(method = RequestMethod.PUT)
	public StandardResponse<User> update(@RequestBody User user,
			@RequestAttribute("loggedInUser") LoggedInUserWrapper loggedInUser) {
		return userService.update(user);
	}

	@PutMapping(value = "grant/{roleId}/user/{userId}")
	public StandardResponse<User> grantRole(@PathVariable("userId") String userId,
			@PathVariable("roleId") String roleId, @RequestAttribute("loggedInUser") LoggedInUserWrapper loggedInUser) {
		LOGGER.error(userId);
		return userService.grantRole(userId, roleId);
	}

	@PutMapping(value = "revoke/{roleId}/user/{userId}")
	public StandardResponse<User> revokeRole(@PathVariable("userId") String userId,
			@PathVariable("roleId") String roleId, @RequestAttribute("loggedInUser") LoggedInUserWrapper loggedInUser) {
		LOGGER.error(userId);
		return userService.revokeRole(userId, roleId);
	}

	@GetMapping(value = "{userId}")
	@CrossOrigin
	public StandardResponse<UserSearchWrapper> getUserById(@PathVariable("userId") String userId) {
		LOGGER.info("Attempting to get user with Id " + userId);
		return userService.getUserById(userId);
	}

	@DeleteMapping(value = "{userId}")
	@CrossOrigin
	public StandardResponse<?> deleteUser(@PathVariable("userId") String userId,
			@RequestAttribute("loggedInUser") LoggedInUserWrapper loggedInUser) {
		LOGGER.info("Attempting to delete user with id " + userId);
		return userService.deleteUser(userId, loggedInUser);
	}

	@GetMapping(value = "department/departmentRoles/{userId}")
	public StandardResponse getAvailableRoles(@PathVariable("userId") String userId) {
		return userService.getRolesByDepartment(userId);
	}

}
