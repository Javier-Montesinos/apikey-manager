package com.montesinos.securedbyheadertoken.server.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.montesinos.securedbyheadertoken.server.dao.ApiKeyRepository;
import com.montesinos.securedbyheadertoken.server.domain.ApiKey;

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
		SecureRandom random = null;
		MessageDigest md = null;
		byte[] hashedPassword = null;
		byte[] salt = new byte[16];
		try {
			random = SecureRandom.getInstance("SHA1PRNG");			
			random.nextBytes(salt);
			
			md = MessageDigest.getInstance("SHA-256");
			md.update(salt);
			
			hashedPassword = md.digest(uuid.getBytes(StandardCharsets.UTF_8));			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
						
		ApiKey key = new ApiKey(userName, uuid, DatatypeConverter.printBase64Binary(salt), byteArrayToHexadecimal(hashedPassword), true);
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
		List<ApiKey> apikey = this.apiKeyRepository.findByUsername(userName);
		
		if(apikey!= null && apikey.size() == 1) {
			ApiKey keyBD = apikey.get(0);
			if(keyBD.isActive() && apiScope != null && apiScope.equals(keyBD.getApiScope())) {
				MessageDigest md = null;
				byte[] hashedPassword = null;
				try {
					md = MessageDigest.getInstance("SHA-256");				
					md.update(Base64.getDecoder().decode(keyBD.getSalt().getBytes(StandardCharsets.UTF_8)));
					
					hashedPassword = md.digest(keyUuid.getBytes(StandardCharsets.UTF_8));
					
					if(keyBD.getApiScope().equals(apiScope) 
							&& keyBD.getHashedUuid().equals(byteArrayToHexadecimal(hashedPassword))) {
						retValue = true;
					} 
					
				} catch (NoSuchAlgorithmException e) {					
					e.printStackTrace();
				} 				
			}
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
		}		
	}

	@Override
	public void enableKey(String userName) {
		List<ApiKey> apikey = this.apiKeyRepository.findByUsername(userName);
		
		if(apikey != null && apikey.size() == 1) {
			ApiKey keyBD = apikey.get(0);
			
			keyBD.setActive(true);
			
			this.apiKeyRepository.save(keyBD);
		}		
	}

	@Override
	public List<ApiKey> getKeys() {
		return this.apiKeyRepository.findAll();
	}
	
	@Override
	public ApiKey getKeyByUserName(String userName) {
		return this.apiKeyRepository.findByUsername(userName).get(0);		
	}
	
	private String byteArrayToHexadecimal(byte[] bytes) {		
		StringBuilder sb = new StringBuilder();
		if(bytes != null) {
			for(int i=0; i< bytes.length ;i++){
		        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		    }
		}
		return sb.toString();
	}

}
