package fi.methics.laverca.csc.util;

import javax.net.ssl.X509TrustManager;

public class AllTrustingTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        
    }

    @Override 
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        
    }

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[] {};
    }
    
}
