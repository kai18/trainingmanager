package com.poc.trainingmanager.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {

	List<PrivilegeUdt> assignedPrivileges = null;

	@Autowired
	RoleService roleService;

	@CrossOrigin()
	@RequestMapping(method = RequestMethod.GET)
	StandardResponse<List<Role>> getAllRoles() {
		return roleService.getAllRoles(assignedPrivileges);
	}

	@CrossOrigin()
	@RequestMapping(method = RequestMethod.GET, value = "/name")
	StandardResponse<Role> getRoleByName(@PathParam("roleName") String roleName) {
		return roleService.getRoleByName(assignedPrivileges, roleName);
	}

	@CrossOrigin()
	@RequestMapping(method = RequestMethod.POST)
	StandardResponse<Role> addRole(@RequestBody Role role) {
		return roleService.addRole(assignedPrivileges, role);
	}

	@CrossOrigin()
	@RequestMapping(method = RequestMethod.PUT)
	StandardResponse<Role> updateRole(@RequestBody Role role) {
		return roleService.updateRole(assignedPrivileges, role);
	}

	@CrossOrigin()
	@RequestMapping(method = RequestMethod.DELETE)
	StandardResponse deleteRole(@RequestBody Role role) {
		return roleService.deleteRole(assignedPrivileges, role);
	}
}
