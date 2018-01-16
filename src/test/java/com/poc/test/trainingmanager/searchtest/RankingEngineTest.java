package com.poc.test.trainingmanager.searchtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.search.RankingEngine;

public class RankingEngineTest {

	@Test
	public void testGetRankForStrings() {
		int rank = RankingEngine.getRank("lol", "lol");
		assertThat(rank).isEqualTo(2);

		int rank2 = RankingEngine.getRank("kaustubh", "aus");
		assertThat(rank2).isEqualTo(1);
	}

	@Test
	public void testGetDepartmentRank() {
		DepartmentUdt angularDepartment = mock(DepartmentUdt.class);
		DepartmentUdt javaDepartment = mock(DepartmentUdt.class);
		DepartmentUdt dotNetDepartment = mock(DepartmentUdt.class);

		List<DepartmentUdt> toBeRanked = new ArrayList<DepartmentUdt>();

		toBeRanked.add(angularDepartment);
		toBeRanked.add(javaDepartment);
		toBeRanked.add(dotNetDepartment);

		List<DepartmentUdt> criteria = new ArrayList<DepartmentUdt>();
		criteria.add(dotNetDepartment);
		int rank1 = RankingEngine.getDepartmentRank(toBeRanked, criteria);
		assertThat(rank1).isEqualTo(1);

		criteria.add(javaDepartment);
		assertThat(RankingEngine.getDepartmentRank(toBeRanked, criteria)).isEqualTo(2);
	}

	@Test
	public void testRankingEngine() {

		DepartmentUdt angularDepartment = mock(DepartmentUdt.class);
		DepartmentUdt javaDepartment = mock(DepartmentUdt.class);
		DepartmentUdt dotNetDepartment = mock(DepartmentUdt.class);

		List<DepartmentUdt> toBeRanked = new ArrayList<DepartmentUdt>();

		toBeRanked.add(angularDepartment);
		toBeRanked.add(javaDepartment);
		toBeRanked.add(dotNetDepartment);

		List<DepartmentUdt> criteria = new ArrayList<DepartmentUdt>();
		criteria.add(dotNetDepartment);

		List<RoleUdt> roles = new ArrayList<RoleUdt>();

		RoleUdt admin = mock(RoleUdt.class);
		RoleUdt student = mock(RoleUdt.class);
		RoleUdt departmentAdmin = mock(RoleUdt.class);

		roles.add(admin);
		roles.add(student);
		roles.add(departmentAdmin);

		List<User> resultList = new ArrayList<User>();

		User rahul = new User();
		User ajay = new User();
		User kshitij = new User();
		User nitin = new User();

		rahul.setFirstName("rahul");
		rahul.setLastName("negi");
		rahul.setRoles(new HashSet<RoleUdt>(Arrays.asList(admin)));
		rahul.setDepartments(new HashSet<DepartmentUdt>(Arrays.asList(angularDepartment)));
		rahul.setEmailId("rahul@rahul.com");
		rahul.setIsActive(true);

		ajay.setFirstName("ajay");
		ajay.setLastName("v");
		ajay.setIsActive(true);
		ajay.setRoles(new HashSet<RoleUdt>(Arrays.asList(student)));
		ajay.setDepartments(new HashSet<DepartmentUdt>(Arrays.asList(dotNetDepartment)));
		ajay.setEmailId("ajay@ajay.com");

		nitin.setFirstName("nitin");
		nitin.setLastName("srinivas");
		nitin.setIsActive(true);
		nitin.setRoles(new HashSet<RoleUdt>(Arrays.asList(departmentAdmin)));
		nitin.setDepartments(new HashSet<DepartmentUdt>(Arrays.asList(javaDepartment)));
		nitin.setEmailId("nitin@ajay.com");

		kshitij.setFirstName("kshitij");
		kshitij.setLastName("singh");
		kshitij.setIsActive(true);
		kshitij.setRoles(new HashSet<RoleUdt>(Arrays.asList(student)));
		kshitij.setDepartments(new HashSet<DepartmentUdt>(Arrays.asList(dotNetDepartment)));
		kshitij.setEmailId("nitin@ajay.com");

		System.out.println(rahul.getIsActive());

		resultList.add(nitin);
		resultList.add(kshitij);
		resultList.add(rahul);
		resultList.add(ajay);

		List<User> expectedRankList = new ArrayList<User>();
		expectedRankList.add(nitin);
		expectedRankList.add(kshitij);
		expectedRankList.add(rahul);
		expectedRankList.add(ajay);

		List<User> actualRankList = RankingEngine.rankResults(resultList, "nitin", "ks", "yad", toBeRanked, roles);
		assertThat(expectedRankList).isEqualTo(actualRankList);

	}

}
