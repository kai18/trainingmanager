package com.poc.trainingmanager.exception;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.poc.trainingmanager.model.StandardResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomWebException extends WebApplicationException {

	private static final long serialVersionUID = -436387011481861336L;
	private static final ObjectMapper mapper = new ObjectMapper();

	public CustomWebException(StandardResponse error) {
		super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(buildMessageBody(error))
				.type(MediaType.APPLICATION_JSON).build());
	}

	private static String buildMessageBody(StandardResponse error) {
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		String message = "";
		try {
			message = mapper.writeValueAsString(error);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}

}
