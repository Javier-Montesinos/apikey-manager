package com.montesinos.apikey.manager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.montesinos.apikey.manager.dao.ApiKeyRepository;
import com.montesinos.apikey.manager.domain.ApiKey;
import com.montesinos.apikey.manager.rest.ApiKeyExistsException;
import com.montesinos.apikey.manager.rest.ApiKeyNotFoundException;

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
		Optional<ApiKey> key = this.apiKeyRepository.findByUsername(this.apiKeyTestUsername);
		if(apiKeyTestEnable && !key.isPresent()) {
			newKey(this.apiKeyTestApiScope, this.apiKeyTestUsername, this.apiKeyTestUuid);
		}
		
		Optional<ApiKey> key2 = this.apiKeyRepository.findByUsername(this.apiKeyTestUsername2);
		if(apiKeyTestEnable && !key2.isPresent()) {
			newKey(this.apiKeyTestApiScope2, this.apiKeyTestUsername2, this.apiKeyTestUuid2);
		}
	}
	
	public ApiKey newKey(String apiScope, String userName, String uuid) {
		Optional<ApiKey> existsKey = this.apiKeyRepository.findByUsername(userName);
		if(existsKey.isPresent()) {
			throw new ApiKeyExistsException("Username exists");
		} 
		ApiKey key = new ApiKey(apiScope, userName, uuid, true);
		String hashedPwd = bcryptPasswordEncoder.encode(uuid);		
		key.setHashedUuid(hashedPwd);
						
		this.apiKeyRepository.save(key);
		
		LOG.info("Key saved: {}", key);
	
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
		Optional<ApiKey> apikey = this.apiKeyRepository.findByUsernameAndApiScopeAndActive(userName, apiScope, true);
		
		if(apikey.isPresent()) {
			ApiKey keyBD = apikey.get();
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
		Optional<ApiKey> apikey = this.apiKeyRepository.findByUsername(userName);
		
		if(apikey.isPresent()) {
			ApiKey keyBD = apikey.get();		
			keyBD.setActive(false);
			
			this.apiKeyRepository.save(keyBD);
		} else {
			throw new ApiKeyNotFoundException("Api Key not found");
		}		
	}

	@Override
	public void enableKey(String userName) {
		Optional<ApiKey> apikey = this.apiKeyRepository.findByUsername(userName);
		
		if(apikey.isPresent()) {
			ApiKey keyBD = apikey.get();
			
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
		Optional<ApiKey> apikey = this.apiKeyRepository.findByUsername(userName);
		
		if(apikey.isPresent()) {
			return apikey.get();
		} else {
			throw new ApiKeyNotFoundException("Api Key not found");
		}
	}

}
