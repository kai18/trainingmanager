package com.poc.trainingmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

	@Autowired
	DepartmentService departmentService;

	@RequestMapping(method = RequestMethod.GET)
	StandardResponse<List<Department>> getAllDepartments() {
		return departmentService.getAllDepartments();
	}

	@RequestMapping(method = RequestMethod.POST)
	StandardResponse<Department> addDepartment(@RequestBody Department department) {
		return departmentService.addDepartment(department);
	}

	@RequestMapping(method = RequestMethod.PUT)
	StandardResponse<Department> updateDepartment(@RequestBody Department department) {
		return departmentService.updateDepartment(department);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	StandardResponse deleteDepartment(@RequestBody Department department) {
		return departmentService.deleteDepartment(department);

	}
}
