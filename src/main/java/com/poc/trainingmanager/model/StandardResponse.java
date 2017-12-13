package com.poc.trainingmanager.model;

public class StandardReponse {

	Object element;
	String error;
	String message;
	boolean status;

	public Object getElement() {
		return element;
	}

	public void setElement(Object element) {
		this.element = element;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "StandardReponse [element=" + element + ", error=" + error + ", message=" + message + ", status="
				+ status + "]";
	}

}
