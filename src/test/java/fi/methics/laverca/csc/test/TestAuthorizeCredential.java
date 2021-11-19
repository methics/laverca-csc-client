package fi.methics.laverca.csc.test;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.csc.CscClient;
import fi.methics.laverca.csc.json.credentials.CscCredentialsAuthorizeResp;
import fi.methics.laverca.csc.json.credentials.CscCredentialsInfoResp;
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
        CscCredentialsListResp credentials = client.listCredentials();
        Assertions.assertTrue(credentials.credentialIDs.size() > 0, "At least one credential was returned");
        
        CscCredentialsInfoResp info = client.getCredentialInfo(credentials.credentialIDs.get(0));
        CscCredentialsAuthorizeResp authorize;
        if (info.isScal2()) {
            authorize = client.authorize(credentials.credentialIDs.get(0), Arrays.asList("DUMMY_HASH"));
        } else {
            authorize = client.authorize(credentials.credentialIDs.get(0));
        }
        
        Assertions.assertNotNull(authorize.SAD, "SAD");
    }
    
}
