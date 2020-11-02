package com.montesinos.securedbyheadertoken.server.service;

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.montesinos.securedbyheadertoken.server.dao.ApiKeyRepository;
import com.montesinos.securedbyheadertoken.server.domain.ApiKey;
import com.montesinos.securedbyheadertoken.server.rest.ApiKeyNotFoundException;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {	
	
	@Value("${apikey.test.enable}")
	private boolean apiKeyTestEnable;
	
	@Value("${apikey.test.username}")
	private String apiKeyTestUsername;
	
	@Value("${apikey.test.uuid}")
	private String apiKeyTestUuid;
	
	@Value("${apikey.test.apiscope}")
	private String apiKeyTestApiScope;
	
	@Value("${apikey.test2.username}")
	private String apiKeyTestUsername2;
	
	@Value("${apikey.test2.uuid}")
	private String apiKeyTestUuid2;
	
	@Value("${apikey.test2.apiscope}")
	private String apiKeyTestApiScope2;
	
	@Autowired
	private ApiKeyRepository apiKeyRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	private static final Logger LOG = LoggerFactory.getLogger(ApiKeyServiceImpl.class);
	
	@PostConstruct
	private void loadData() {		
		List<ApiKey> apis = this.apiKeyRepository.findByUsername(this.apiKeyTestUsername);
		if(apiKeyTestEnable && apis.isEmpty()) {
			newKey(this.apiKeyTestApiScope, this.apiKeyTestUsername, this.apiKeyTestUuid);
		}
		
		List<ApiKey> apis2 = this.apiKeyRepository.findByUsername(this.apiKeyTestUsername2);
		if(apiKeyTestEnable && apis2.isEmpty()) {
			newKey(this.apiKeyTestApiScope2, this.apiKeyTestUsername2, this.apiKeyTestUuid2);
		}
	}
	
	public ApiKey newKey(String apiScope, String userName, String uuid) {
		String hashedPwd = bcryptPasswordEncoder.encode(uuid);
		ApiKey key = new ApiKey(userName, uuid, hashedPwd, true);
		LOG.info("Key generated: {}", key);
		
		key.setUuid(null);
		key.setApiScope(apiScope);		
		
		this.apiKeyRepository.save(key);
	
		return key;
	}
	
	@Override
	public ApiKey newKey(String apiScope) {
		String userName = UUID.randomUUID().toString();
		String uuid = UUID.randomUUID().toString();
		
		return newKey(apiScope, userName, uuid);		
	}
		
	@Override
	public boolean authenticateKey(String apiScope, String userName, String keyUuid) {
		boolean retValue = false;
		List<ApiKey> apikey = this.apiKeyRepository.findByUsernameAndApiScopeAndActive(userName, apiScope, true);
		
		if(apikey!= null && apikey.size() == 1) {
			ApiKey keyBD = apikey.get(0);
			if(bcryptPasswordEncoder.matches(keyUuid, keyBD.getHashedUuid())){
				retValue = true;
			}
		} else {
			throw new ApiKeyNotFoundException("Api Key not found");
		}
		
		return retValue;
	}

	@Override
	public void disableKey(String userName) {
		List<ApiKey> apikey = this.apiKeyRepository.findByUsername(userName);
		
		if(apikey != null && apikey.size() == 1) {
			ApiKey keyBD = apikey.get(0);
			
			keyBD.setActive(false);
			
			this.apiKeyRepository.save(keyBD);
		} else {
			throw new ApiKeyNotFoundException("Api Key not found");
		}		
	}

	@Override
	public void enableKey(String userName) {
		List<ApiKey> apikey = this.apiKeyRepository.findByUsername(userName);
		
		if(apikey != null && apikey.size() == 1) {
			ApiKey keyBD = apikey.get(0);
			
			keyBD.setActive(true);
			
			this.apiKeyRepository.save(keyBD);
		} else {
			throw new ApiKeyNotFoundException("Api Key not found");
		}		
	}

	@Override
	public List<ApiKey> getKeys() {
		return this.apiKeyRepository.findAll();
	}
	
	@Override
	public ApiKey getKeyByUserName(String userName) {
		List<ApiKey> apikey = this.apiKeyRepository.findByUsername(userName);
		
		if(apikey != null && apikey.size() == 1) {
			return apikey.get(0);
		} else {
			throw new ApiKeyNotFoundException("Api Key not found");
		}
	}

}
