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

@RestController
public class ApiKeysRestController {
	
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
	
	@PutMapping("/keys/{keyUuid}")
	public String revokeKey(@PathVariable String keyUuid){
		this.apiKeyService.revokeKey(keyUuid);
		
		return "Apikey revoked with uuid: " + keyUuid; 
	}
	
	
}
