package fi.methics.laverca.csc;

import java.io.IOException;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import fi.methics.laverca.csc.json.auth.CscLoginReq;
import fi.methics.laverca.csc.json.auth.CscLoginResp;
import fi.methics.laverca.csc.json.auth.CscRevokeReq;
import fi.methics.laverca.csc.json.auth.CscRevokeResp;
import fi.methics.laverca.csc.json.credentials.CscCredentialsAuthorizeReq;
import fi.methics.laverca.csc.json.credentials.CscCredentialsAuthorizeResp;
import fi.methics.laverca.csc.json.credentials.CscCredentialsInfoReq;
import fi.methics.laverca.csc.json.credentials.CscCredentialsInfoResp;
import fi.methics.laverca.csc.json.credentials.CscCredentialsListReq;
import fi.methics.laverca.csc.json.credentials.CscCredentialsListResp;
import fi.methics.laverca.csc.json.info.CscInfoReq;
import fi.methics.laverca.csc.json.info.CscInfoResp;
import fi.methics.laverca.csc.json.signatures.CscSignHashReq;
import fi.methics.laverca.csc.json.signatures.CscSignHashResp;
import fi.methics.laverca.csc.util.AllTrustingHostnameVerifier;
import fi.methics.laverca.csc.util.AllTrustingTrustManager;

public class CscClient {

    public static final String RSA_WITH_SHA1   = "1.2.840.113549.1.1.5";
    public static final String RSA_WITH_SHA224 = "1.2.840.113549.1.1.14";
    public static final String RSA_WITH_SHA256 = "1.2.840.113549.1.1.11";
    public static final String RSA_WITH_SHA384 = "1.2.840.113549.1.1.12";
    public static final String RSA_WITH_SHA512 = "1.2.840.113549.1.1.13";

    
    private String baseurl;
    private String username;
    private String password;
    
    private OkHttpClient client;
    
    private String access_token;
    private String refresh_token;
    
