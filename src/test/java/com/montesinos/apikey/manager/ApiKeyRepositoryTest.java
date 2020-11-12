package com.montesinos.apikey.manager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.montesinos.apikey.manager.dao.ApiKeyRepository;
import com.montesinos.apikey.manager.domain.ApiKey;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ApiKeyRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private ApiKeyRepository apiKeyRepository;
        
    // write test cases here
    @Test
    @DisplayName("Test find Api Key by Username")
    public void testFindByUserName() {
        // given
    	ApiKey apikey = new ApiKey("javi", "javi-hashed", true);
    	entityManager.persist(apikey);
    	entityManager.flush();
    	
        // when
        Optional<ApiKey> returnedApikey = apiKeyRepository.findByUsername(apikey.getUsername());
     
        // then
        Assertions.assertTrue(returnedApikey.isPresent(), "Apikey was not found");
        Assertions.assertSame(returnedApikey.get(), apikey, "The apikey returned was not the same as the mock");
    }
    
    @Test
    @DisplayName("Test find Api Key enable by Username Scope ")
    public void testFindByUserNameScopeEnable() {
        // given
    	ApiKey apikey = new ApiKey("javi", "javi-hashed", true);
    	apikey.setApiScope("mi-scope");
    	
    	entityManager.persist(apikey);
    	entityManager.flush();
    	
        // when
        Optional<ApiKey> returnedApikey = apiKeyRepository.findByUsernameAndApiScopeAndActive(apikey.getUsername(), apikey.getApiScope(), true);
     
        // then
        Assertions.assertTrue(returnedApikey.isPresent(), "Apikey was not found");
        Assertions.assertSame(returnedApikey.get(), apikey, "The apikey returned was not the same as the mock");
    }
    
    @Test
    @DisplayName("Test no find Api Key disable by Username Scope ")
    public void testNoFindByUserNameScopeEnable() {
        // given
    	ApiKey apikey = new ApiKey("javi", "javi-hashed", false);
    	apikey.setApiScope("mi-scope");
    	
    	entityManager.persist(apikey);
    	entityManager.flush();
    	
        // when
        Optional<ApiKey> returnedApikey = apiKeyRepository.findByUsernameAndApiScopeAndActive(apikey.getUsername(), apikey.getApiScope(), true);
     
        // then
        Assertions.assertFalse(returnedApikey.isPresent(), "Apikey is enabled");
    }
              
}
