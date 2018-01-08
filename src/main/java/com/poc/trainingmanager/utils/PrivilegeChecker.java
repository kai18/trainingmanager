package com.poc.trainingmanager.utils;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.poc.trainingmanager.exceptions.AccessDeniedException;
import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;

public class PrivilegeChecker {

	@Autowired
	DepartmentRolesRepository departmentRolesRespositoy;

	public boolean isSuperAdmin(Set<RoleUdt> loggedInUserRoles) {
		for (RoleUdt roleUdt : loggedInUserRoles) {
			if (roleUdt.getRoleType() == "System")
				return true;
		}

		return false;
	}

	public Set<DepartmentUdt> getCommonDepartments(Set<DepartmentUdt> loggedInUserDepartments,
			Set<DepartmentUdt> userToBeDeletedDepartments) {
		Set<DepartmentUdt> commonDepartments = new HashSet<DepartmentUdt>();
		for (DepartmentUdt department : userToBeDeletedDepartments) {
			if (loggedInUserDepartments.contains(department))
				commonDepartments.add(department);
		}
		return commonDepartments;
	}

	public PrivilegeUdt checkIfUserIsDepartmentAdmin(Set<DepartmentUdt> commonDepartments) {
		for (DepartmentUdt department : commonDepartments) {
			DepartmentRoles departmentRoles = departmentRolesRespositoy
					.findByDepartmentId(department.getDepartmentId());
			for (RoleUdt role : departmentRoles.getRoles()) {
				if (role.getRoleType() == "Department")
					return role.getPrivilege();
			}
		}

		return null;
	}

	public boolean checkIAllowedToDelete(Set<DepartmentUdt> loggedInUserDepartments,
			Set<DepartmentUdt> userToBeDeletedDepartments) {
		// get common departments
		Set<DepartmentUdt> commonDepartments = this.getCommonDepartments(loggedInUserDepartments,
				userToBeDeletedDepartments);
		if (commonDepartments != null && !commonDepartments.isEmpty()) {
			// check if the logged in user has admin role in at least one of the
			// common departments
			PrivilegeUdt privileges = this.checkIfUserIsDepartmentAdmin(commonDepartments);
			if (privileges != null) {
				if (privileges.getDeletionPrivilege() == 1)// check if the role has the privilege to delete
					return true;
				else
					throw new AccessDeniedException("User doesn't have permission to delete");
			}
		}
		return false;
	}

	public boolean checkIAllowedToUpdate(Set<DepartmentUdt> loggedInUserDepartments,
			Set<DepartmentUdt> userToBeDeletedDepartments) {
		// get common departments
		Set<DepartmentUdt> commonDepartments = this.getCommonDepartments(loggedInUserDepartments,
				userToBeDeletedDepartments);
		if (commonDepartments != null && !commonDepartments.isEmpty()) {
			// check if the logged in user has admin role in at least one of the
			// commmon departments
			PrivilegeUdt privileges = this.checkIfUserIsDepartmentAdmin(commonDepartments);
			if (privileges != null) {
				if (privileges.getUpdationPrivilege() == 1)// check if the role has the privilege to update
					return true;
				else
					throw new AccessDeniedException("User doesn't have permission to update");
			}
		}
		return false;
	}

	public boolean checkIAllowedToInsert(Set<DepartmentUdt> loggedInUserDepartments,
			Set<DepartmentUdt> userToBeDeletedDepartments) {
		// get common departments
		Set<DepartmentUdt> commonDepartments = this.getCommonDepartments(loggedInUserDepartments,
				userToBeDeletedDepartments);
		if (commonDepartments != null && !commonDepartments.isEmpty()) {
			// check if the logged in user has admin role in at least one of the
			// common departments
			PrivilegeUdt privileges = this.checkIfUserIsDepartmentAdmin(commonDepartments);
			if (privileges != null) {
				if (privileges.getCreationPrivilege() == 1)// check if the role has the privilege to insert
					return true;
				else
					throw new AccessDeniedException("User doesn't have permission to Insert");
			}
		}
		return false;

	}
}
