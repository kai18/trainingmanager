package com.poc.trainingmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.service.RoleService;
import com.poc.trainingmanager.model.*;

@RestController
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	RoleService roleService;
	
	@RequestMapping(method=RequestMethod.GET)
	StandardResponse getAllRoles(){
		return roleService.getAllRoles();
	}
}
