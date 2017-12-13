package com.poc.trainingmanager.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.AddressUdt;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;

@Service
public interface UserService {

	public StandardResponse insert(User user,AddressUdt address, RoleUdt role, DepartmentUdt department);
	
	public StandardResponse search(Map<String, String> searchParameters);

}
