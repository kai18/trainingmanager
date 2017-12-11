package com.poc.trainingmanager.repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.poc.trainingmanager.model.Role;

@Repository
public interface RoleRepository extends CassandraRepository<Role> {

	public Role findByRoleId(UUID roleId);

}
