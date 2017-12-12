package com.poc.trainingmanager.model.cassandraudt;

import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.UserDefinedType;

import com.datastax.driver.core.DataType;
import java.util.UUID;

@UserDefinedType("previlege")
public class PrevilegeUdt {

	@Column("creation")
	private int creationPrevilege;
	
	@Column("deletion")
	private int deletionPrevilege;

	@Column("updation")
	private int updationPrevilege;
	
	@Column("read")
	private int readPrevilege;
	
	@CassandraType(type = DataType.Name.UUID, userTypeName = "department_id")
	private UUID department_id;
}
