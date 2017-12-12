package com.poc.trainingmanager.model.cassandraudt;

import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.UserDefinedType;

import com.datastax.driver.core.DataType;
import java.util.UUID;

@UserDefinedType("privilege")
public class PrivilegeUdt {

	@Column("creation")
	private int creationPrivilege;
	
	@Column("deletion")
	private int deletionPrivilege;

	@Column("updation")
	private int updationPrivilege;
	
	@Column("read")
	private int readPrivilege;
	
	@Column("department_id")
	private UUID department_id;
}
