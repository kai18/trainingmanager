package com.poc.trainingmanager.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.poc.trainingmanager.exceptions.BadRequestException;
import com.poc.trainingmanager.model.User;
import com.poc.trainingmanager.utils.CommonUtils;

public class FieldValidator {

	public static boolean validateEmail(String email) {
		Pattern emailPattern = Pattern.compile(
				"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
		Matcher emailMatcher = emailPattern.matcher(email);
		return emailMatcher.matches();
	}

	public static boolean isNullOrIsEmpty(String... strings) {
		for (String string : strings) {
			if (string == null || string.isEmpty())
				return true;
		}
		return false;
	}
	
	public static void validateForUserInsert(User user) {
		if (!CommonUtils.isNull((user))) {
			if (CommonUtils.isStringNull(user.getEmailId())) {
				throw new BadRequestException("User insert Failed. Email ID cannot be null.");
			} else if (CommonUtils.isStringNull(user.getPhoneNumber())) {
				throw new BadRequestException("User insert Failed. Phone number cannot be null.");
			}
			else if (CommonUtils.isStringNull(user.getFirstName())) {
				throw new BadRequestException("User insert Failed. First name cannot be null.");
			}
			else if (CommonUtils.isStringNull(user.getLastName())) {
				throw new BadRequestException("User insert Failed. Last name cannot be null.");
			}
			else if (CommonUtils.isStringNull(user.getGender())) {
				throw new BadRequestException("User insert Failed. Gender cannot be null.");
			}
			else if (CommonUtils.isStringNull(user.getPassword())) {
				throw new BadRequestException("User insert Failed. Password cannot be null.");
			}
			else if (CommonUtils.isNull(user.getIsActive())) {
				throw new BadRequestException("User insert Failed. Is Active cannot be null.");
			}
			else if (CommonUtils.isNull(user.getAddress())) {
				throw new BadRequestException("User insert Failed. Address cannot be null.");
			}
			else if (CommonUtils.isNull(user.getRoles())) {
				throw new BadRequestException("User insert Failed. Role cannot be null.");
			}
			else if (CommonUtils.isNull(user.getDepartments())) {
				throw new BadRequestException("User insert Failed. Department cannot be null.");
			}
		} else {
			throw new BadRequestException("User insert Failed. User cannot be null.");
		}
	}

}
