package com.poc.trainingmanager.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.repository.UserRepository;

@Service
public class SearchEngine {

	@Autowired
	UserRepository userRepository;

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

		if (roles != null && roles.isEmpty()) {
			List<User> userList = this.searchByRoles(roles);
			this.addToResult(userList, resultList);
		}

		return this.rankResults(resultList, email, firstName, lastName, departments, roles);
	}

	/*
	 * private int getRank(List<String> results, List<String> toBeSearched) { int
	 * rank = 0; for (String result : results) { for (String term : toBeSearched) {
	 * if (result.equalsIgnoreCase(term)) rank += 2; else if
	 * (result.toLowerCase().matches("(.*)" + term.toLowerCase() + "(.*)")) rank +=
	 * 1; } } return rank; }
	 */

	private List<User> rankResults(List<User> resultList, String email, String firstName, String lastName,
			List<DepartmentUdt> departments, List<RoleUdt> roles) {

		Multimap<Integer, User> resultMap = MultimapBuilder.treeKeys().linkedListValues().build();

		for (User user : resultList) {
			int rank = 0;
			if (email != null)
				rank += this.getRank(user.getEmailId(), email);
			if (firstName != null)
				rank += this.getRank(user.getFirstName(), firstName);
			if (lastName != null)
				rank += this.getRank(user.getLastName(), lastName);

			/*
			 * if (departments != null && !departments.isEmpty()) rank += this.getRank(new
			 * ArrayList(user.getDepartments()), departments); if (roles != null &&
			 * !roles.isEmpty()) rank += this.getRank(new ArrayList(user.getRoles()),
			 * roles);
			 */

			LOGGER.error("Rank for user: " + user.getFirstName() + " is " + rank);

			resultMap.put(rank, user);
		}

		List<User> sortedResultList = new ArrayList<User>();

		System.out.println(resultMap);

		for (User finalUser : resultMap.values()) {
			sortedResultList.add(finalUser);
		}

		Collections.reverse(sortedResultList);
		System.out.println(sortedResultList);
		return sortedResultList;

	}

	public User searchByEmail(String email) {
		if (email != null) {
			User user = userRepository.findByEmailId(email);
			if (user != null)
				return user;
			else {
				LOGGER.error("Null email value passed");
			}
		}
		return null;
	}

	public List<User> searchByFirstNameAndLastName(String firstName, String lastName) {
		return null;
	}

	public List<User> searchByFirstName(String firstName) {
		if (firstName != null) {
			List<User> userList = userRepository.findByFirstNameContainingIgnoreCase(firstName);
			return userList;
		}

		else {
			LOGGER.error("Null firstName passed");
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
		if (departments != null && !departments.isEmpty()) {
			for (DepartmentUdt department : departments)
				userList.addAll(userRepository.findByDepartments(department));
		}
		LOGGER.error("Department Result" + userList);

		return userList;
	}

	public List<User> searchByRoles(List<RoleUdt> roles) {

		List<User> userList = new ArrayList<User>();
		if (roles != null && !roles.isEmpty()) {
			for (RoleUdt role : roles)
				userList.addAll(userRepository.findByRoles(role));
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

	private int getRank(String result, String toBeSearched) {
		int rank = 0;

		LOGGER.error("result is " + result + " tobesearched is " + toBeSearched);
		LOGGER.error("Check " + result.toLowerCase().matches("(.*)" + "ankaja" + "(.*)"));

		if (result.equalsIgnoreCase(toBeSearched))
			rank += 2;

		else if (result.toLowerCase().matches("(.*)" + toBeSearched + "(.*)")) {
			LOGGER.error("Here");
			rank += 1;
		}

		LOGGER.error("Rank is " + rank);

		return rank;
	}

	private void addToResult(List<User> userList, List<User> resultList) {
		if (userList != null && !userList.isEmpty())
			resultList.addAll(userList);

	}

}
