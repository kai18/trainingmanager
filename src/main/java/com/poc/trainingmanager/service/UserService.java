package com.poc.trainingmanager.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;

@Service
public interface UserService {

	public StandardResponse<User> insert(User user);
	
	public StandardResponse<User> update(User user); 
	
	public StandardResponse search(Map<String, String> searchParameters);

	public StandardResponse<User> grantrole(String userId, String roleId);
	
	public StandardResponse<User> revokerole(String userId, String roleId);
}
