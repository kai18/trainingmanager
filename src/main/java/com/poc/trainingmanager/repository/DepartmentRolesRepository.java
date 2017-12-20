package com.poc.trainingmanager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;
import com.poc.trainingmanager.model.DepartmentRoles;

@Repository
public interface DepartmentRolesRepository extends CassandraRepository<DepartmentRoles> {
	
	public DepartmentRoles findByRoles(RoleUdt role);
	public DepartmentRoles findByDepartmentId(UUID departmentId);
	public DepartmentRoles save(DepartmentRoles departmentRoles);
	public void delete(DepartmentRoles departmentRoles);
	
}
