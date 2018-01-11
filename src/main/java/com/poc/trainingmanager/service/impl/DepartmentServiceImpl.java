package com.poc.trainingmanager.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.trainingmanager.constants.Constants;
import com.poc.trainingmanager.exceptions.DuplicateDataException;
import com.poc.trainingmanager.exceptions.InvalidRequestDataException;
import com.poc.trainingmanager.exceptions.ResourceNotFoundException;
import com.poc.trainingmanager.model.Department;
import com.poc.trainingmanager.model.DepartmentRoles;
import com.poc.trainingmanager.model.DepartmentUsers;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.cassandraudt.UserUdt;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;
import com.poc.trainingmanager.model.wrapper.WrapperUtil;
import com.poc.trainingmanager.repository.DepartmentRepository;
import com.poc.trainingmanager.repository.DepartmentRolesRepository;
import com.poc.trainingmanager.repository.DepartmentUsersRepository;
import com.poc.trainingmanager.repository.UserRepository;
import com.poc.trainingmanager.service.DepartmentService;
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
	DepartmentRepository departmentRepository;

	@Autowired
	DepartmentRolesRepository departmentRolesRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	DepartmentUsersRepository departmentUsersRepository;

	@Autowired
	PrivilegeChecker privilegeChecker;

	/*
	 * method to get all the departments of the department table.
	 * 
	 * @see com.poc.trainingmanager.service.DepartmentService#getAllDepartments()
	 */
	@Override
	public StandardResponse<List<Department>> getAllDepartments() {
		StandardResponse<List<Department>> standardResponse = new StandardResponse<List<Department>>();
		List<Department> allDepartment = departmentRepository.findAll();
		if (allDepartment == null) {
			throw new ResourceNotFoundException("No departments found");
		}
		logger.info("All departments fetched successfully");
		standardResponse.setCode(200);
		standardResponse.setStatus(Constants.SUCCESS);
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

		Date date = new Date();
		if (department == null) {
			throw new InvalidRequestDataException("Invalid department object provided");
		}
		if (departmentRepository.findByDepartmentName(department.getDepartmentName()) != null) {
			throw new DuplicateDataException("Department already exists");
		}
		department.setDepartmentId(UUID.randomUUID());
		department.setDepartmentCreatedDtm(date);
		department.setDepartmentUpdatedDtm(date);
		Department addedDepartment = departmentRepository.save(department);
		if (CommonUtils.isNull(addedDepartment)) {
			standardResponse.setMessage("Department cannot be empty.");
			return standardResponse;
		}
		DepartmentRoles departmentRoles = new DepartmentRoles();
		departmentRoles.setDepartmentId(department.getDepartmentId());
		departmentRoles.setRoles(null);
		DepartmentRoles departmentRole = departmentRolesRepository.save(departmentRoles);
		DepartmentUsers departmentUsers = new DepartmentUsers();
		departmentUsers.setDepartmentId(department.getDepartmentId());
		departmentUsers.setUserDepartmentsUdt(null);
		DepartmentUsers departmentUser = departmentUsersRepository.save(departmentUsers);
		if (departmentRole == null || departmentUser == null) {
			logger.error("Department {" + department + "} insertion failed");
			standardResponse.setMessage("Department not inserted");
			return standardResponse;
		}
		logger.info("Department {" + department + "} successfully added");
		logger.info("DepartmentRoles {" + departmentRole + "} successfully updated with the new department");
		standardResponse.setCode(200);
		standardResponse.setStatus("Success");
		standardResponse.setMessage("Department inserted successfully");
		standardResponse.setElement(addedDepartment);
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

		privilegeChecker.isAllowedToEditDepartment(loggedInUser.getPrivileges(), department.getDepartmentId());

		StandardResponse<Department> standardResponse = new StandardResponse<Department>();
		Date date = new Date();
		Department oldDepartment = new Department();
		oldDepartment = departmentRepository.findByDepartmentId(department.getDepartmentId());

		if (oldDepartment == null)
			throw new ResourceNotFoundException("Department to be updated does not exist");

		DepartmentUdt oldDepartmentUdt = WrapperUtil.departmentToDepartmentUdt(oldDepartment);
		Department newDepartment = new Department();
		newDepartment = oldDepartment;
		newDepartment.setDepartmentUpdatedDtm(date);
		newDepartment.setDepartmentDescription(department.getDepartmentDescription());
		newDepartment.setDepartmentName(department.getDepartmentName());

		Department updatedDepartment = departmentRepository.save(newDepartment);
		logger.info("Department {" + department + "} successfully updated in department table");
		User user = new User();
		Set<UserUdt> userList = new HashSet<UserUdt>();
		DepartmentUsers departmentUsers = new DepartmentUsers();
		departmentUsers = departmentUsersRepository.findByDepartmentId(department.getDepartmentId());

		if (departmentUsers != null) {
			userList = departmentUsers.getUserDepartmentsUdt();
			if (userList != null) {

				Set<UserUdt> newUserUdts = new HashSet<UserUdt>();
				for (Iterator<UserUdt> userUdtIterator = userList.iterator(); userUdtIterator.hasNext();) {
					UserUdt userUdt = userUdtIterator.next();
					user = userRepository.findById(userUdt.getId());
					Set<DepartmentUdt> departmentList = new HashSet<DepartmentUdt>();
					departmentList = userUdt.getDepartments();
					departmentList.remove(oldDepartmentUdt);
					departmentList.add(WrapperUtil.departmentToDepartmentUdt(newDepartment));
					userUdt.setDepartments(departmentList);
					user.setDepartments(departmentList);
					userRepository.save(user);
					newUserUdts.add(userUdt);
					logger.info("Department {" + department + "} successfully updated in user table");
				}
				departmentUsers.setUserDepartmentsUdt(newUserUdts);
				departmentUsersRepository.save(departmentUsers);
				logger.info("Department {" + department + "} successfully updated in departmentUser table");
			}
		}
		if (CommonUtils.isStringNull(updatedDepartment.getDepartmentName())
				|| CommonUtils.isStringNull(updatedDepartment.getDepartmentDescription())) {
			throw new InvalidRequestDataException("Department name or description cannot be empty");
		}
		logger.info("Department {" + department + "} successfully updated");
		standardResponse.setCode(200);
		standardResponse.setStatus(Constants.SUCCESS);
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
	public StandardResponse<Object> deleteDepartment(String deptId, LoggedInUserWrapper loggedInUser) {

		this.privilegeChecker.IsAllowedToDeleteDepartment(loggedInUser.getPrivileges());

		UUID departmentId = UUID.fromString(deptId);
		StandardResponse<Object> standardResponse = new StandardResponse<Object>();
		DepartmentRoles departmentRoles = new DepartmentRoles();
		departmentRoles = departmentRolesRepository.findByDepartmentId(departmentId);
		departmentRolesRepository.delete(departmentRoles);
		DepartmentUsers departmentUsers = new DepartmentUsers();
		departmentUsers = departmentUsersRepository.findByDepartmentId(departmentId);
		departmentUsersRepository.delete(departmentUsers);
		departmentRepository.delete(departmentRepository.findByDepartmentId(departmentId));
		standardResponse.setCode(200);
		standardResponse.setStatus(Constants.SUCCESS);
		standardResponse.setMessage("Department deleted successfully");
		logger.info("Department with ID {" + departmentId + "} successfully deleted");
		return standardResponse;
	}
}
