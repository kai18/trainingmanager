package com.poc.trainingmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

	@Autowired
	DepartmentService departmentService;

	@CrossOrigin()
	@RequestMapping(method = RequestMethod.GET)
	StandardResponse<List<Department>> getAllDepartments() {
		return departmentService.getAllDepartments();
	}

	@CrossOrigin()
	@RequestMapping(method = RequestMethod.POST)
	StandardResponse<Department> addDepartment(@RequestBody Department department,
			@RequestAttribute("loggedInUser") LoggedInUserWrapper loggedInUser) {
		return departmentService.addDepartment(department, loggedInUser);
	}

	@CrossOrigin()
	@RequestMapping(method = RequestMethod.PUT)
	StandardResponse<Department> updateDepartment(@RequestBody Department department,
			@RequestAttribute("loggedInUser") LoggedInUserWrapper loggedInUser) {
		return departmentService.updateDepartment(department, loggedInUser);
	}

	@CrossOrigin()
	@DeleteMapping(value = "{departmentId}")
	StandardResponse deleteDepartment(@PathVariable(value = "departmentId") String departmentId,
			@RequestAttribute("loggedInUser") LoggedInUserWrapper loggedInUser) {
		return departmentService.deleteDepartment(departmentId, loggedInUser);

	}
}
