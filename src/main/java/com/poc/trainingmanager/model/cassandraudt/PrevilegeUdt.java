package com.poc.trainingmanager.model.cassandraudt;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.UserDefinedType;

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
}
