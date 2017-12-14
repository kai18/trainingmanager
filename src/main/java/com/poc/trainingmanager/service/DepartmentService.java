package com.poc.trainingmanager.service;
import java.util.List;

import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.StandardResponse;
import org.springframework.stereotype.Service;

@Service
public interface DepartmentService {
	
	public StandardResponse<List<Department>> getAllDepartments();
	public StandardResponse<Department> addDepartment(Department department);
	public StandardResponse<Department> updateDepartment(Department department);
	public StandardResponse deleteDepartment(Department department);
}
