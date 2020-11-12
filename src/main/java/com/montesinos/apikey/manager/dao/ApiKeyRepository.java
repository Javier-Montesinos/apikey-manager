package com.montesinos.apikey.manager.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.montesinos.apikey.manager.domain.ApiKey;

public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {

	Optional<ApiKey> findByUsername(String username);
	
	Optional<ApiKey> findByUsernameAndApiScopeAndActive(String username, String apiScope, boolean active);
	
					
}
