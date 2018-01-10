package com.poc.trainingmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;

@Service
public interface DepartmentService {

	public StandardResponse<List<Department>> getAllDepartments();

	public StandardResponse<Department> addDepartment(Department department, LoggedInUserWrapper loggedInUser);

	public StandardResponse<Department> updateDepartment(Department department, LoggedInUserWrapper loggedInUser);

	public StandardResponse deleteDepartment(String departmentId, LoggedInUserWrapper loggedInUser);
}
