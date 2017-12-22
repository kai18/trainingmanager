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
	
	@CassandraType(type = DataType.Name.UUID)
	@Column("department_id")
	private UUID department_id;

	public int getCreationPrivilege() {
		return creationPrivilege;
	}

	public void setCreationPrivilege(int creationPrivilege) {
		this.creationPrivilege = creationPrivilege;
	}

	public int getDeletionPrivilege() {
		return deletionPrivilege;
	}

	public void setDeletionPrivilege(int deletionPrivilege) {
		this.deletionPrivilege = deletionPrivilege;
	}

	public int getUpdationPrivilege() {
		return updationPrivilege;
	}

	public void setUpdationPrivilege(int updationPrivilege) {
		this.updationPrivilege = updationPrivilege;
	}

	public int getReadPrivilege() {
		return readPrivilege;
	}

	public void setReadPrivilege(int readPrivilege) {
		this.readPrivilege = readPrivilege;
	}

	public UUID getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(UUID department_id) {
		this.department_id = department_id;
	}

	public PrivilegeUdt(int creationPrivilege, int deletionPrivilege, int updationPrivilege, int readPrivilege,
			UUID department_id) {
		super();
		this.creationPrivilege = creationPrivilege;
		this.deletionPrivilege = deletionPrivilege;
		this.updationPrivilege = updationPrivilege;
		this.readPrivilege = readPrivilege;
		this.department_id = department_id;
	}
	
	public PrivilegeUdt() {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + creationPrivilege;
		result = prime * result + deletionPrivilege;
		result = prime * result + ((department_id == null) ? 0 : department_id.hashCode());
		result = prime * result + readPrivilege;
		result = prime * result + updationPrivilege;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrivilegeUdt other = (PrivilegeUdt) obj;
		if (creationPrivilege != other.creationPrivilege)
			return false;
		if (deletionPrivilege != other.deletionPrivilege)
			return false;
		if (department_id == null) {
			if (other.department_id != null)
				return false;
		} else if (!department_id.equals(other.department_id))
			return false;
		if (readPrivilege != other.readPrivilege)
			return false;
		if (updationPrivilege != other.updationPrivilege)
			return false;
		return true;
	}
}
