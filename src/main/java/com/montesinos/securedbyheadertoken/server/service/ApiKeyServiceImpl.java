package com.montesinos.securedbyheadertoken.server.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.montesinos.securedbyheadertoken.server.domain.ApiKey;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {

	private final static Logger LOG = LoggerFactory.getLogger(ApiKeyServiceImpl.class);

	private Map<String, ApiKey> keys;
	
	@Value("${apikey.test.enable}")
	private boolean apiKeyTestEnable;
	
	@Value("${apikey.test.username}")
	private String apiKeyTestUsername;
	
	@Value("${apikey.test.uuid}")
	private String apiKeyTestUuid;
	
	@Value("${apikey.test.apiscope}")
	private String apiKeyTestApiScope;
	
	@PostConstruct
	private void loadData() {
		keys = new HashMap<String, ApiKey>();
		
		if(apiKeyTestEnable) {
			newKey(this.apiKeyTestApiScope, this.apiKeyTestUsername, this.apiKeyTestUuid);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		ApiKey key = new ApiKey(userName, uuid, DatatypeConverter.printBase64Binary(salt), byteArrayToHexadecimal(hashedPassword), true);
		LOG.info("Key generated: {}" + key.toString());
		
		key.setUuid(null);
		key.setApiScope(apiScope);
		this.keys.put(userName, key);
	
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
		if(this.keys.containsKey(userName)) {
			ApiKey keyBD = this.keys.get(userName);

			MessageDigest md = null;
			byte[] hashedPassword = null;
			try {
				md = MessageDigest.getInstance("SHA-256");				
				md.update(Base64.getDecoder().decode(keyBD.getSalt().getBytes("UTF-8")));
				
				hashedPassword = md.digest(keyUuid.getBytes(StandardCharsets.UTF_8));
				
				if(keyBD.getApiScope().equals(apiScope) 
						&& keyBD.getHashedUuid().equals(byteArrayToHexadecimal(hashedPassword))) {
					retValue = true;
				} 
				
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return retValue;
	}

	@Override
	public void revokeKey(String userName) {
		if(this.keys.containsKey(userName)) {
			ApiKey keyBD = this.keys.get(userName);
			
			keyBD.setActive(false);
			
			this.keys.replace(userName, keyBD);
		}		
	}

	@Override
	public List<ApiKey> getKeys() {
		return new ArrayList<ApiKey>(this.keys.values());
	}
	
	@Override
	public ApiKey getKeyByUserName(String userName) {
		return this.keys.get(userName);		
	}
	
	private String byteArrayToHexadecimal(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i< bytes.length ;i++){
	        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
		return sb.toString();
	}

}
