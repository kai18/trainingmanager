package com.poc.trainingmanager.controller;

import java.util.List;
import java.util.UUID;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrivilegeUdt;
import com.poc.trainingmanager.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {

	List<PrivilegeUdt> assignedPrivileges=null;
	
	@Autowired
	RoleService roleService;
	
	@RequestMapping(method=RequestMethod.GET)
	StandardResponse<List<Role>> getAllRoles(){
		return roleService.getAllRoles(assignedPrivileges);
	}
	
	//not fetching
	@RequestMapping(method=RequestMethod.GET, value="/name")
	StandardResponse<Role> getRoleByName(@PathParam("roleName") String roleName) {
		return roleService.getRoleByName(assignedPrivileges, roleName);
	}
	
	//not posting
	@RequestMapping(method=RequestMethod.POST)
	StandardResponse insertRole(@RequestBody Role role) {
		return roleService.updateRole(assignedPrivileges, role);
	}
	
	//updating, but making privilege field null
	@RequestMapping(method=RequestMethod.PUT)
	StandardResponse updateRole(@RequestBody Role role) {
		return roleService.updateRole(assignedPrivileges, role);
	}
	
	@RequestMapping(method=RequestMethod.DELETE)
	StandardResponse deleteRole(@RequestBody Role role) {
		return roleService.deleteRole(assignedPrivileges, role);
	}
}
