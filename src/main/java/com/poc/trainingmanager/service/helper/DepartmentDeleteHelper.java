package com.poc.trainingmanager.service.helper;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.DepartmentUsers;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.DepartmentUsersRepository;

@Service
public class DepartmentDeleteHelper {

	@Autowired
	DepartmentRepository departmentRepository;

	@Autowired
	DepartmentUsersRepository departmentUsersRepository;

	@Autowired
	DepartmentRolesRepository departmentRolesRepository;

	public void deleteFromDepartment(String deptId) {
		// delete from department table
		UUID departmentId = UUID.fromString(deptId);
		departmentRepository.delete(departmentRepository.findByDepartmentId(departmentId));
	}

	public void deleteFromDepartmentUsers(String deptId) {
		UUID departmentId = UUID.fromString(deptId);
		DepartmentUsers departmentUsers = new DepartmentUsers();
		departmentUsers = departmentUsersRepository.findByDepartmentId(departmentId);
		departmentUsersRepository.delete(departmentUsers);
	}

	public void deleteFromDepartmentRoles(String deptId) {
		UUID departmentId = UUID.fromString(deptId);
		DepartmentRoles departmentRoles = new DepartmentRoles();
		departmentRoles = departmentRolesRepository.findByDepartmentId(departmentId);
		departmentRolesRepository.delete(departmentRoles);
	}
}
