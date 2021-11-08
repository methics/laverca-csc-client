package fi.methics.laverca.csc.test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.csc.CscClient;
import fi.methics.laverca.csc.json.credentials.CscCredentialsAuthorizeResp;
import fi.methics.laverca.csc.json.credentials.CscCredentialsListResp;

public class TestAuthorizeCredential {

    @Test
    public void testAuthorize() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscCredentialsAuthorizeResp authorize = client.authorize(credentials.credentialIDs.get(0));
        
        Assertions.assertNotNull(authorize.SAD, "SAD");
    }
    
}
