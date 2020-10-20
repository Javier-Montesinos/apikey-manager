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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.montesinos.securedbyheadertoken.server.service.ApiKeyServiceImpl;

@Component
@Order(1)
public class CheckTokenHeaderFilter implements Filter {

	private final static Logger LOG = LoggerFactory.getLogger(CheckTokenHeaderFilter.class);
	
	@Value("${apikey.user.name}")
	private final String apiKeyUserName = null;
	
	@Value("${apikey.name}")
	private final String apiKeyName = null;
	
	@Value("${apikey.scope}")
	private final String apiScope = null;	

	@Autowired
	private ApiKeyServiceImpl apiKeyServiceImpl;
	
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		LOG.info("Initializing filter :{}", this);						 
	}	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
				
		String apiKey = req.getHeader(this.apiKeyName);
		String userName = req.getHeader(this.apiKeyUserName);
		
		LOG.info("req : {}, user name: {}, api key value: {}", req.getRequestURI(), userName, apiKey);
		
		// TODO implementar con seguridad de spring
		if(req.getServletPath().startsWith("/keys/auth/")) {
			if(this.apiKeyServiceImpl.authenticateKey(this.apiScope, userName, apiKey) == true) {
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
		LOG.warn("Destructing filter :{}", this);
	}
		
}
