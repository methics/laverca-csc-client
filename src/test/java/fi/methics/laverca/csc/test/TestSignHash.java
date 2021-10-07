package fi.methics.laverca.csc.test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.csc.CscClient;
import fi.methics.laverca.csc.json.credentials.CscCredentialsAuthorizeResp;
import fi.methics.laverca.csc.json.credentials.CscCredentialsListResp;
import fi.methics.laverca.csc.json.signatures.CscSignHashResp;

public class TestSignHash {

    @Test
    public void testSignHash() {
        CscClient client = new CscClient.Builder().withBaseUrl("https://14.248.94.18:9011/soap")
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername("35847001001")
                                                  .withPassword("eSIozRZGDA2P")
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscCredentialsAuthorizeResp authorize = client.authorize(credentials.credentialIDs.get(0));
        CscSignHashResp              signhash = client.signHash(credentials.credentialIDs.get(0), authorize.SAD, "yT8dSBW55jBPpLP4qc72IQg1h8YWX8fKmsAx6B84Y4w=");
        
        Assertions.assertNotNull(signhash.signatures,       "signatures");
        Assertions.assertNotNull(signhash.signatures.get(0), "signature");
    }
    
}
