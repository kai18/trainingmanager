package com.poc.trainingmanager.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

@Table("department")
public class Department implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6762819638637980192L;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((departmentCreatedDtm == null) ? 0 : departmentCreatedDtm.hashCode());
		result = prime * result + ((departmentDescription == null) ? 0 : departmentDescription.hashCode());
		result = prime * result + ((departmentId == null) ? 0 : departmentId.hashCode());
		result = prime * result + ((departmentName == null) ? 0 : departmentName.hashCode());
		result = prime * result + ((departmentUpdatedDtm == null) ? 0 : departmentUpdatedDtm.hashCode());
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
		Department other = (Department) obj;
		if (departmentCreatedDtm == null) {
			if (other.departmentCreatedDtm != null)
				return false;
		} else if (!departmentCreatedDtm.equals(other.departmentCreatedDtm))
			return false;
		if (departmentDescription == null) {
			if (other.departmentDescription != null)
				return false;
		} else if (!departmentDescription.equals(other.departmentDescription))
			return false;
		if (departmentId == null) {
			if (other.departmentId != null)
				return false;
		} else if (!departmentId.equals(other.departmentId))
			return false;
		if (departmentName == null) {
			if (other.departmentName != null)
				return false;
		} else if (!departmentName.equals(other.departmentName))
			return false;
		if (departmentUpdatedDtm == null) {
			if (other.departmentUpdatedDtm != null)
				return false;
		} else if (!departmentUpdatedDtm.equals(other.departmentUpdatedDtm))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Department [departmentId=" + departmentId + ", departmentName=" + departmentName
				+ ", departmentDescription=" + departmentDescription + ", departmentCreatedDtm=" + departmentCreatedDtm
				+ ", departmentUpdatedDtm=" + departmentUpdatedDtm + "]";
	}

}