    protected CscClient(String baseurl, 
                        String username, 
                        String password,
                        boolean trustall) {
        this.baseurl  = baseurl;
        this.username = username;
        this.password = password;
        
        
        this.client = new OkHttpClient();
        if (trustall) {
            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, new TrustManager[] { new AllTrustingTrustManager() }, new java.security.SecureRandom());
                this.client.setSslSocketFactory(sslContext.getSocketFactory());
                this.client.setHostnameVerifier(new AllTrustingHostnameVerifier());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Login with HTTP Basic credentials
     * @return Login response
     */
    public CscLoginResp authLogin() {
        CscLoginReq req = new CscLoginReq();
        req.remeberMe = true;
        
        try {
            String url = this.baseurl+"/csc/v1/auth/login";
            System.out.println("Sending req to " + url);
            Request  request  = new Request.Builder().url(url)
                                                     .post(req.toRequestBody())
                                                     .header("Authorization", Credentials.basic(this.username, this.password))
                                                     .build();
            
            Response response = client.newCall(request).execute();
            CscLoginResp loginresp = CscLoginResp.fromResponse(response, CscLoginResp.class);
            
            this.access_token  = loginresp.access_token;
            this.refresh_token = loginresp.refresh_token;
            
            return loginresp; 
        } catch (IOException e) {
            e.printStackTrace();
            throw new CscException(e);
        } catch (CscException e) {
            throw e;
        }
    }
    
    /**
     * Use refresh_token to refresh login
     * @return Login response
     */
    public CscLoginResp refreshLogin() {
        CscLoginReq req = new CscLoginReq();
        req.remeberMe     = true;
        req.refresh_token = this.refresh_token;
        
        try {
            String url = this.baseurl+"/csc/v1/auth/login";
            System.out.println("Sending req to " + url);
            Request  request  = new Request.Builder().url(url)
                                                     .post(req.toRequestBody())
                                                     .build();
            
            Response response = client.newCall(request).execute();
            CscLoginResp loginresp = CscLoginResp.fromResponse(response, CscLoginResp.class);
            
            this.access_token  = loginresp.access_token;
            this.refresh_token = loginresp.refresh_token;
            
            return loginresp; 
        } catch (IOException e) {
            e.printStackTrace();
            throw new CscException(e);
        } catch (CscException e) {
            throw e;
        }
    }
    
    /**
     * Revoke current login
     * @return Revoke response
     */
    public CscRevokeResp authRevoke() {
        if (this.access_token == null) {
            throw CscException.createNotLoggedInException();
        }
        
        CscRevokeReq req = new CscRevokeReq();
        req.token = this.access_token;
        req.token_type_hint = "access_token";
        
        try {
            String url = this.baseurl+"/csc/v1/auth/revoke";
            System.out.println("Sending req to " + url);
            Request  request  = new Request.Builder().url(url)
                                                     .post(req.toRequestBody())
                                                     .header("Authorization", "Bearer " + this.access_token)
                                                     .build();
            
            Response response = client.newCall(request).execute();
            CscRevokeResp loginresp = CscRevokeResp.fromResponse(response, CscRevokeResp.class);
            
            this.access_token = null;
            
            return loginresp; 
        } catch (IOException e) {
            e.printStackTrace();
            throw new CscException(e);
        } catch (CscException e) {
            throw e;
        }
    }
    
    /**
     * Authorize signature
     * @param credentialid Credential ID to authorize
     * @return Authorize response
     */
    public CscCredentialsAuthorizeResp authorize(String credentialid) {
        if (this.access_token == null) {
            throw CscException.createNotLoggedInException();
        }
        CscCredentialsAuthorizeReq req = new CscCredentialsAuthorizeReq();
        req.credentialID  = credentialid;
        req.numSignatures = 1;
        
        try {
            String url = this.baseurl+"/csc/v1/credentials/authorize";
            System.out.println("Sending req to " + url);
            Request  request  = new Request.Builder().url(url)
                                                     .post(req.toRequestBody())
                                                     .header("Authorization", "Bearer " + this.access_token)
                                                     .build();
            
            Response response = client.newCall(request).execute();
            return CscCredentialsAuthorizeResp.fromResponse(response, CscCredentialsAuthorizeResp.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CscException(e);
        } catch (CscException e) {
            throw e;
        }
    }
    
    /**
     * Get details of a credential
     * @param credentialid Credential ID
     * @return Credential info JSON
     */
    public CscCredentialsInfoResp getCredentialInfo(String credentialid) {
        if (this.access_token == null) {
            throw CscException.createNotLoggedInException();
        }
        CscCredentialsInfoReq req = new CscCredentialsInfoReq();
        req.credentialID = credentialid;
        
        try {
            String url = this.baseurl+"/csc/v1/credentials/info";
            System.out.println("Sending req to " + url);
            Request  request  = new Request.Builder().url(url)
                                                     .post(req.toRequestBody())
                                                     .header("Authorization", "Bearer " + this.access_token)
                                                     .build();
            
            Response response = client.newCall(request).execute();
            return CscCredentialsInfoResp.fromResponse(response, CscCredentialsInfoResp.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CscException(e);
        } catch (CscException e) {
            throw e;
        }
    }
    
    /**
     * Get information about the target CSC server
     * <p>Calls /csc/v1/info API
     * @param lang Language which the info should be returned in (if possible)
     * @return CSC info response JSON
     * @throws CscException
     */
    public CscInfoResp getInfo(String lang) throws CscException {
        CscInfoReq req = new CscInfoReq();
        req.lang = lang;
        
        try {
            String url = this.baseurl+"/csc/v1/info";
            System.out.println("Sending req to " + url);
            Request  request  = new Request.Builder().url(url).post(req.toRequestBody()).build();
            Response response = client.newCall(request).execute();
            
            return CscInfoResp.fromResponse(response, CscInfoResp.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CscException(e);
        } catch (CscException e) {
            throw e;
        }
    }
    
    /**
     * List client credentials
     * @return Credential list JSON
     */
    public CscCredentialsListResp listCredentials() {
        if (this.access_token == null) {
            throw CscException.createNotLoggedInException();
        }
        CscCredentialsListReq req = new CscCredentialsListReq();
        req.maxResults = 20;
        
        try {
            String url = this.baseurl+"/csc/v1/credentials/list";
            System.out.println("Sending req to " + url);
            Request  request  = new Request.Builder().url(url)
                                                     .post(req.toRequestBody())
                                                     .header("Authorization", "Bearer " + this.access_token)
                                                     .build();
            
            Response response = client.newCall(request).execute();
            return CscCredentialsListResp.fromResponse(response, CscCredentialsListResp.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CscException(e);
        } catch (CscException e) {
            throw e;
        }
    }
    
    /**
     * Sign a hash
     * @param credentialid Credential ID to authorize
     * @param sad      Signature Activation Data (from authorize response)
     * @param hash     Hash to sign
     * @param signAlgo Signature Algorithm (Use e.g. {@link CscClient#RSA_WITH_SHA256})
     * @param hashAlgo Signature hash algorithm. Optional.
     * @return Authorize response
     */
    public CscSignHashResp signHash(String credentialid, String sad, String hash, String signAlgo, String hashAlgo) {
        if (this.access_token == null) {
            throw CscException.createNotLoggedInException();
        }
        CscSignHashReq req = new CscSignHashReq();
        req.credentialID = credentialid;
        req.hash         = Arrays.asList(hash);
        req.signAlgo     = signAlgo;
        req.hashAlgo     = hashAlgo;
        req.SAD          = sad;
        
        try {
            String url = this.baseurl+"/csc/v1/signatures/signHash";
            System.out.println("Sending req to " + url);
            Request  request  = new Request.Builder().url(url)
                                                     .post(req.toRequestBody())
                                                     .header("Authorization", "Bearer " + this.access_token)
                                                     .build();
            
            Response response = client.newCall(request).execute();
            return CscSignHashResp.fromResponse(response, CscSignHashResp.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CscException(e);
        } catch (CscException e) {
            throw e;
        }
    }
    
    /**
     * Sign a hash
     * @param credentialid Credential ID to authorize
     * @param sad      Signature Activation Data (from authorize response)
     * @param hash     Hash to sign
     * @param signAlgo Signature Algorithm (Use e.g. {@link CscClient#RSA_WITH_SHA256})
     * @return Authorize response
     */
    public CscSignHashResp signHash(String credentialid, String sad, String hash, String signAlgo) {
        return this.signHash(credentialid, sad, hash, signAlgo, null);
    }
    
    /**
     * Sign a SHA-256 hash
     * @param credentialid Credential ID to authorize
     * @param sad      Signature Activation Data (from authorize response)
     * @param hash     Hash to sign
     * @return Authorize response
     */
    public CscSignHashResp signHash(String credentialid, String sad, String hash) {
        return this.signHash(credentialid, sad, hash, CscClient.RSA_WITH_SHA256, null);
    }
    
    public static class Builder {

        private String baseurl;
        private String username;
        private String password;
        private boolean trustall;
        
        public CscClient build() {
            return new CscClient(this.baseurl, this.username, this.password, this.trustall);
        }
        
        public Builder withBaseUrl(String baseurl) {
            this.baseurl = baseurl;
            return this;
        }
        
        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }
        
        public Builder withTrustInsecureConnections(boolean trust) {
            this.trustall = trust;
            return this;
        }
        
        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }
        
    }
    
}
