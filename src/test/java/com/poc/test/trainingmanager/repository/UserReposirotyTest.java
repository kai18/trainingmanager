package com.poc.trainingmanager.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.poc.trainingmanager.controller.UserController;
import com.poc.trainingmanager.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { UserRepository.class })
@EnableAutoConfiguration
@ComponentScan
@ContextConfiguration
public class UserReposirotyTest {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;

	@Test
	public void getById() {
		String id = "e8df3d48-24aa-4f25-9fc7-d55ce224bb17";
		UUID userId = UUID.fromString(id);
		User user = userRepository.findById(userId);

		assertThat(user).isNotNull();
		assertThat(user).hasFieldOrProperty("password");

	}
}
