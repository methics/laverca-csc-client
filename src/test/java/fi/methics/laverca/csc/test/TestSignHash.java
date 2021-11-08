package fi.methics.laverca.csc.test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.csc.CscClient;
import fi.methics.laverca.csc.json.credentials.CscCredentialsAuthorizeResp;
import fi.methics.laverca.csc.json.credentials.CscCredentialsListResp;
import fi.methics.laverca.csc.json.signatures.CscSignHashResp;

public class TestSignHash {

    public static final String SHA_1_HASH   = "6SRQGxqJ372qx+kExRGCnXx8j08=";
    public static final String SHA_256_HASH = "yT8dSBW55jBPpLP4qc72IQg1h8YWX8fKmsAx6B84Y4w=";
    public static final String SHA_384_HASH = "YS/z/ZRgAExEwv4RIrytordiMWxI5Pzn+6MHOh5pHdo+Y5cllkTD1sbCrPLzXP8F";
    public static final String SHA_512_HASH = "mQTi7vuAYupmdN3krOePaZR3FRbJN0KAnbUdTDiqa4bCPejblNKr9xZQwmClbAXl3jhFcUEkBkeT4flalmwYvw==";
    
    @Test
    public void testSignHash() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscCredentialsAuthorizeResp authorize = client.authorize(credentials.credentialIDs.get(0));
        CscSignHashResp              signhash = client.signHash(credentials.credentialIDs.get(0), authorize.SAD, SHA_256_HASH);
        
        Assertions.assertNotNull(signhash.signatures,       "signatures");
        Assertions.assertNotNull(signhash.signatures.get(0), "signature");
    }
    
    
    @Test
    public void testSignSHA1Hash() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscCredentialsAuthorizeResp authorize = client.authorize(credentials.credentialIDs.get(0));
        CscSignHashResp              signhash = client.signHash(credentials.credentialIDs.get(0), authorize.SAD, SHA_1_HASH, CscClient.RSA_WITH_SHA1);
        
        Assertions.assertNotNull(signhash.signatures,       "signatures");
        Assertions.assertNotNull(signhash.signatures.get(0), "signature");
    }
    
    
    @Test
    public void testSignSHA256Hash() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscCredentialsAuthorizeResp authorize = client.authorize(credentials.credentialIDs.get(0));
        CscSignHashResp              signhash = client.signHash(credentials.credentialIDs.get(0), authorize.SAD, SHA_256_HASH, CscClient.RSA_WITH_SHA256);
        
        Assertions.assertNotNull(signhash.signatures,       "signatures");
        Assertions.assertNotNull(signhash.signatures.get(0), "signature");
    }
    
    
    @Test
    public void testSignSHA384Hash() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscCredentialsAuthorizeResp authorize = client.authorize(credentials.credentialIDs.get(0));
        CscSignHashResp              signhash = client.signHash(credentials.credentialIDs.get(0), authorize.SAD, SHA_384_HASH, CscClient.RSA_WITH_SHA384);
        
        Assertions.assertNotNull(signhash.signatures,       "signatures");
        Assertions.assertNotNull(signhash.signatures.get(0), "signature");
    }
    
    @Test
    public void testSignSHA512Hash() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscCredentialsAuthorizeResp authorize = client.authorize(credentials.credentialIDs.get(0));
        CscSignHashResp              signhash = client.signHash(credentials.credentialIDs.get(0), authorize.SAD, SHA_512_HASH, CscClient.RSA_WITH_SHA512);
        
        Assertions.assertNotNull(signhash.signatures,       "signatures");
        Assertions.assertNotNull(signhash.signatures.get(0), "signature");
    }
    
}
