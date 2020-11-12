package com.montesinos.apikey.manager;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.montesinos.apikey.manager.dao.ApiKeyRepository;
import com.montesinos.apikey.manager.domain.ApiKey;
import com.montesinos.apikey.manager.rest.ApiKeyExistsException;
import com.montesinos.apikey.manager.service.ApiKeyService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiKeyServiceTest {

	@Autowired
    private ApiKeyService apiKeyService;
	
	@MockBean
    private ApiKeyRepository apiKeyRepository;

	private static ApiKey apiKey;
	private static ApiKey apiKeyByDefault;
	
	@BeforeAll
	public static void initApiKey() {
		// Setup our mock repository
    	String userName = UUID.randomUUID().toString();
    	String uuid = UUID.randomUUID().toString();
    	    	
        apiKey = new ApiKey("scope-testing", userName, uuid, true);               
	}
    
    
    @Test
    @Order(1)
    @DisplayName("Test new Key by Scope")
    public void testNewKeyByScope() {
         // Execute the service call
         ApiKey returnedApikey = apiKeyService.newKey("scope-testing");         
         
         apiKeyByDefault = returnedApikey;
         
         // Assert the response
         Assertions.assertNotNull(returnedApikey, "Api Key no returned when created");
         Assertions.assertNotNull(returnedApikey.getUsername(), "Api Key must have username");
         Assertions.assertSame(returnedApikey.getApiScope(), "scope-testing", "The Api Key returned must have the same scope as the scope mock");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test new Key by Scope, Username and UIID")
    public void testNewKeyByScopeAndUsername() {   	
        // Execute the service call
    	ApiKey returnedApikey = apiKeyService.newKey(apiKey.getApiScope(), apiKey.getUsername(), apiKey.getUuid());        
        
    	// set hash uuid for authentication
    	apiKey.setHashedUuid(returnedApikey.getHashedUuid());
    	
        // Assert the response
        Assertions.assertNotNull(returnedApikey, "Api Key no returned when created");
        Assertions.assertSame(returnedApikey.getApiScope(), apiKey.getApiScope(), "The Api Key scope returned was not the same as the mock");
        Assertions.assertSame(returnedApikey.getUsername(), apiKey.getUsername(), "The Api Key username returned was not the same as the mock");
        Assertions.assertSame(returnedApikey.getUuid(), apiKey.getUuid(), "The Api Key uuid was not the same as the mock");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test find Api Key by Username")
    public void testFindByUserName() {            
        doReturn(Optional.of(apiKey)).when(apiKeyRepository).findByUsername(apiKey.getUsername());
 
        // Execute the service call
        Optional<ApiKey> returnedApikey = Optional.ofNullable(apiKeyService.getKeyByUserName(apiKey.getUsername()));

        // Assert the response
        Assertions.assertTrue(returnedApikey.isPresent(), "Api Key was not found");
        Assertions.assertSame(returnedApikey.get(), apiKey, "The Api Key returned was not the same as the mock");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test new Key must fails if username exists")
    public void testNewKeyFailUniqueUsername() {
    	doReturn(Optional.of(apiKey)).when(apiKeyRepository).findByUsername(apiKey.getUsername());
    	    	    
    	// Assert the response
    	assertThrows(
	           ApiKeyExistsException.class,
	           () -> apiKeyService.newKey(apiKey.getApiScope(), apiKey.getUsername(), apiKey.getUuid()),
	           "Expected newKey() to throw, but it didn't"
	    );            	              
    }
    
    @Test
    @Order(50)
    @DisplayName("Test authenticate api key")
    public void testAuthenticateApiKey() {
    	// mock the repository    	
    	doReturn(Optional.of(apiKey)).when(apiKeyRepository).findByUsernameAndApiScopeAndActive(apiKey.getUsername(), apiKey.getApiScope(), true);
   	 
        // Execute the service call
        boolean auth = apiKeyService.authenticateKey(apiKey.getApiScope(), apiKey.getUsername(), apiKey.getUuid());
        
        // Assert the response        
        Assertions.assertTrue(auth, "The Api Key no is authenticated");
    }      
    
    @Test
    @Order(60)
    @DisplayName("Test disable api key")
    public void testDisableApiKey() {
    	doReturn(Optional.of(apiKey)).when(apiKeyRepository).findByUsername(apiKey.getUsername());
    	doReturn(Optional.of(apiKey)).when(apiKeyRepository).findByUsernameAndApiScopeAndActive(apiKey.getUsername(), apiKey.getApiScope(), true);
    	
    	apiKeyService.disableKey(apiKey.getUsername());    	
    	ApiKey key = apiKeyService.getKeyByUserName(apiKey.getUsername());
        
        // Assert the response        
        Assertions.assertFalse(key.isActive(), "The Api Key no is disable");
    }
    
    @Test
    @Order(70)
    @DisplayName("Test enable api key")
    public void testEnableApiKey() {
    	doReturn(Optional.of(apiKey)).when(apiKeyRepository).findByUsername(apiKey.getUsername());
    	doReturn(Optional.of(apiKey)).when(apiKeyRepository).findByUsernameAndApiScopeAndActive(apiKey.getUsername(), apiKey.getApiScope(), true);
    	
    	apiKeyService.enableKey(apiKey.getUsername());    	
    	ApiKey key = apiKeyService.getKeyByUserName(apiKey.getUsername());
        
        // Assert the response        
        Assertions.assertTrue(key.isActive(), "The Api Key no is enable");
    }
    
    @Test
    @Order(80)
    @DisplayName("Test get all api key")
    public void testGetAllKeys() {
    	// Setup our mock repository        
        doReturn(Arrays.asList(apiKey, apiKeyByDefault)).when(apiKeyRepository).findAll();

        // Execute the service call
        List<ApiKey> keys = apiKeyService.getKeys();

        // Assert the response
        Assertions.assertEquals(2, keys.size(), "findAll should return 2 keys");
    }
              
}