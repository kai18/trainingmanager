package com.poc.trainingmanager.repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.poc.trainingmanager.model.User;

@Repository
public interface UserRepository extends CassandraRepository<User> {

	public User findById(UUID uuid);

}
