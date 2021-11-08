package fi.methics.laverca.csc.test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.csc.CscClient;
import fi.methics.laverca.csc.CscException;
import fi.methics.laverca.csc.json.credentials.CscCredentialsListResp;

public class TestListCredentials {

    @Test
    public void testListCredentials() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp resp = client.listCredentials();
        Assertions.assertNotNull(resp.credentialIDs, "credentialIDs");
        Assertions.assertTrue(resp.credentialIDs.size() > 0);
    }
    
    @Test
    public void testListCredentialsWithoutLoggingIn() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .build();
        CscException exception = Assertions.assertThrows(CscException.class, () -> {
            client.listCredentials();
        });
        Assertions.assertEquals(exception.getError().error, "client_error");
    }
    
    
}
