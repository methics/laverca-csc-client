package fi.methics.laverca.csc.test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.csc.CscClient;
import fi.methics.laverca.csc.json.auth.CscLoginResp;

public class TestAuth {

    @Test
    public void testAuthLogin() {
        CscClient client = new CscClient.Builder().withBaseUrl("https://14.248.94.18:9011/soap")
                                                  .withTrustInsecureConnections(true)
                                                  .withUsername("35847001001")
                                                  .withPassword("eSIozRZGDA2P")
                                                  .build();
        CscLoginResp resp = client.authLogin();
        Assertions.assertNotNull(resp.access_token, "access_token");
    }
    
}
