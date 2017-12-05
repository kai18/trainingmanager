package com.poc.trainingmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.repository.UserRepository;

@Service
public class TestAbc {

	@Autowired
	UserRepository userRepository;

	public Long test() {
		Long count = userRepository.count();
		System.out.print("Count is being called" + count);
		return count;
	}

}
