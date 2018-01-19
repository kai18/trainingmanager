package com.poc.trainingmanager.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.exceptions.DuplicateDataException;
import com.poc.trainingmanager.exceptions.ResourceNotFoundException;
import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.DepartmentUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.DepartmentService;
import com.poc.trainingmanager.service.helper.DepartmentDeleteHelper;
import com.poc.trainingmanager.service.helper.DepartmentInsertHelper;
import com.poc.trainingmanager.service.helper.DepartmentUpdateHelper;
import com.poc.trainingmanager.utils.CommonUtils;
import com.poc.trainingmanager.utils.PrivilegeChecker;

/**
 * DepartmentService methods allow CRUD operations to be done on the department
 * table. Department table has department ID which is of UUID type and is
 * generated in the database, departmentName which is of String type and has the
 * name of the department, departmentDescription which is of string type and
 * gives a brief description of the department. The department name and
 * description are the only parameters that can be altered only by the system
 * admin or the department admin or anyone with that specific privilege in the
 * role table.
 */

@Service
public class DepartmentServiceImpl implements DepartmentService {

	private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

	@Autowired
	PrivilegeChecker privilegeChecker;
	@Autowired
	DepartmentInsertHelper departmentInsertHelper;

	@Autowired
	DepartmentUpdateHelper departmentUpdateHelper;

	@Autowired
	DepartmentDeleteHelper departmentDeleteHelper;

	@Autowired
	DepartmentRepository departmentRepository;

	@Autowired
	DepartmentRolesRepository departmentRolesRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	DepartmentUsersRepository departmentUsersRepository;

	/*
	 * method to get all the departments of the department table.
	 * 
	 * @see com.poc.trainingmanager.service.DepartmentService#getAllDepartments()
	 */
	@Override
	public StandardResponse<List<Department>> getAllDepartments() {
		StandardResponse<List<Department>> standardResponse = new StandardResponse<List<Department>>();
		List<Department> allDepartment = departmentRepository.findAll();
		if (allDepartment == null)
			throw new ResourceNotFoundException("Department with ID not found.");
		logger.info("All departments fetched successfully");
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("All department fetched");
		standardResponse.setElement(allDepartment);
		return standardResponse;
	}

	/*
	 * method to insert a new department in the database. The departmentId is
	 * randomly generated. DepartmentName and departmentDescription are provided by
	 * the user and the departmentCreatedDtm is set by taking the time during
	 * insertion once inserted, the record is inserted in the departmentRoles,
	 * departmentUsers tables as well.
	 * 
	 * @see com.poc.trainingmanager.service.DepartmentService#addDepartment(com.poc.
	 * trainingmanager.model.Department)
	 */
	@Override
	public StandardResponse<Department> addDepartment(Department department, LoggedInUserWrapper loggedInUser) {
		StandardResponse<Department> standardResponse = new StandardResponse<Department>();

		privilegeChecker.isAllowedToCreateDepartment(loggedInUser.getPrivileges());

		if (department == null) {
			logger.error("Inserted department was null, hence failed to Add department");
			standardResponse.setCode(422);
			standardResponse.setStatus("Failed");
			standardResponse.setMessage("Department can't be null, failed to add this department");
			return standardResponse;
		}
		if (departmentRepository.findByDepartmentName(department.getDepartmentName()) != null)
			throw new DuplicateDataException("Department addition failed as it already exists.");

		// insert into Department table
		Department departmentInserted = departmentInsertHelper.insertIntoDepartment(department);
		logger.info("Department {" + department + "} successfully added");

		// insert into Department_Users table
		departmentInsertHelper.insertIntoDepartmentUsers(departmentInserted);
		logger.info("Department {" + department + "} successfully added to Department_Users");

		// insert into Department_Roles
		departmentInsertHelper.insertIntoDepartmentRoles(departmentInserted);
		logger.info("Department {" + department + "} successfully added to Department_Roles");

		if (departmentInserted == null) {
			logger.error("Department {" + department + "} insertion failed");
			standardResponse.setMessage("Department not inserted");
			return standardResponse;
		}
		logger.info("Department {" + department + "} successfully added");
		logger.info("DepartmentRoles {" + department + "} successfully updated with the new department");
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Department inserted successfully");
		standardResponse.setElement(departmentInserted);
		return standardResponse;
	}

	/*
	 * Method to update the department name and description with the departmentId as
	 * reference. The old record is removed and then the new record with the updated
	 * name and description is added. Once updated, the record gets updated in user,
	 * department, departmentRoles and departmentUsers tables.
	 * 
	 * @see
	 * com.poc.trainingmanager.service.DepartmentService#updateDepartment(com.poc.
	 * trainingmanager.model.Department)
	 */
	@Override
	public StandardResponse<Department> updateDepartment(Department department, LoggedInUserWrapper loggedInUser) {
		StandardResponse<Department> standardResponse = new StandardResponse<Department>();

		privilegeChecker.isAllowedToEditDepartment(loggedInUser.getPrivileges(), department.getDepartmentId());

		Date date = new Date();

		// fetch the old department from department table
		Department oldDepartment = departmentRepository.findByDepartmentId(department.getDepartmentId());
		// update the department in department table
		Department updatedDepartment = departmentUpdateHelper.updateDepartment(oldDepartment, department);

		oldDepartment = departmentRepository.findByDepartmentId(department.getDepartmentId());

		if (oldDepartment == null)
			throw new ResourceNotFoundException("The department to be updated does not exist.");

		logger.info("Department {" + department + "} successfully updated in department table");

		// update the Department in department_users table
		departmentUpdateHelper.updateDepartmentUsers(oldDepartment, updatedDepartment);
		logger.info("Department in DepartmentUsers successfully updated");

		logger.info("Department {" + department + "} successfully updated in user table");

		if (CommonUtils.isStringNull(updatedDepartment.getDepartmentName())
				|| CommonUtils.isStringNull(updatedDepartment.getDepartmentDescription())) {
			logger.error("Department {" + department + "} updation failed");
			standardResponse.setCode(409);
			standardResponse.setMessage("Failed");
			standardResponse.setMessage("Department name and description cannot be empty");
			return standardResponse;
		}
		logger.info("Department {" + department + "} successfully updated");
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Department updated successfully");
		standardResponse.setElement(updatedDepartment);
		return standardResponse;
	}

	/*
	 * method to delete a record from the database. with the departmentId as the
	 * reference, the record is deleted form the department,
	 * departmentUsers,departmentRoles and the user table.
	 * 
	 * @see
	 * com.poc.trainingmanager.service.DepartmentService#deleteDepartment(com.poc.
	 * trainingmanager.model.Department)
	 */
	@Override
	public StandardResponse deleteDepartment(String deptId, LoggedInUserWrapper loggedInUser) {

		this.privilegeChecker.IsAllowedToDeleteDepartment(loggedInUser.getPrivileges());

		// delete from Department table
		departmentDeleteHelper.deleteFromDepartment(deptId);

		// delete from department_users table
		departmentDeleteHelper.deleteFromDepartmentUsers(deptId);

		// delete from department_roles table
		departmentDeleteHelper.deleteFromDepartmentRoles(deptId);

		UUID departmentId = UUID.fromString(deptId);
		StandardResponse standardResponse = new StandardResponse<Object>();
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Department deleted successfully");
		logger.info("Department with ID {" + departmentId + "} successfully deleted");
		return standardResponse;
	}

}
