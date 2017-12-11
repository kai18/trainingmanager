package com.poc.trainingmanager.service;

import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.StandardResponse;

@Service
public interface UserService {

	public StandardResponse search();

}
