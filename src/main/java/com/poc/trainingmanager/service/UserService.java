package com.poc.trainingmanager.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.StandardResponse;

@Service
public interface UserService {

	public StandardResponse search(Map<String, String> searchParameters);

}
