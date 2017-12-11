package com.poc.trainingmanager.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

@Table("department")
public class Department {

	@PrimaryKeyColumn(name = "DEPARTMENT_ID", type = PrimaryKeyType.PARTITIONED)
	@Column("DEPARTMENT_ID")
	private UUID departmentId;

	@Column("DEPARTMENT_NAME")
	private String departmentName;

	@Column("DEPARTMENT_DESCRIPTION")
	private String departmentDescription;

	@Column("CREATED_DTM")
	private Date departmentCreatedDtm;

	@Column("UPDATED_DTM")
	private Date departmentUpdatedDtm;

	public UUID getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(UUID departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentDescription() {
		return departmentDescription;
	}

	public void setDepartmentDescription(String departmentDescription) {
		this.departmentDescription = departmentDescription;
	}

	public Date getDepartmentCreatedDtm() {
		return departmentCreatedDtm;
	}

	public void setDepartmentCreatedDtm(Date departmentCreatedDtm) {
		this.departmentCreatedDtm = departmentCreatedDtm;
	}

	public Date getDepartmentUpdatedDtm() {
		return departmentUpdatedDtm;
	}

	public void setDepartmentUpdatedDtm(Date departmentUpdatedDtm) {
		this.departmentUpdatedDtm = departmentUpdatedDtm;
	}

	public Department(UUID departmentId, String departmentName, String departmentDescription, Date departmentCreatedDtm,
			Date departmentUpdatedDtm) {
		super();
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.departmentDescription = departmentDescription;
		this.departmentCreatedDtm = departmentCreatedDtm;
		this.departmentUpdatedDtm = departmentUpdatedDtm;
	}

	@Override
	public String toString() {
		return "Department [departmentId=" + departmentId + ", departmentName=" + departmentName
				+ ", departmentDescription=" + departmentDescription + ", departmentCreatedDtm=" + departmentCreatedDtm
				+ ", departmentUpdatedDtm=" + departmentUpdatedDtm + "]";
	}

}
