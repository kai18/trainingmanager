package com.poc.test.trainingmanager.servicetest.userservicetest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.poc.test.trainingmanager.testdata.SearchData;
import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.wrapper.UserSearchWrapper;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.RoleRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.search.SearchEngine;
import com.poc.trainingmanager.service.impl.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = { UserServiceImpl.class })
@ComponentScan
public class UserServiceTest {

	@Mock
	UserRepository userRepository;

	@Mock
	RoleRepository roleRepository;

	@Mock
	DepartmentRepository departmentRepository;

	@Mock
	SearchEngine searchEngine;

	@InjectMocks
	UserServiceImpl userService;

	@Before
	public void setUp() {
		System.out.println("Setting up");
		MockitoAnnotations.initMocks(this);
		SearchData.setUp();

	}

	@Test
	public void userSearchTest() {
		String departmentName = "departmentName";
		String roleName = "roleName";

		String email = "j";
		String firstName = "kshitij";
		String lastName = "singh";

		List<User> expected = new ArrayList<User>(new LinkedHashSet<User>(SearchData.expectedResult));

		StandardResponse<List<UserSearchWrapper>> expectedResponse = new StandardResponse<List<UserSearchWrapper>>();
		expectedResponse.setElement(WrapperUtil.wrapUserToUserSearchWrapper(expected));

		Role role = mock(Role.class);
		Department department = mock(Department.class);
		when(departmentRepository.findByDepartmentName(Mockito.anyString())).thenReturn(department);
		when(roleRepository.findByRoleName(Mockito.anyString())).thenReturn(role);

		when(searchEngine.searchByAllParameters(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyListOf(DepartmentUdt.class), Mockito.anyListOf(RoleUdt.class))).thenReturn(expected);

		assertThat(userService.search(firstName, lastName, email, departmentName, roleName).getElement())
				.isEqualTo(expectedResponse.getElement());

	}

}
