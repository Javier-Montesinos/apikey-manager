package com.montesinos.securedbyheadertoken.server.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.montesinos.securedbyheadertoken.server.domain.ApiKey;

public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {

	List<ApiKey> findByUsername(String username);
					
}
