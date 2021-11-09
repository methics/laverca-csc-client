package fi.methics.laverca.csc.test;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.csc.CscClient;
import fi.methics.laverca.csc.CscException;
import fi.methics.laverca.csc.json.credentials.CscCredentialsAuthorizeResp;
import fi.methics.laverca.csc.json.credentials.CscCredentialsInfoResp;
import fi.methics.laverca.csc.json.credentials.CscCredentialsListResp;
import fi.methics.laverca.csc.json.signatures.CscSignHashResp;

public class TestSignHash {

    public static final List<String> SHA_1_HASH   = Arrays.asList("6SRQGxqJ372qx+kExRGCnXx8j08=");
    public static final List<String> SHA_256_HASH = Arrays.asList("yT8dSBW55jBPpLP4qc72IQg1h8YWX8fKmsAx6B84Y4w=");
    public static final List<String> SHA_384_HASH = Arrays.asList("YS/z/ZRgAExEwv4RIrytordiMWxI5Pzn+6MHOh5pHdo+Y5cllkTD1sbCrPLzXP8F");
    public static final List<String> SHA_512_HASH = Arrays.asList("mQTi7vuAYupmdN3krOePaZR3FRbJN0KAnbUdTDiqa4bCPejblNKr9xZQwmClbAXl3jhFcUEkBkeT4flalmwYvw==");
    
    public static final List<String> MULTI_SHA_256_HASH = Arrays.asList("yT8dSBW55jBPpLP4qc72IQg1h8YWX8fKmsAx6B84Y4w=", "rNgt8E7HUwnm9NAZHXnTYrZbhUTE6z7r8bXFEMxtZeQ=");
    public static final List<String> WRONG_SHA_256_HASH = Arrays.asList("oHGkSKNF/kVs5I7gZbg2cDVLzjrVUGUUmXyLw8KANnM=", "rNgt8E7HUwnm9NAZHXnTYrZbhUTE6z7r8bXFEMxtZeQ=");

    /**
     * Test signing a single hash with explicit authorize call
     */
    @Test
    public void testSignHash() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscCredentialsInfoResp    info        = client.getCredentialInfo(credentials.credentialIDs.get(0));
        CscCredentialsAuthorizeResp authorize = null;
        if (info.isScal2()) {
            authorize = client.authorize(credentials.credentialIDs.get(0), SHA_256_HASH);
        } else {
            authorize = client.authorize(credentials.credentialIDs.get(0));
        }
        
        CscSignHashResp signhash = client.signHash(credentials.credentialIDs.get(0), authorize, SHA_256_HASH, CscClient.RSA_WITH_SHA256, null);
        
        Assertions.assertNotNull(signhash.signatures,       "signatures");
        Assertions.assertNotNull(signhash.signatures.get(0), "signature");
    }
    
    /**
     * Test signing multiple SHA-256 hashes with explicit authorize call
     */
    @Test
    public void testSignMultipleHash() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscCredentialsAuthorizeResp authorize = client.authorize(credentials.credentialIDs.get(0), MULTI_SHA_256_HASH);
        
        CscSignHashResp signhash = client.signHash(credentials.credentialIDs.get(0), authorize, MULTI_SHA_256_HASH, CscClient.RSA_WITH_SHA256, null);
        
        Assertions.assertNotNull(signhash.signatures,        "signatures");
        Assertions.assertNotNull(signhash.signatures.get(0), "signature1");
        Assertions.assertNotNull(signhash.signatures.get(1), "signature2");
    }
    
    /**
     * Test signing a single hash with explicit authorize call
     */
    @Test
    public void testSCAL2InvalidHash() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscCredentialsInfoResp    info        = client.getCredentialInfo(credentials.credentialIDs.get(0));
        if (!info.isScal2()) {
            System.out.println("Credential is not SCAL2. Ignoring.");
            return;
        }
        CscCredentialsAuthorizeResp authorize = client.authorize(credentials.credentialIDs.get(0), MULTI_SHA_256_HASH);
        
        CscException exception = Assertions.assertThrows(CscException.class, () -> {
            client.signHash(credentials.credentialIDs.get(0), authorize, WRONG_SHA_256_HASH, CscClient.RSA_WITH_SHA256, null);
        });
        Assertions.assertEquals(exception.getError().error, "invalid_request");
    }
    
    /**
     * Test signing a single SHA1 hash
     */
    @Test
    public void testSignSHA1Hash() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscSignHashResp              signhash = client.signHash(credentials.credentialIDs.get(0), SHA_1_HASH, CscClient.RSA_WITH_SHA1);
        
        Assertions.assertNotNull(signhash.signatures,       "signatures");
        Assertions.assertNotNull(signhash.signatures.get(0), "signature");
    }
    
    /**
     * Test signing a single SHA256 hash
     */
    @Test
    public void testSignSHA256Hash() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscSignHashResp              signhash = client.signHash(credentials.credentialIDs.get(0), SHA_256_HASH, CscClient.RSA_WITH_SHA256);
        
        Assertions.assertNotNull(signhash.signatures,       "signatures");
        Assertions.assertNotNull(signhash.signatures.get(0), "signature");
    }
    
    /**
     * Test signing a single SHA384 hash
     */
    @Test
    public void testSignSHA384Hash() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscSignHashResp              signhash = client.signHash(credentials.credentialIDs.get(0), SHA_384_HASH, CscClient.RSA_WITH_SHA384);
        
        Assertions.assertNotNull(signhash.signatures,       "signatures");
        Assertions.assertNotNull(signhash.signatures.get(0), "signature");
    }
    
    /**
     * Test signing a single SHA512 hash
     */
    @Test
    public void testSignSHA512Hash() {
        CscClient client = new CscClient.Builder().withBaseUrl(TestAuth.BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(TestAuth.USERNAME)
                                                  .withPassword(TestAuth.API_KEY)
                                                  .build();
        client.authLogin();
        CscCredentialsListResp    credentials = client.listCredentials();
        CscSignHashResp              signhash = client.signHash(credentials.credentialIDs.get(0), SHA_512_HASH, CscClient.RSA_WITH_SHA512);
        
        Assertions.assertNotNull(signhash.signatures,       "signatures");
        Assertions.assertNotNull(signhash.signatures.get(0), "signature");
    }
    
}
