package com.poc.trainingmanager.utils;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.poc.trainingmanager.exceptions.AccessDeniedException;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;

public class PrivilegeChecker {

	@Autowired
	DepartmentRolesRepository departmentRolesRespositoy;

	public boolean userBelongsToSameDepartment(Set<DepartmentUdt> loggedInUserDepartments,
			Set<DepartmentUdt> userToBeDeletedDepartments) {
		for (DepartmentUdt department : userToBeDeletedDepartments) {
			if (loggedInUserDepartments.contains(department)) {
				return true;
			}
		}
		return false;
	}

	public PrivilegeUdt getUserDepartmentPrevilege(Set<DepartmentUdt> loggedInUserDepartments, Set<RoleUdt> roles) {
		for (DepartmentUdt department : loggedInUserDepartments) {
			Set<RoleUdt> departmentRoles = departmentRolesRespositoy.findByDepartmentId(department.getDepartmentId())
					.getRoles();
			for (RoleUdt role : roles) {
				if (departmentRoles.contains(role) && role.getRoleType() == "Department") {
					PrivilegeUdt privilege = role.getPrivilege();
					return privilege;
				}
			}
		}

		return null;
	}

	public boolean isSuperAdmin(Set<RoleUdt> roles) {
		for (RoleUdt roleUdt : roles) {
			if (roleUdt.getRoleType() == "System")
				return true;
		}

		return false;
	}

	public boolean checkForDeletePrevilege(Set<DepartmentUdt> loggedInUserDepartments, Set<RoleUdt> roles,
			Set<DepartmentUdt> userToBeDeletedDepartments) {

		if (this.isSuperAdmin(roles))
			return true;

		else if (userBelongsToSameDepartment(loggedInUserDepartments, userToBeDeletedDepartments)) {
			PrivilegeUdt privilegeUdt = this.getUserDepartmentPrevilege(loggedInUserDepartments, roles);
			if (privilegeUdt != null) {
				if (privilegeUdt.getDeletionPrivilege() == 1) {
					return true;
				}
			}
		}
		throw new AccessDeniedException("You don't have permission to delete");
	}

	public boolean checkForUdatePrevilege(Set<DepartmentUdt> loggedInUserDepartments, Set<RoleUdt> roles,
			Set<DepartmentUdt> userToBeDeletedDepartments) {

		if (this.isSuperAdmin(roles))
			return true;

		else if (userBelongsToSameDepartment(loggedInUserDepartments, userToBeDeletedDepartments)) {
			PrivilegeUdt privilegeUdt = this.getUserDepartmentPrevilege(loggedInUserDepartments, roles);
			if (privilegeUdt != null) {
				if (privilegeUdt.getUpdationPrivilege() == 1) {
					return true;
				}
			}
		}

		throw new AccessDeniedException("You don't have permission to update");
	}

	public boolean checkForCreatePrevilege(Set<DepartmentUdt> loggedInUserDepartments, Set<RoleUdt> roles,
			Set<DepartmentUdt> userToBeDeletedDepartments) {

		if (this.isSuperAdmin(roles))
			return true;

		else if (userBelongsToSameDepartment(loggedInUserDepartments, userToBeDeletedDepartments)) {
			PrivilegeUdt privilegeUdt = this.getUserDepartmentPrevilege(loggedInUserDepartments, roles);
			if (privilegeUdt != null) {
				if (privilegeUdt.getCreationPrivilege() == 1) {
					return true;
				}
			}
		}
		throw new AccessDeniedException("You don't have permission to create");
	}
}
