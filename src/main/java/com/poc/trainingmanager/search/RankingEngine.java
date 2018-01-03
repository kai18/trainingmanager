package com.poc.trainingmanager.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;

public class RankingEngine {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchEngine.class);

	private static int getRank(String result, String toBeSearched) {
		int rank = 0;

		LOGGER.error("result is " + result + " tobesearched is " + toBeSearched);

		if (result.equalsIgnoreCase(toBeSearched))
			rank += 2;

		else if (result.toLowerCase().matches("(.*)" + toBeSearched + "(.*)")) {
			LOGGER.error("Here");
			rank += 1;
		}

		LOGGER.error("Rank is " + rank);

		return rank;
	}

	private static int getDepartmentRank(List<DepartmentUdt> toBeRanked, List<DepartmentUdt> criteria) {
		int rank = 0;
		if (toBeRanked != null && criteria != null) {
			for (DepartmentUdt departmentUdt : toBeRanked) {
				for (DepartmentUdt criteriaDepartmentUdt : criteria) {
					if (departmentUdt.equals(criteriaDepartmentUdt))
						rank++;
				}
			}
		}
		return rank;
	}

	private static int getRoleRank(List<RoleUdt> toBeRanked, List<RoleUdt> criteria) {
		int rank = 0;
		if (toBeRanked != null && criteria != null) {
			for (RoleUdt departmentUdt : toBeRanked) {
				for (RoleUdt criteriaDepartmentUdt : criteria) {
					if (departmentUdt.equals(criteriaDepartmentUdt))
						rank++;
				}
			}
		}
		return rank;
	}

	static List<User> rankResults(List<User> resultList, String email, String firstName, String lastName,
			List<DepartmentUdt> departments, List<RoleUdt> roles) {

		Multimap<Integer, User> resultMap = MultimapBuilder.treeKeys().linkedListValues().build();

		for (User user : resultList) {
			int rank = 0;
			if (email != null)
				rank += RankingEngine.getRank(user.getEmailId(), email);
			if (firstName != null)
				rank += RankingEngine.getRank(user.getFirstName(), firstName);
			if (lastName != null)
				rank += RankingEngine.getRank(user.getLastName(), lastName);

			if (departments != null && !departments.isEmpty())
				rank += RankingEngine.getDepartmentRank(new ArrayList<DepartmentUdt>(user.getDepartments()),
						departments);
			if (roles != null && !roles.isEmpty())
				rank += RankingEngine.getRoleRank(new ArrayList<RoleUdt>(user.getRoles()), roles);

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

}
