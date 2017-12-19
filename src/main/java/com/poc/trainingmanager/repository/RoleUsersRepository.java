package com.poc.trainingmanager.repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.poc.trainingmanager.model.Role;
import com.poc.trainingmanager.model.RoleUsers;

@Repository
public interface RoleUsersRepository extends CassandraRepository<RoleUsers> {

	public RoleUsers save(RoleUsers roleUsers); 
	public RoleUsers findByRoleId(UUID roleId);
	public void delete(RoleUsers roleUsers);
}
