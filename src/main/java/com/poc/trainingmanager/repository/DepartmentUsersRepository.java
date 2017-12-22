package com.poc.trainingmanager.repository;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import com.poc.trainingmanager.model.DepartmentUsers;

public interface DepartmentUsersRepository extends CassandraRepository<DepartmentUsers>{
	
	public DepartmentUsers save(DepartmentUsers departmentUsers);
	public DepartmentUsers findByDepartmentId(UUID departmentId);
	public void delete(DepartmentUsers departmentUsers);
	

}