package com.poc.trainingmanager.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.poc.trainingmanager.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
l
}
