package com.poc.trainingmanager.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);
	
	//Service method to fetch all the departments
	public StandardResponse<List<Department>> getAllDepartments() {
		StandardResponse<List<Department>> standardResponse = new StandardResponse<List<Department>>();
		List<Department> allDepartment = departmentRepository.findAll();
		if(allDepartment == null)
		{
			logger.warn("No department found");
			standardResponse.setCode(404);
			standardResponse.setStatus("Success");
			standardResponse.setMessage("No department found");
			return standardResponse;
		}
		logger.info("All departments fetched successfully");
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("All departments fetched");
		standardResponse.setElement(allDepartment);
		return standardResponse;
	}

	@Override
	public StandardResponse<Department> addDepartment(Department department) {
		StandardResponse<Department> standardResponse = new StandardResponse<Department>();
		Date date = new Date();
		if (department==null)
		{
			logger.error("Inserted department was null, hence failed to Add department");
			standardResponse.setCode(400);
			standardResponse.setStatus("Failed");
			standardResponse.setMessage("Department cant by null, failed to add this department");
			return standardResponse;
		}
		if (departmentRepository.findByDepartmentName(department.getDepartmentName()) != null) {
			logger.error("Department {" + department + "} already exists, duplicate department cannot be inserted");
			standardResponse.setCode(409);
			standardResponse.setStatus("Failed");
			standardResponse.setMessage("Department already exists, failed to add this department");
			return standardResponse;
		}

		department.setDepartmentId(UUID.randomUUID());
		department.setDepartmentCreatedDtm(date);
		Department addedDepartment = departmentRepository.save(department);

		DepartmentRoles departmentRoles = new DepartmentRoles();
		departmentRoles.setDepartmentId(department.getDepartmentId());
		departmentRoles.setRoles(null);
		departmentRolesRepository.save(departmentRoles);
		//DepartmentRoles departmentRolesAdded = departmentRolesRepository.save(departmentRoles);
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Department inserted successfully");
		standardResponse.setElement(addedDepartment);
		return standardResponse;
		
	}
	
	@Override
	public StandardResponse<Department> updateDepartment(Department department) {
		StandardResponse<Department> standardResponse = new StandardResponse<Department>();
		Date date = new Date();
		department.setDepartmentUpdatedDtm(date);
		department.setDepartmentCreatedDtm(date);
		Department updatedDepartment = departmentRepository.save(department);
		if(CommonUtils.isStringNull(updatedDepartment.getDepartmentName())||CommonUtils.isStringNull(updatedDepartment.getDepartmentDescription()))
		{
			standardResponse.setMessage("Department name and description cannot be empty");
			return standardResponse;
		}
		
		DepartmentRoles departmentRoles = new DepartmentRoles();
		departmentRoles.setDepartmentId(department.getDepartmentId());
		departmentRolesRepository.save(departmentRoles);
		logger.info("Department {" + department + "} successfully updated");
		logger.info("Department in DepartmentRoles {" + departmentRoles + "} successfully updated");
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
		DepartmentRoles departmentRoles = new DepartmentRoles();
		departmentRoles.setDepartmentId(department.getDepartmentId());
		departmentRolesRepository.delete(departmentRoles);
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Department deleted successfully");
		return null;
	}

} 
