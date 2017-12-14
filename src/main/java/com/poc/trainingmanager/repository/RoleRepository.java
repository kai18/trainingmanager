package com.poc.trainingmanager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.poc.trainingmanager.model.Role;

@Repository
public interface RoleRepository extends CassandraRepository<Role> {

	public List<Role> findAll();
	public Role findByRoleId(UUID roleId);
	public Role findByRoleName(String roleName);
	public Role findByRoleType(String roleType);
	public Role save(Role role);
	public void delete(Role role);

}
