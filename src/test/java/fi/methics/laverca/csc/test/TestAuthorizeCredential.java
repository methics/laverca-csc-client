package fi.methics.laverca.csc.test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.csc.CscClient;
import fi.methics.laverca.csc.json.credentials.CscCredentialsAuthorizeResp;
import fi.methics.laverca.csc.json.credentials.CscCredentialsListResp;

public class TestAuthorizeCredential {

    @Test
    public void testAuthorize() {
        CscClient client = new CscClient.Builder().withBaseUrl("https://14.248.94.18:9011/soap")
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername("35847001001")
                                                  .withPassword("eSIozRZGDA2P")
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscCredentialsAuthorizeResp authorize = client.authorize(credentials.credentialIDs.get(0));
        
        Assertions.assertNotNull(authorize.SAD, "SAD");
    }
    
}
