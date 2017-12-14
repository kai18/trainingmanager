package com.poc.trainingmanager.service.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.constants.Constants;
import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.AddressUdt;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.UserService;
import com.poc.trainingmanager.utils.FieldValidator;
import com.poc.trainingmanager.utils.PasswordUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public StandardResponse search(Map<String, String> searchParameters) {

		String firstName = searchParameters.get("firstName");
		String lastName = searchParameters.get("lastName");
		String email = searchParameters.get("email");
		String departments = searchParameters.get("departments");
		String roles = searchParameters.get("role");

		List<User> unwrappedResults = new ArrayList<User>();

		if (email != null && FieldValidator.validateEmail(email)) {
			User user = userRepository.findByEmailId(email);
			if (user != null) {
				unwrappedResults.add(user);
			}
		}

		else if (!FieldValidator.isNullOrIsEmpty(firstName, lastName, email, departments, roles)) {

		}
		return null;
	}

	@Override
	public StandardResponse insert(User user,AddressUdt address, RoleUdt role, DepartmentUdt department) {
		StandardResponse stdResponse = new StandardResponse();

		//FieldValidator.validateForUserInsert(user);
		user.setAddress(address);
		user.setPassword(PasswordUtil.getPasswordHash(user.getPassword()));
		userRepository.insert(user);
		stdResponse.setStatus(Constants.SUCCESS);
		return stdResponse;
	}

}
