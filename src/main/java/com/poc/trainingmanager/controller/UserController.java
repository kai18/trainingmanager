package com.poc.trainingmanager.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poc.trainingmanager.config.TestAbc;
import com.poc.trainingmanager.config.TrainingmanagerApplication;
import com.poc.trainingmanager.model.StandardResponse;

@Controller
public class UserController {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TrainingmanagerApplication.class);

	@Autowired
	TestAbc test;

	@GetMapping("/")
	@ResponseBody
	public Long test() {
		LOGGER.info("INFO LOGGING");
		LOGGER.debug("Debug logging");
		return test.test();

	}

	@GetMapping("search")
	@ResponseBody
	public StandardResponse search() {
		return null;

	}

}
