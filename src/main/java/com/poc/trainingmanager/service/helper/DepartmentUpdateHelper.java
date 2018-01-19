package com.poc.trainingmanager.service.helper;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.DepartmentUsers;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.DepartmentUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;

@Service
public class DepartmentUpdateHelper {

	@Autowired
	DepartmentRepository departmentRepository;

	@Autowired
	DepartmentUsersRepository departmentUsersRepository;

	@Autowired
	UserRepository userRepository;

	public Department updateDepartment(Department oldDepartment, Department newDepartment) {
		Date date = new Date();
		Department updatedDepartment = oldDepartment;
		// setting the new name and description to update
		updatedDepartment.setDepartmentDescription(newDepartment.getDepartmentDescription());
		updatedDepartment.setDepartmentName(newDepartment.getDepartmentName());
		updatedDepartment.setDepartmentUpdatedDtm(date);
		return departmentRepository.save(updatedDepartment);
	}

	public void updateDepartmentUsers(Department oldDepartment, Department department) {
		User user = new User();
		DepartmentUdt oldDepartmentUdt = WrapperUtil.departmentToDepartmentUdt(oldDepartment);
		Set<UserUdt> userList = new HashSet<UserUdt>();
		DepartmentUsers departmentUsers = new DepartmentUsers();
		departmentUsers = departmentUsersRepository.findByDepartmentId(department.getDepartmentId());
		if (departmentUsers != null) {
			userList = departmentUsers.getUserDepartmentsUdt();
			if (userList != null) {

				Set<UserUdt> newUserUdts = new HashSet<UserUdt>();
				for (Iterator<UserUdt> userUdtIterator = userList.iterator(); userUdtIterator.hasNext();) {
					UserUdt userUdt = userUdtIterator.next();
					user = userRepository.findById(userUdt.getId());
					Set<DepartmentUdt> departmentList = new HashSet<DepartmentUdt>();
					departmentList = userUdt.getDepartments();
					departmentList.remove(oldDepartmentUdt);
					departmentList.add(WrapperUtil.departmentToDepartmentUdt(department));
					userUdt.setDepartments(departmentList);
					user.setDepartments(departmentList);
					userRepository.save(user);
					newUserUdts.add(userUdt);
				}
				departmentUsers.setUserDepartmentsUdt(newUserUdts);
				departmentUsersRepository.save(departmentUsers);
			}
		}
	}

}
