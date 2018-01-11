package com.poc.trainingmanager.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentUsersRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;

/**
 * @author Kaustubh.Kaustubh
 *         <p>
 *         This class searches for users given any criteria from firstName,
 *         lastName, email, department and role. It fetches the users from the
 *         database by executing appropriate select queries. At the core lies
 *         the ranking algorithm, that checks how many criteria a particular
 *         result satisfies and to what degree and then ranks the results
 *         accordingly
 *         </p>
 */
@Service
public class SearchEngine {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleUsersRepository roleUsersRepository;

	@Autowired
	DepartmentUsersRepository departmentUsersRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchEngine.class);

	public List<User> searchByAllParameters(String email, String firstName, String lastName,
			List<DepartmentUdt> departments, List<RoleUdt> roles) {

		List<User> resultList = new ArrayList<User>();
		if (email != null) {
			List<User> userList = this.searchByPartialEmail(email);
			this.addToResult(userList, resultList);
		}

		if (firstName != null) {
			List<User> userList = this.searchByFirstName(firstName);
			this.addToResult(userList, resultList);
		}

		if (lastName != null) {
			List<User> userList = this.searchByLastName(lastName);
			this.addToResult(userList, resultList);
		}

		if (departments != null && !departments.isEmpty()) {
			List<User> userList = this.searchByDepartments(departments);
			this.addToResult(userList, resultList);
		}

		if (roles != null && !roles.isEmpty()) {
			List<User> userList = this.searchByRoles(roles);
			this.addToResult(userList, resultList);
		}

		return RankingEngine.rankResults(resultList, email, firstName, lastName, departments, roles);
	}

	public User searchByEmail(String email) {
		if (email != null) {
			User user = userRepository.findByEmailId(email);
			if (user != null)
				return user;
			else {
				LOGGER.warn("Null email value passed");
			}
		}
		return null;
	}

	public List<User> searchByFirstName(String firstName) {
		if (firstName != null) {
			List<User> userList = userRepository.findByFirstNameContainingIgnoreCase(firstName);
			return userList;
		}

		else {
			LOGGER.warn("Null firstName passed");
			return null;
		}
	}

	public List<User> searchByLastName(String lastName) {
		if (lastName != null) {
			List<User> userList = userRepository.findByLastNameContainingIgnoreCase(lastName);
			if (userList != null && !userList.isEmpty())
				return userList;
			else {
				LOGGER.error("Null lastName passed");
			}
		}
		return null;
	}

	public List<User> searchByDepartments(List<DepartmentUdt> departments) {
		List<User> userList = new ArrayList<User>();
		List<UserUdt> userUdtList = null;

		if (departments != null && !departments.isEmpty()) {
			for (DepartmentUdt department : departments) {
				Set<UserUdt> userUdts = departmentUsersRepository.findByDepartmentId(department.getDepartmentId())
						.getUserDepartmentsUdt();
				if (userUdts != null) {
					userUdtList = new ArrayList<UserUdt>(userUdts);
					userList.addAll(WrapperUtil.userUdtToUser(userUdtList));
				}
			}
		}

		return userList;
	}

	public List<User> searchByRoles(List<RoleUdt> roles) {

		List<User> userList = new ArrayList<User>();
		List<UserUdt> userUdtList = null;
		if (roles != null && !roles.isEmpty()) {
			for (RoleUdt role : roles) {
				Set<UserUdt> userUdts = roleUsersRepository.findByRoleId(role.getRoleId()).getUserUdt();
				if (userUdts != null) {
					userUdtList = new ArrayList<UserUdt>(userUdts);
					userList.addAll(WrapperUtil.userUdtToUser(userUdtList));
				}
			}
		}
		LOGGER.error("Role Result" + userList);
		return userList;
	}

	public List<User> searchByPartialEmail(String email) {
		if (email != null) {
			List<User> userList = userRepository.findByEmailIdContainingIgnoreCase(email);
			if (userList != null && !userList.isEmpty())
				return userList;
			else {
				LOGGER.error("Null email value passed");
			}
		}
		return null;
	}

	private void addToResult(List<User> userList, List<User> resultList) {
		if (userList != null && !userList.isEmpty())
			resultList.addAll(userList);

	}

}
