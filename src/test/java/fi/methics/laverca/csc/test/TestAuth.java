package fi.methics.laverca.csc.test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.csc.CscClient;
import fi.methics.laverca.csc.CscException;
import fi.methics.laverca.csc.json.auth.CscLoginResp;

public class TestAuth {

    //public static final String BASE_URL = "https://14.248.94.18:9011/soap";
    //public static final String USERNAME = "35847001001";
    //public static final String API_KEY  = "eSIozRZGDA2P";
    
    public static final String BASE_URL = "https://localhost:9011/soap";
    public static final String USERNAME = "35847001001";
    public static final String API_KEY  = "vYeEUIk8tzkh";
    
    @Test
    public void testAuthLogin() {
        CscClient client = new CscClient.Builder().withBaseUrl(BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(USERNAME)
                                                  .withPassword(API_KEY)
                                                  .build();
        CscLoginResp resp = client.authLogin();
        Assertions.assertNotNull(resp.access_token, "access_token");
    }

    @Test
    public void testRefreshToken() {
        CscClient client = new CscClient.Builder().withBaseUrl(BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(USERNAME)
                                                  .withPassword(API_KEY)
                                                  .build();
        CscLoginResp resp1 = client.authLogin();
        CscLoginResp resp2 = client.refreshLogin();
        Assertions.assertNotNull(resp1.access_token, "access_token");
        Assertions.assertNotNull(resp2.access_token, "access_token");
    }

    @Test
    public void testInvalidCredentials() {
        CscClient client = new CscClient.Builder().withBaseUrl(BASE_URL)
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername(USERNAME)
                                                  .withPassword("abc123123")
                                                  .build();
        CscException exception = Assertions.assertThrows(CscException.class, () -> {
            client.authLogin();
        });
        Assertions.assertEquals(exception.getError().error, "authentication_error");
    }

}
