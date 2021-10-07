package fi.methics.laverca.csc.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class AllTrustingHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
    
}
