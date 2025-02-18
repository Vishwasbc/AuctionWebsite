package com.gateway.filter;

import java.nio.charset.StandardCharsets;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import com.gateway.util.JwtUtil;
import com.google.common.net.HttpHeaders;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	private JwtUtil util;
	
	private PathData data = new PathData("/user", "/product", "/auction", "/bids");

	public static interface Config {
	}

	public AuthenticationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			if (RouteValidator.isSecured.test(exchange.getRequest())) {
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					return handleUnauthorized(exchange.getResponse(), "Missing authorization header");
				}

				String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7);
				}

				try {
					String role = util.extractRolesFromToken(authHeader);
					String requestedPath = exchange.getRequest().getPath().toString();
					String method = exchange.getRequest().getMethod().name();

					if (!isAuthorized(role, requestedPath, method)) {
						return handleUnauthorized(exchange.getResponse(), "Unauthorized access");
					}

				} catch (Exception e) {
					return handleUnauthorized(exchange.getResponse(), "Invalid token");
				}
			}
			return chain.filter(exchange);
		};
	}

	private boolean isAuthorized(String role, String path, String method) {
		if ("ADMIN".equalsIgnoreCase(role)) {
			return path.startsWith(data.getUserPath()) || path.startsWith(data.getProductPath()) || path.startsWith(data.getAuctionPath())
					|| path.startsWith(data.getBidPath());
		} else if ("BIDDER".equalsIgnoreCase(role)) {
			return (path.startsWith(data.getUserPath()) || path.startsWith(data.getProductPath())) && method.equalsIgnoreCase("GET");
		} else if ("SELLER".equalsIgnoreCase(role)) {
			return ((path.startsWith(data.getUserPath()) || path.startsWith(data.getAuctionPath())) && method.equalsIgnoreCase("GET"))
					|| (path.startsWith(data.getProductPath()));
		}
		return false;
	}

	private Mono<Void> handleUnauthorized(ServerHttpResponse response, String message) {
		response.setStatusCode(HttpStatus.FORBIDDEN);
		byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
		response.getHeaders().setContentLength(bytes.length);
		return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
	}
}
