package com.poc.trainingmanager.controller;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.poc.trainingmanager.config.TestAbc;
import com.poc.trainingmanager.config.TrainingmanagerApplication;
import com.poc.trainingmanager.model.StandardResponse;
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
	public StandardResponse search(@RequestParam Map<String, String> searchParameters) {
		LOGGER.info("Searching using given parameters");
		String firstName = searchParameters.get("firstName");
		System.out.println(firstName);
		StandardResponse searchResponse = new StandardResponse();

		searchResponse.setMessage("Testing");
		userService.search(searchParameters);
		return searchResponse;

	}

}
