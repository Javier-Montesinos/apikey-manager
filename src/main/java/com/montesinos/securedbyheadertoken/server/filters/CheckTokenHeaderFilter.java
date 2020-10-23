package com.montesinos.securedbyheadertoken.server.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.montesinos.securedbyheadertoken.server.service.ApiKeyServiceImpl;

@Component
@Order(1)
public class CheckTokenHeaderFilter implements Filter {

	@Autowired
	private ApiKeyServiceImpl apiKeyServiceImpl;
	
	@Value("${apikey.user.name}")
	private static final String API_KEY_USER_NAME = null;
	
	@Value("${apikey.name}")
	private static final String API_KEY_NAME = null;
	
	@Value("${apikey.scope}")
	private static final String API_SCOPE = null;	

	private static final Logger LOG = LoggerFactory.getLogger(CheckTokenHeaderFilter.class);
	
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		LOG.info("Initializing filter: {}", this);						 
	}	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
				
		String apiKey = req.getHeader(API_KEY_NAME);
		String userName = req.getHeader(API_KEY_USER_NAME);
		
		LOG.info("req : {}, user name: {}, api key value: {}", req.getRequestURI(), userName, apiKey);
		
		// se debe implementar con seguridad de spring para evitar esto
		if(req.getServletPath().startsWith("/keys/auth/")) {
			if(this.apiKeyServiceImpl.authenticateKey(API_SCOPE, userName, apiKey)) {
				chain.doFilter(request, response);
				
			} else {		
				res.sendError(HttpStatus.NOT_FOUND.value(), "Recurso no disponible");
				
			}	
		} else {			
			chain.doFilter(request, response);
		}
	}
	
	@Override
	public void destroy() {
		LOG.warn("Destructing filter: {}", this);
	}
		
}
