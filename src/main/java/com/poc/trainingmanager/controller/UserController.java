package com.poc.trainingmanager.controller;

import java.util.Map;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.poc.trainingmanager.config.TestAbc;
import com.poc.trainingmanager.config.TrainingmanagerApplication;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;
import com.poc.trainingmanager.service.UserService;

@RestController
public class UserController {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TrainingmanagerApplication.class);

	@Autowired
	TestAbc test;

	@Autowired
	UserService userService;

	@GetMapping("/")
	@ResponseBody
	public Long test() {
		LOGGER.info("INFO LOGGING");
		LOGGER.debug("Debug logging");
		return test.test();

	}

	@GetMapping("search")
	public StandardResponse<UserSearchWrapper> search(@RequestParam Map<String, String> searchParameters) {
		LOGGER.info("Searching using given parameters");
		String firstName = searchParameters.get("firstName");
		System.out.println(firstName);
		return userService.search(searchParameters);

	}
	
	@RequestMapping(method=RequestMethod.POST)
	StandardResponse<User> insert(@RequestBody User user) {
		return userService.insert(user);
	}
	
	@GetMapping("{userId}/grantrole/{roleId}")
    @RequestMapping(method=RequestMethod.PUT)
    StandardResponse<User> grantRole(@RequestParam UUID userId,@RequestParam UUID roleId){
           return userService.grantrole(userId, roleId);
    }
    
//    @GetMapping("{userId}/revokerole/{roleId}")
//    @RequestMapping(method=RequestMethod.PUT)
//    StandardResponse<User> revokeRole(@RequestParam UUID userId,@RequestParam UUID roleId){
//           return userService.revokerole(userId, roleId);
//    }
}
