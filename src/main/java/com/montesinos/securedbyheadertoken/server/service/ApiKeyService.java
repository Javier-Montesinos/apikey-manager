package com.montesinos.securedbyheadertoken.server.service;

import java.util.List;

import com.montesinos.securedbyheadertoken.server.domain.ApiKey;

public interface ApiKeyService {

	public ApiKey newKey(String apiScope, String userName, String uuid); 
	
	public ApiKey newKey(String apiScope);
		
	public boolean authenticateKey(String apiScope, String userName, String keyUuid);
	
	public void revokeKey(String userName);

	public List<ApiKey> getKeys();
	
	public ApiKey getKeyByUserName(String userName);
	
}
