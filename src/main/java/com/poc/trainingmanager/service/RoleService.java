package com.poc.trainingmanager.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;

/**
 * <h1>Role Service</h1>
 * <p>
 * It contains all the methods that can be used for 
 * CRUD operation of Role on Cassandra DB Role table.
 * A Role denotes what an User supposed to perform.
 * Privilege in a Role will specify the access to perform any operation. 
 * </p>
 */
/**
 * @author Nitin.K
 *
 */
@Service
public interface RoleService {

	/**
	 * This method is used to fetch all the existing Roles from the Role table.
	 * 
	 * @param assignedPrivileges
	 *            is a list of privileges associated to the User who invokes this
	 *            operation.
	 */
	public StandardResponse<List<Role>> getAllRoles();

	/**
	 * This method is used to fetch a specific existing Role with the given Role
	 * Name from the Role table.
	 * 
	 * @param assignedPrivileges
	 *            a list of privileges associated to the User who invokes this
	 *            operation.
	 * @param roleName
	 *            the unique name given for a Role
	 */
	public StandardResponse<Role> getRoleByName(String roleName);

	// This method is used to insert a new Role into the Role table.
	public StandardResponse<Role> addRole(Role role, LoggedInUserWrapper loggedInUser);

	// This method is used to update an existing Role in the Role table.
	public StandardResponse<Role> updateRole(Role role, LoggedInUserWrapper loggedInUser);

	// This method is used to delete an existing Role in the Role table.
	public StandardResponse<Role> deleteRole(String roleId, LoggedInUserWrapper loggedInUser);

	public StandardResponse<List<RoleUsers>> getAllRoleUsers();

	public StandardResponse<List<DepartmentRoles>> getAllDepartmentRoles(List<PrivilegeUdt> assignedPrivileges);

	public StandardResponse<Set<RoleUdt>> getDepartmentRoles(UUID departmentId);

}
