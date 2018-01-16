package com.poc.test.trainingmanager.servicetest.userservicetest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.impl.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = { UserServiceImpl.class })
@ComponentScan
public class UserServiceTest {

	@MockBean
	UserRepository userRepository;

	@Autowired
	@InjectMocks
	UserServiceImpl userService;

	@Before
	public void setUp() {
		System.out.println("Setting up");
	}

	@Test
	public void demoTest() {
	}

}
