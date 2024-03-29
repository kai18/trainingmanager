package com.poc.trainingmanager.repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.poc.trainingmanager.model.Department;

@Repository
public interface DepartmentRepository extends CassandraRepository<Department> {

	public Department findByDepartmentId(UUID uuid);
}
