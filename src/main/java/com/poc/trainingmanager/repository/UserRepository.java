package com.poc.trainingmanager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.RoleUdt;

@Repository
public interface UserRepository extends CassandraRepository<User> {

	public User save(User user);
	
	public User findById(UUID uuid);

	public User findByEmailId(String email);

	public List<User> findByEmailIdContainingIgnoreCase(String email);

	public List<User> findByFirstNameContainingIgnoreCase(String firstName);

	public List<User> findByLastNameContainingIgnoreCase(String lastName);

	public List<User> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName,
			String lastName);

	public List<User> findByRoles(RoleUdt roleUdt);
}
