package com.poc.trainingmanager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.poc.trainingmanager.model.User;

@Repository
public interface UserRepository extends CassandraRepository<User> {

	public User findById(UUID uuid);

	public User findByEmail(String email);

	public List<User> findByEmailContainingIgnoreCase(String email);

	public List<User> findByFirstNameContainingIgnoreCase(String firstName);

	public List<User> findByLastNameContainingIgnoreCase(String lastName);

	public List<User> findByFirstNameAndLastNameContainingIgnoreCase(String firstName, String lastName);

}
