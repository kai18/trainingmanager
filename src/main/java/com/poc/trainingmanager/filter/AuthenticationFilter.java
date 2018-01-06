package com.poc.trainingmanager.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import com.poc.trainingmanager.exceptions.AccessDeniedException;
import com.poc.trainingmanager.model.StandardResponse;
import com.poc.trainingmanager.model.cassandraudt.DepartmentUdt;
import com.poc.trainingmanager.model.wrapper.LoggedInUserWrapper;

@Component
@CrossOrigin
public class AuthenticationFilter extends GenericFilterBean {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		LOGGER.info("filter");

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		LOGGER.debug("Request detected: ", httpRequest.getMethod());

		if (httpRequest.getMethod().equals("GET") || httpRequest.getMethod().equals("POST")
				|| httpRequest.getMethod().equals("PUT") || httpRequest.getMethod().equals("DELETE")) {

			String jwtToken = httpRequest.getHeader("jwt-token");

			Map<String, Object> loggedInUserDetails = this.isValidJwtToken(jwtToken);
			if (loggedInUserDetails != null) {
				LoggedInUserWrapper user = new LoggedInUserWrapper();
				user.setDepartments((Set<DepartmentUdt>) loggedInUserDetails.get("departments"));
				request.setAttribute("loggedInUser", user);
				filterChain.doFilter(request, response);
			} else {
				throw new AccessDeniedException("Invalid JWT Token");
			}

		}
	}

	private Map<String, Object> isValidJwtToken(String jwtToken) {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("jwt-token", jwtToken);
		HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);

		RestTemplate restTemplate = new RestTemplate();

		try {
			ResponseEntity<StandardResponse> jwtAuthenticationResponse = restTemplate
					.exchange("http://localhost:9082/verify", HttpMethod.GET, entity, StandardResponse.class);
			if (jwtAuthenticationResponse != null) {
				StandardResponse authenticationResponse = jwtAuthenticationResponse.getBody();
				if (authenticationResponse.getStatus().equals("SUCCESS")) {
					LOGGER.error("SUCESS");
					return (Map<String, Object>) jwtAuthenticationResponse.getBody().getElement();
				}
			}

		} catch (Exception e) {
		}
		return null;
	}

}