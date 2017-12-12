package com.poc.trainingmanager.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.PrevilegeUdt;
import com.poc.trainingmanager.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {

	List<PrevilegeUdt> assignedPrevileges=null;
	
	@Autowired
	RoleService roleService;
	
	@RequestMapping(method=RequestMethod.GET)
	StandardResponse getAllRoles(){
		return roleService.getAllRoles(assignedPrevileges);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{roleName}")
	StandardResponse getRoleByName(@RequestParam("roleName") String roleName) {
		return roleService.getRoleByName(assignedPrevileges, roleName);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	StandardResponse insertRole(@RequestBody Role role) {
		return roleService.updateRole(assignedPrevileges, role);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/{roleId}")
	StandardResponse updateRole(@RequestBody Role role) {
		return roleService.updateRole(assignedPrevileges, role);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{roleId}")
	StandardResponse deleteRole(@RequestParam("roleId") UUID roleId) {
		return roleService.deleteRole(assignedPrevileges, roleId);
	}
}
