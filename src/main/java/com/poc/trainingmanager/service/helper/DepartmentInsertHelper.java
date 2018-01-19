package com.poc.trainingmanager.service.helper;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.DepartmentUsers;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.DepartmentUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;

@Service
public class DepartmentInsertHelper {

	@Autowired
	DepartmentRepository departmentRepository;

	@Autowired
	DepartmentRolesRepository departmentRolesRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	DepartmentUsersRepository departmentUsersRepository;

	public Department insertIntoDepartment(Department department) {
		Date date = new Date();
		department.setDepartmentId(UUID.randomUUID());
		department.setDepartmentCreatedDtm(date);
		department.setDepartmentUpdatedDtm(date);
		return departmentRepository.save(department);
	}

	public void insertIntoDepartmentUsers(Department department) {
		DepartmentUsers departmentUsers = new DepartmentUsers();
		departmentUsers.setDepartmentId(department.getDepartmentId());
		departmentUsers.setUserDepartmentsUdt(null);
		departmentUsersRepository.save(departmentUsers);
	}

	public void insertIntoDepartmentRoles(Department department) {
		DepartmentRoles departmentRoles = new DepartmentRoles();
		departmentRoles.setDepartmentId(department.getDepartmentId());
		departmentRoles.setRoles(null);
		departmentRolesRepository.save(departmentRoles);

	}

}
