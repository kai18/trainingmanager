package com.poc.trainingmanager.utils;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.poc.trainingmanager.exceptions.AccessDeniedException;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;

@Component
public class PrivilegeChecker {

	@Autowired
	DepartmentRolesRepository departmentRolesRespositoy;

	@Autowired
	RoleUsersRepository roleUsersRepository;

	public boolean isSuperAdmin(Set<PrivilegeUdt> privileges) {
		for (PrivilegeUdt privilege : privileges) {
			if (privilege.getDepartment_id() == null && privilege.getUpdationPrivilege() == 1)
				return true;
		}
		return false;
	}

	public boolean isAllowedToDeleteUser(Set<DepartmentUdt> userToBeDeletedDepartments, Set<PrivilegeUdt> privileges) {

		if (this.isSuperAdmin(privileges))
			return true;

		for (DepartmentUdt department : userToBeDeletedDepartments) {
			for (PrivilegeUdt privilege : privileges) {
				if (privilege.getDepartment_id().equals(department.getDepartmentId())) {
					if (privilege.getDeletionPrivilege() == 1) {
						return true;
					}
				}
			}
		}

		throw new AccessDeniedException("You dont have sufficient privileges to delete this user");
	}

	public boolean isAllowedToEditUser(User userToBeDeleted, LoggedInUserWrapper loggedInUser) {

		if (userToBeDeleted.getId().equals(loggedInUser.getUserId()))
			return true;

		if (this.isSuperAdmin(loggedInUser.getPrivileges()))
			return true;

		Set<DepartmentUdt> userToBeDeletedDepartments = userToBeDeleted.getDepartments();
		Set<PrivilegeUdt> loggedInUserPrivilege = loggedInUser.getPrivileges();

		for (DepartmentUdt department : userToBeDeletedDepartments) {
			for (PrivilegeUdt privilege : loggedInUserPrivilege) {
				if (privilege.getDepartment_id().equals(department.getDepartmentId())) {
					if (privilege.getUpdationPrivilege() == 1) {
						return true;
					}
				}
			}
		}

		throw new AccessDeniedException("You dont have sufficient privileges to delete this user");
	}

	public boolean isAllowedToCreateRole(Set<PrivilegeUdt> loggedInUserPrivileges) {

		if (this.isSuperAdmin(loggedInUserPrivileges))
			return true;
		throw new AccessDeniedException("You are not an admin");
	}

	public boolean isAllowedToDeleteRole(Set<PrivilegeUdt> loggedInUserPrivileges) {

		if (this.isSuperAdmin(loggedInUserPrivileges))
			return true;
		throw new AccessDeniedException("You are not an admin");
	}

	public boolean isAllowedToEditRole(Set<PrivilegeUdt> loggedInUserPrivileges) {

		if (this.isSuperAdmin(loggedInUserPrivileges))
			return true;
		throw new AccessDeniedException("You are not an admin");
	}

	public boolean isAllowedToCreateDepartment(Set<PrivilegeUdt> loggedInUserprivileges) {

		if (this.isSuperAdmin(loggedInUserprivileges))
			return true;
		throw new AccessDeniedException("You dont have sufficient privileges to create a department");
	}

	public boolean isAllowedToEditDepartment(Set<PrivilegeUdt> loggedInUserPrivileges, UUID departmentId) {

		if (this.isSuperAdmin(loggedInUserPrivileges))
			return true;

		for (PrivilegeUdt privilege : loggedInUserPrivileges) {
			if (privilege.getDepartment_id() == null && privilege.getUpdationPrivilege() == 9)
				return true;
			if (privilege.getDepartment_id().equals(departmentId) && privilege.getUpdationPrivilege() == 9) {
				return true;
			}
		}

		throw new AccessDeniedException("Youd dont have sufficient privileges to edit this department");
	}

	public boolean IsAllowedToDeleteDepartment(Set<PrivilegeUdt> loggedInUserPrivileges) {

		if (this.isSuperAdmin(loggedInUserPrivileges))
			return true;
		throw new AccessDeniedException("You dont have sufficient privileges to delete this department");
	}

}
