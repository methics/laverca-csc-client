package fi.methics.laverca.csc.test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.csc.CscClient;
import fi.methics.laverca.csc.json.credentials.CscCredentialsInfoResp;
import fi.methics.laverca.csc.json.credentials.CscCredentialsListResp;

public class TestGetCredentialInfo {

    @Test
    public void testGetCredentialInfo() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp credentials    = client.listCredentials();
        System.out.println("/credentials/list: " + credentials);
        CscCredentialsInfoResp credentialInfo = client.getCredentialInfo(credentials.credentialIDs.get(0));
        Assertions.assertNotNull(credentialInfo.cert, "cert");
    }
    
}
