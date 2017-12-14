package com.poc.trainingmanager.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.DepartmentService;
import com.poc.trainingmanager.utils.CommonUtils;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	
	@Autowired
	DepartmentRepository departmentRepository;
	@Autowired
	DepartmentRolesRepository departmentRolesRepository;
	@Autowired
	UserRepository userRepository;
	
	//Service method to fetch all the departments
	public StandardResponse<List<Department>> getAllDepartments() {
		StandardResponse<List<Department>> standardResponse = new StandardResponse<List<Department>>();
		List<Department> allDepartment = departmentRepository.findAll();
		if(allDepartment == null)
		{
			return null;
		}
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("All department fetched");
		standardResponse.setElement(allDepartment);
		return standardResponse;
	}

	@Override
	public StandardResponse<Department> addDepartment(Department department) {
		StandardResponse<Department> standardResponse = new StandardResponse<Department>();
		Date date = new Date();
		department.setDepartmentId(UUID.randomUUID());
		department.setDepartmentCreatedDtm(date);
		department.setDepartmentUpdatedDtm(date);
		Department addedDepartment = departmentRepository.save(department);
		if (CommonUtils.isNull(addedDepartment))
		{
			standardResponse.setMessage("Department cannot be empty.");
			return standardResponse;
		}
//		DepartmentRoles departmentRoles = new DepartmentRoles();
//		departmentRoles.setDepartmentId(department.getDepartmentId());
//		departmentRoles.setRoles(null);
//		DepartmentRoles departmentRole = departmentRolesRepository.save(departmentRoles);
//		if(departmentRole.getRoles()==null)
//		{
//			System.out.println("object is null");
//		}
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Department inserted successfully");
		standardResponse.setElement(addedDepartment);
		return standardResponse;
		
	}
	
	@Override
	public StandardResponse<Department> updateDepartment(Department department) {
		StandardResponse<Department> standardResponse = new StandardResponse<Department>();
//		Date date = new Date();
//		department.setDepartmentUpdatedDtm(date);
		Department updatedDepartment = departmentRepository.save(department);
		if(CommonUtils.isStringNull(updatedDepartment.getDepartmentName())||CommonUtils.isStringNull(updatedDepartment.getDepartmentDescription()))
		{
			standardResponse.setMessage("Department name and description cannot be empty");
			return standardResponse;
		}
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Department updated successfully");
		standardResponse.setElement(updatedDepartment);
		return standardResponse;
	}
	
	@Override
	public StandardResponse deleteDepartment(Department department) {
		StandardResponse standardResponse = new StandardResponse();
		departmentRepository.delete(department);
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Department deleted successfully");
		return null;
	}

} 
