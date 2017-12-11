package com.poc.trainingmanager.config;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.AddressUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.UserRepository;

@Service
public class TestAbc {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	DepartmentRepository departmentRepository;

	public Long test() {
		Long count = userRepository.count();
		System.out.println(roleRepository.count());

		Set<RoleUdt> roles = new HashSet<RoleUdt>();
		Role role1 = roleRepository.findByRoleId(UUID.fromString("a1f4a0ed-faa7-44f7-8383-50f8feaa23d5"));
		if (role1 != null) {
			System.out.println("Success");
		}
		RoleUdt roleUdt = new RoleUdt();

		BeanUtils.copyProperties(role1, roleUdt);
		roleUdt.setRoleId(role1.getRoleId());
		roles.add(roleUdt);
		System.out.println(roleUdt.getRoleName());

		User user = new User();
		user.setRoles(roles);

		// Set<DepartmentUdt> departments = new HashSet<DepartmentUdt>();

		// Department department = new Department();
		// department = departmentRepository.findByDepartmentId("")

		user.setFirstName("Kaustubh");
		user.setLastName("Kaustubh");
		user.setEmailId("kaustubh@kaustubh.com");
		user.setPhoneNumber("8126422399");
		user.setId(UUID.randomUUID());
		user.setPassword("cc03e747a6afbbcbf8be7668acfebee5");
		user.setGender("male");
		user.setIsActive(true);
		// user.setDepartments(departments);

		AddressUdt address = new AddressUdt();
		address.setArea("Mahadevpura");
		address.setCity("Bangalore");
		address.setDoorNumber("105");
		address.setState("Karnataka");
		address.setCountry("India");
		address.setZipcode("560048");
		address.setStreetName("Mahadevpura");

		user.setAddress(address);

		// userRepository.save(user);
		User user1 = userRepository.findById(UUID.fromString("fd9a878f-a923-4e70-955d-ce42d0dccadf"));
		System.out.println(user1);
		return count;
	}

}
