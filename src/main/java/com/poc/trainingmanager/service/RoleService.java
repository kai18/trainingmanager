package com.poc.trainingmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;

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
	 * @param assignedPrivileges  is a list of privileges associated to the User who invokes this operation.
	 */
	public StandardResponse<List<Role>> getAllRoles(List<PrivilegeUdt> assignedPrivileges);
	
	/**
	 * This method is used to fetch a specific existing Role with the given Role Name from the Role table.
	 * @param assignedPrivileges a list of privileges associated to the User who invokes this operation.
	 * @param roleName the unique name given for a Role
	 */
	public StandardResponse<Role> getRoleByName(List<PrivilegeUdt> assignedPrivileges, String roleName);
	
	//This method is used to insert a new Role into the Role table.
	public StandardResponse<Role> addRole(List<PrivilegeUdt> assignedPrivileges, Role role);

	//This method is used to update an existing Role in the Role table.
	public StandardResponse<Role> updateRole(List<PrivilegeUdt> assignedPrivileges, Role role);
    
	//This method is used to delete an existing Role in the Role table.
	public StandardResponse<Role> deleteRole(List<PrivilegeUdt> assignedPrivileges, String roleId);

	public StandardResponse<List<RoleUsers>> getAllRoleUsers(List<PrivilegeUdt> assignedPrivileges);

	public StandardResponse<List<DepartmentRoles>> getAllDepartmentRoles(List<PrivilegeUdt> assignedPrivileges);

}
