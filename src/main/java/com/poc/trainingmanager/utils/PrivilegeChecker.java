package com.poc.trainingmanager.utils;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.poc.trainingmanager.exceptions.AccessDeniedException;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.RoleUsersRepository;

@Component
public class PrivilegeChecker {

	@Autowired
	DepartmentRolesRepository departmentRolesRespositoy;

	@Autowired
	RoleUsersRepository roleUsersRepository;

	public boolean IsAllowedToDeleteUser(Set<DepartmentUdt> userToBeDeletedDepartments, List<PrivilegeUdt> privileges) {

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

	public boolean IsAllowedToEditUser(Set<DepartmentUdt> userToBeDeletedDepartments,
			Set<PrivilegeUdt> loggedInUserPrivilege) {

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

	public boolean IsAllowedToCreateRole(Set<PrivilegeUdt> loggedInUserPrivilege) {

		for (PrivilegeUdt privilege : loggedInUserPrivilege) {
			if (privilege.getDepartment_id() == null && privilege.getCreationPrivilege() == 1)
				return true;
		}
		throw new AccessDeniedException("You dont have sufficient privileges to delete this user");
	}

	public boolean IsAllowedToDeleteRole(Set<PrivilegeUdt> loggedInUserPrivilege) {

		for (PrivilegeUdt privilege : loggedInUserPrivilege) {
			if (privilege.getDepartment_id() == null && privilege.getDeletionPrivilege() == 1)
				return true;
		}
		throw new AccessDeniedException("You dont have sufficient privileges to delete this user");
	}

	public boolean IsAllowedToEditRole(Set<PrivilegeUdt> loggedInUserPrivilege) {

		for (PrivilegeUdt privilege : loggedInUserPrivilege) {
			if (privilege.getDepartment_id() == null && privilege.getUpdationPrivilege() == 1)
				return true;
		}
		throw new AccessDeniedException("You dont have sufficient privileges to delete this user");
	}

	public boolean IsAllowedToCreateDepartment(Set<PrivilegeUdt> loggedInUserPrivilege) {

		for (PrivilegeUdt privilege : loggedInUserPrivilege) {
			if (privilege.getDepartment_id() == null && privilege.getCreationPrivilege() == 1)
				return true;
		}
		throw new AccessDeniedException("You dont have sufficient privileges to delete this user");
	}

	public boolean isAllowedToEditDepartment(Set<PrivilegeUdt> loggedInUserPrivilege, UUID departmentId) {

		for (PrivilegeUdt privilege : loggedInUserPrivilege) {
			if (privilege.getDepartment_id().equals(departmentId) && privilege.getUpdationPrivilege() == 1) {
				return true;
			}
		}

		throw new AccessDeniedException("Youd dont have sufficient privileges to edit this department");
	}

	public boolean IsAllowedToDeleteDepartment(Set<PrivilegeUdt> loggedInUserPrivilege) {

		for (PrivilegeUdt privilege : loggedInUserPrivilege) {
			if (privilege.getDepartment_id() == null && privilege.getDeletionPrivilege() == 1)
				return true;
		}
		throw new AccessDeniedException("You dont have sufficient privileges to delete this user");
	}

}
