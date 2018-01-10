package com.poc.trainingmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;

@Service
public interface UserService {

	public StandardResponse<User> insert(User user);

	public StandardResponse<User> update(User user, LoggedInUserWrapper loggedInUser);

	public StandardResponse<List<UserSearchWrapper>> search(String firstName, String lastName, String email,
			String departments, String roles);

	public StandardResponse<User> grantRole(String userId, String roleId, LoggedInUserWrapper loggedInUser);

	public StandardResponse<User> revokeRole(String userId, String roleId, LoggedInUserWrapper loggedInUser);

	StandardResponse<UserSearchWrapper> getUserById(String id);

	public StandardResponse deleteUser(String userId, LoggedInUserWrapper loggedInUser);

	public StandardResponse getRolesByDepartment(String userId);
}
