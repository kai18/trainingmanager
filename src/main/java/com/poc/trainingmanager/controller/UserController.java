package com.poc.trainingmanager.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poc.trainingmanager.config.TestAbc;
import com.poc.trainingmanager.config.TrainingmanagerApplication;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;
import com.poc.trainingmanager.service.UserService;

@RestController
@RequestMapping("users/")
public class UserController {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TrainingmanagerApplication.class);

	@Autowired
	TestAbc test;

	@Autowired
	UserService userService;

	/*
	 * @GetMapping("/")
	 * 
	 * @ResponseBody public Long test() { LOGGER.info("INFO LOGGING");
	 * LOGGER.debug("Debug logging"); return test.test();
	 * 
	 * }
	 */

	@CrossOrigin()
	@GetMapping("search")
	public StandardResponse<List<UserSearchWrapper>> search(
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "departments", required = false) String departments,
			@RequestParam(value = "roles", required = false) String roles) {

		LOGGER.info("Searching using given parameters");
		System.out.println(firstName);
		return userService.search(firstName, lastName, email, departments, roles);

	}

	@RequestMapping(method = RequestMethod.POST)
	StandardResponse<User> insert(@RequestBody User user) {
		return userService.insert(user);
	}

	@RequestMapping(method = RequestMethod.PUT)
	StandardResponse<User> update(@RequestBody User user) {
		return userService.update(user);
	}

	@PutMapping(value = "grant/{roleId}/user/{userId}")
	StandardResponse<User> grantRole(@PathVariable("userId") String userId, @PathVariable("roleId") String roleId) {
		LOGGER.error(userId);
		return userService.grantRole(userId, roleId);
	}

	@PutMapping(value = "revoke/{roleId}/user/{userId}")
	StandardResponse<User> revokeRole(@PathVariable("userId") String userId, @PathVariable("roleId") String roleId) {
		LOGGER.error(userId);
		return userService.revokeRole(userId, roleId);
	}
	
	@GetMapping(value = "{userId}")
	StandardResponse<UserSearchWrapper> getUserById(@PathVariable("userId") String userId) {
		LOGGER.error(userId);
		return userService.getUserById(userId);
	}
}
