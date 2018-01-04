package com.poc.trainingmanager.model.wrapper;

import java.util.Set;

import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;

public class LoggedInUserWrapper {

	private Set<DepartmentUdt> departments;

	public Set<DepartmentUdt> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<DepartmentUdt> departments) {
		this.departments = departments;
	}

}
