package com.poc.trainingmanager.model.wrapper;

import java.util.List;
import java.util.Set;

import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;

public class LoggedInUserWrapper {

	private Set<DepartmentUdt> departments;
	private List<PrivilegeUdt> privileges;

	public Set<DepartmentUdt> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<DepartmentUdt> departments) {
		this.departments = departments;
	}

	public List<PrivilegeUdt> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<PrivilegeUdt> privileges) {
		this.privileges = privileges;
	}

}
