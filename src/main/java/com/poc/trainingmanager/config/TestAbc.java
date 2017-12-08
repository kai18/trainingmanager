package com.poc.trainingmanager.config;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.UserRepository;

@Service
public class TestAbc {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;

	public Long test() {
		Long count = userRepository.count();
		List<User> users = (List<User>) userRepository.findAll();
		// System.out.print("Count is being called" + users.get(0));
		System.out.println(roleRepository.count());
		Role role = new Role();
		role.setRoleName("Demo Admin");
		role.setRoleType("Demo");
		role.setRoleId(UUID.randomUUID());

		Role role1 = roleRepository.findByRoleId(UUID.fromString("16ca0260-db25-11e7-bbe4-dd62d06d5395"));
		if (role1 != null)
			System.out.println("Success");
		System.out.println(role1.getRoleDescription());

		return count;
	}

}
