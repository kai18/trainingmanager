package com.poc.trainingmanager.model.wrapper;

import java.util.Set;
import java.util.UUID;

import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;

public class LoggedInUserWrapper {

	UUID userId;
	private Set<DepartmentUdt> departments;
	private Set<PrivilegeUdt> privileges;

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public Set<DepartmentUdt> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<DepartmentUdt> departments) {
		this.departments = departments;
	}

	public Set<PrivilegeUdt> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<PrivilegeUdt> privileges) {
		this.privileges = privileges;
	}

}
