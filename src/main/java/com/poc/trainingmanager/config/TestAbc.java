package com.poc.trainingmanager.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.repository.UserRepository;

@Service
public class TestAbc {

	@Autowired
	UserRepository userRepository;

	public Long test() {
		Long count = userRepository.count();
		List<User> users = (List<User>) userRepository.findAll();
		System.out.print("Count is being called" + users.get(0));

		return count;
	}

}
