package com.montesinos.securedbyheadertoken.server;

import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.montesinos.securedbyheadertoken.server.dao.ApiKeyRepository;
import com.montesinos.securedbyheadertoken.server.domain.ApiKey;
import com.montesinos.securedbyheadertoken.server.service.ApiKeyService;

@SpringBootTest
public class ApiKeyServiceTest {

	@Autowired
    private ApiKeyService apiKeyService;
	
	@MockBean
    private ApiKeyRepository apiKeyRepository;
	
    @Test
    @DisplayName("Test find Api Key by Username")
    public void testFindByUserName() {
    	// Setup our mock repository
    	String name = "alex";
        ApiKey apikey = new ApiKey(name, "123", true);
        
        doReturn(Optional.of(apikey)).when(apiKeyRepository).findByUsername(name);
 
        // Execute the service call
        Optional<ApiKey> returnedApikey = Optional.ofNullable(apiKeyService.getKeyByUserName(name));

        // Assert the response
        Assertions.assertTrue(returnedApikey.isPresent(), "Api Key was not found");
        Assertions.assertSame(returnedApikey.get(), apikey, "The Api Key returned was not the same as the mock");
        

        
    }
       
}