package com.montesinos.apikey.manager.service;

import java.util.List;

import com.montesinos.apikey.manager.domain.ApiKey;

public interface ApiKeyService {

	public ApiKey newKey(String apiScope, String userName, String uuid); 
	
	public ApiKey newKey(String apiScope);
		
	public boolean authenticateKey(String apiScope, String userName, String keyUuid);
	
	public void enableKey(String userName);
	
	public void disableKey(String userName);

	public List<ApiKey> getKeys();
	
	public ApiKey getKeyByUserName(String userName);
	
}
