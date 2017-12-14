package com.poc.trainingmanager.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;

@Service
public interface UserService {

	public StandardResponse insert(User user);
	
	public StandardResponse search(Map<String, String> searchParameters);

}
