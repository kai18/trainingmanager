package com.poc.trainingmanager.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.poc.trainingmanager.repository")
@ComponentScan(basePackages = { "com.poc.trainingmanager", "com.poc.trainingmanager.config",
		"com.poc.trainingmanager.repository", "com.poc.trainingmanager.model", "com.poc.trainingmanager.exceptions" })
public class TrainingmanagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TrainingmanagerApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedOrigins("*")
						.allowedHeaders("*");
			}
		};
	}
}