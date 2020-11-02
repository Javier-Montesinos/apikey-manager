package com.montesinos.securedbyheadertoken.server.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.montesinos.securedbyheadertoken.server.domain.ApiKey;
import com.montesinos.securedbyheadertoken.server.service.ApiKeyService;

/**
 * Clase para el manejo de key para apis rest
 * 
 * Colección Postman de pruebas: https://www.getpostman.com/collections/9b5b8b84c74a8369acbf
 *  
 * @author javiermontesinos
 *
 */
@RestController
public class ApiKeyRestController {
	
	@Autowired
	private ApiKeyService apiKeyService;
	
	@PostMapping("/keys/{apiScope}")
	public ApiKey createApiKey(@PathVariable String apiScope) {
		return this.apiKeyService.newKey(apiScope);
	}
	
	@GetMapping("/keys")
	public List<ApiKey> getKeys(){
		return this.apiKeyService.getKeys();
	}
	
	@GetMapping("/keys/{userName}")
	public ApiKey getKeys(@PathVariable String userName){
		return this.apiKeyService.getKeyByUserName(userName);
	}
	
	@GetMapping("/keys/auth/{apiScope}/{userName}/{keyUuid}")
	public String authenticate(@PathVariable String apiScope, 
			@PathVariable String userName, @PathVariable String keyUuid){
		return String.valueOf(this.apiKeyService.authenticateKey(apiScope, userName, keyUuid));
	}
	
	@PutMapping("/keys/{userName}/disable")
	public String diableKey(@PathVariable String userName){
		this.apiKeyService.disableKey(userName);
		
		return "Apikey disabled with uuid: " + userName; 
	}
	
	@PutMapping("/keys/{userName}/enable")
	public String enableKey(@PathVariable String userName){
		this.apiKeyService.enableKey(userName);
		
		return "Apikey enabled with uuid: " + userName; 
	}
	
	
}
