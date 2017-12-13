package com.poc.trainingmanager.model.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.poc.trainingmanager.model.User;

public class WrapperUtil {

	public static UserSearchWrapper wrapUserToUserSearchWrapper(User user) {

		if (user != null) {
			UserSearchWrapper userSearchWrapper = new UserSearchWrapper();
			BeanUtils.copyProperties(user, userSearchWrapper);
			return userSearchWrapper;
		}

		return null;
	}

	public static List<UserSearchWrapper> wrapUserToUserSearchWrapper(List<User> users) {

		List<UserSearchWrapper> userList = new ArrayList<UserSearchWrapper>();
		for (User user : users) {
			if (user != null) {
				UserSearchWrapper userSearchWrapper = new UserSearchWrapper();
				BeanUtils.copyProperties(user, userSearchWrapper);
				userList.add(userSearchWrapper);
			}
		}

		return userList;
	}
}
