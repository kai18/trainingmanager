package com.poc.trainingmanager.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.poc.trainingmanager.repository")
@ComponentScan(basePackages = { "com.poc.trainingmanager", "com.poc.trainingmanager.config",
		"com.poc.trainingmanager.repository", "com.poc.trainingmanager.model" })
public class TrainingmanagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TrainingmanagerApplication.class, args);
	}
}