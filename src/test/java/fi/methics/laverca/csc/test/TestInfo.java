package fi.methics.laverca.csc.test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.csc.CscClient;
import fi.methics.laverca.csc.json.info.CscInfoResp;

public class TestInfo {

    @Test
    public void testInfo() {
        CscClient client = new CscClient.Builder().withBaseUrl("https://14.248.94.18:9011/soap").withTrustInsecureConnections(true).build();
        CscInfoResp resp = client.getInfo("EN");
        Assertions.assertNotNull(resp.name, "name");
    }
    
}
