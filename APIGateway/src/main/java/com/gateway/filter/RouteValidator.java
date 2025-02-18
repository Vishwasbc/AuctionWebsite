package com.gateway.filter;

import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {
	
	private RouteValidator() {
		
	}

	protected static final String[] OPEN_API_ENDPOINTS = { "/user/register","/user/login", "/eureka" };

	public static final Predicate<ServerHttpRequest> isSecured = request -> {
		String path = request.getPath().toString();
		for (String endpoint : OPEN_API_ENDPOINTS) {
			if (path.contains(endpoint)) {
				return false; // End point does not require authorization
			}
		}
		return true; // End point requires authorization
	};

}
