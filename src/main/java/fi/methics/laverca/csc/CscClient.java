package fi.methics.laverca.csc;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

/**
 * CSC Client class. This is used to communicate with an RSSP.
 * This class is not thread safe as it stores variables like access_token and refresh_token.
 * 
 * <p>Usage example:
 * <pre>{@code 
 * CscClient client = new CscClient.Builder().withBaseUrl(BASE_URL)
 *                                           .withUsername(USERNAME)
 *                                           .withPassword(API_KEY)
 *                                            .build();
 * client.authLogin();
 * CscCredentialsListRes credentials = client.listCredentials();
 * CscSignHashResp          signhash = client.signHash(credentials.credentialIDs.get(0), Arrays.asList(SHA256_HASH), CscClient.RSA_WITH_SHA256);
 * String signature = signHash.signatures.get(0);
 * }</pre>
 */
public class CscClient {

    public static final String RSA_WITH_SHA1   = "1.2.840.113549.1.1.5";
    public static final String RSA_WITH_SHA224 = "1.2.840.113549.1.1.14";
    public static final String RSA_WITH_SHA256 = "1.2.840.113549.1.1.11";
    public static final String RSA_WITH_SHA384 = "1.2.840.113549.1.1.12";
    public static final String RSA_WITH_SHA512 = "1.2.840.113549.1.1.13";

    
    private String baseUrl;
    private String secondaryUrl;
    private String username;
    private String password;
    
    private OkHttpClient client;
    
    private String access_token;
    private String refresh_token;
    private CscCredentialsAuthorizeResp authorize;
    private boolean isScal2 = false;
    
    protected CscClient(String baseUrl,
                        String secondaryUrl,
                        String username, 
                        String password,
                        OkHttpClient client,
                        boolean trustall) 
    {
        this.baseUrl      = baseUrl;
        this.secondaryUrl = secondaryUrl;
        this.username     = username;
        this.password     = password;
        
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS);
    
            if (trustall) {
                try {
                    SSLContext sslContext = SSLContext.getInstance("SSL");
                    sslContext.init(null, new TrustManager[] { new AllTrustingTrustManager() }, new java.security.SecureRandom());
                    builder.sslSocketFactory(sslContext.getSocketFactory(), new AllTrustingTrustManager());
                    builder.hostnameVerifier(new AllTrustingHostnameVerifier());
                } catch (Exception e) {
                    throw new CscException(e);
                }
            }
            this.client = builder.build();
        } else {
            this.client = client;
        }
    }
    
    /**
     * Login with HTTP Basic credentials
     * @return Login response
     */
    public CscLoginResp authLogin() {
        CscLoginReq req = new CscLoginReq();
        req.rememberMe = true;
        
        String service = "/csc/v1/auth/login";
        Request.Builder request = new Request.Builder()
                .post(req.toRequestBody())
                .header("Authorization", Credentials.basic(this.username, this.password));
        
        try (Response response = sendRequest(request, service)) {
            
            CscLoginResp loginresp = CscLoginResp.fromResponse(response, CscLoginResp.class);
            
            this.access_token  = loginresp.access_token;
            this.refresh_token = loginresp.refresh_token;
            
            return loginresp; 
        } catch (IOException e) {
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
        req.rememberMe    = true;
        req.refresh_token = this.refresh_token;
        
        String service = "/csc/v1/auth/login";
        Request.Builder request = new Request.Builder()
                .post(req.toRequestBody());
        
        try (Response response = sendRequest(request, service)){
            
            CscLoginResp loginresp = CscLoginResp.fromResponse(response, CscLoginResp.class);
            
            this.access_token  = loginresp.access_token;
            this.refresh_token = loginresp.refresh_token;
            
            return loginresp; 
        } catch (IOException e) {
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
        
        String service = "/csc/v1/auth/revoke";
        Request.Builder request = new Request.Builder()
                .post(req.toRequestBody())
                .header("Authorization", "Bearer " + this.access_token);
        
        try (Response response = this.sendRequest(request, service)) {

            CscRevokeResp loginresp = CscRevokeResp.fromResponse(response, CscRevokeResp.class);
            
            this.access_token = null;
            
            return loginresp; 
        } catch (IOException e) {
            throw new CscException(e);
        } catch (CscException e) {
            throw e;
        }
    }
    
    /**
     * Explicitly authorize signature in SCAL1 mode.
     * 
     * <p>This can optionally called before {@link #signHash(String, List, String)}.
     * If no valid authorize response is found, signHash automatically calls this again.
     * 
     * @param credentialid Credential ID to authorize
     * @return Authorize response
     */
    public CscCredentialsAuthorizeResp authorize(String credentialid) {
        if (this.isScal2) {
            throw CscException.createMissingParamException("hash");
        }
        return this.authorize(credentialid, null);
    }
    
    /**
     * Explicitly authorize signature in SCAL2 mode.
     * 
     * <p>This can optionally called before {@link #signHash(String, List, String)}.
     * If no valid authorize response is found, signHash automatically calls this again.
     * 
     * @param credentialid Credential ID to authorize
     * @return Authorize response
     */
    public CscCredentialsAuthorizeResp authorize(String credentialid, List<String> hash) {
        if (this.access_token == null) {
            throw CscException.createNotLoggedInException();
        }
        CscCredentialsAuthorizeReq req = new CscCredentialsAuthorizeReq();
        req.credentialID  = credentialid;
        req.numSignatures = hash != null ? hash.size() : 1;
        req.hash          = hash;
        
        String service = "/csc/v1/credentials/authorize";
        Request.Builder request = new Request.Builder()
                .post(req.toRequestBody())
                .header("Authorization", "Bearer " + this.access_token);
        
        try (Response response = sendRequest(request, service)){

            CscCredentialsAuthorizeResp authorize = CscCredentialsAuthorizeResp.fromResponse(response, CscCredentialsAuthorizeResp.class);
            
            this.authorize = authorize;
            
            return authorize;
        } catch (IOException e) {
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
        
        String service = "/csc/v1/credentials/info";
        Request.Builder request = new Request.Builder()
                .post(req.toRequestBody())
                .header("Authorization", "Bearer " + this.access_token);
        
        try (Response response = sendRequest(request, service)){

            CscCredentialsInfoResp info = CscCredentialsInfoResp.fromResponse(response, CscCredentialsInfoResp.class);
            if ("2".equals(info.SCAL)) {
                this.isScal2 = true;
            }
            return info;
        } catch (IOException e) {
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
        
        String service = "/csc/v1/info";
        Request.Builder  request  = new Request.Builder().post(req.toRequestBody());
        
        try (Response response = sendRequest(request, service)){

            return CscInfoResp.fromResponse(response, CscInfoResp.class);
        } catch (IOException e) {
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
        
        String service = "/csc/v1/credentials/list";
        Request.Builder request = new Request.Builder()
                .post(req.toRequestBody())
                .header("Authorization", "Bearer " + this.access_token);
        
        
        try (Response response = sendRequest(request, service)){

            return CscCredentialsListResp.fromResponse(response, CscCredentialsListResp.class);
        } catch (CscException e) {
            throw e;
        } catch (Exception e) {
            throw new CscException(e);
        }
    }
    
    /**
     * Sign a list of hashes
     * 
     * @param credentialid Credential ID to authorize
     * @param authorize Authorize response with Signature Activation Data
     * @param hash      Hashes to sign
     * @param signAlgo  Signature Algorithm (Use e.g. {@link CscClient#RSA_WITH_SHA256})
     * @param hashAlgo  Signature hash algorithm. Optional.
     * @return Authorize response
     */
    public CscSignHashResp signHash(String credentialid, CscCredentialsAuthorizeResp authorize, List<String> hash, String signAlgo, String hashAlgo) {
        if (this.access_token == null) {
            throw CscException.createNotLoggedInException();
        }
        CscSignHashReq req = new CscSignHashReq();
        req.credentialID = credentialid;
        req.hash         = hash;
        req.signAlgo     = signAlgo;
        req.hashAlgo     = hashAlgo;
        req.SAD          = authorize.SAD;
        
        String service = "/csc/v1/signatures/signHash";
        Request.Builder request = new Request.Builder()
                .post(req.toRequestBody())
                .header("Authorization", "Bearer " + this.access_token);
        
        try (Response response = sendRequest(request, service)){

            return CscSignHashResp.fromResponse(response, CscSignHashResp.class);
        } catch (IOException e) {
            throw new CscException(e);
        } catch (CscException e) {
            throw e;
        }
    }

    /**
     * Sign a list of hashes
     * 
     * @param credentialid Credential ID to authorize
     * @param authorize Authorize response with Signature Activation Data
     * @param hash     Hash to sign
     * @param signAlgo Signature Algorithm (Use e.g. {@link CscClient#RSA_WITH_SHA256})
     * @param hashAlgo  Signature hash algorithm. Optional.
     * @return Authorize response
     */
    public CscSignHashResp signHash(String credentialid, List<String> hash, String signAlgo, String hashAlgo) {
        if (this.authorize == null || this.authorize.isExpired()) {
            this.authorize(credentialid, hash);
        }
        return this.signHash(credentialid, this.authorize, hash, signAlgo, hashAlgo);
    }
    
    /**
     * Sign a list of hashes
     * 
     * @param credentialid Credential ID to authorize
     * @param authorize Authorize response with Signature Activation Data
     * @param hash     Hash to sign
     * @param signAlgo Signature Algorithm (Use e.g. {@link CscClient#RSA_WITH_SHA256})
     * @return Authorize response
     */
    public CscSignHashResp signHash(String credentialid, List<String> hash, String signAlgo) {
        return this.signHash(credentialid, hash, signAlgo, null);
    }
    
    /**
     * Send a request to primary URL.
     * If the request fails, and secondary is defined, retry to secondary URL.
     * @param reqBuilder Request Builder
     * @param service    Service part of the URL (e.g. /csc/v1/info)
     * @return
     * @throws IOException
     */
    private Response sendRequest(Request.Builder reqBuilder, String service) throws IOException {
        
        try {
            Request request   = reqBuilder.url(this.baseUrl+service).build();
            Response response = this.client.newCall(request).execute();
            return response;
        } catch (IOException e) {
            if (this.secondaryUrl != null) {
                Request request   = reqBuilder.url(this.secondaryUrl+service).build();
                Response response = this.client.newCall(request).execute();
                return response;
            }
            throw e;
        }
    }
    
    /**
     * Builder for {@link CscClient}
     */
    public static class Builder {

        private String baseUrl;
        private String secondaryUrl;
        private String username;
        private String password;
        private boolean trustall;
        
        private OkHttpClient client;
        
        /**
         * Build a new {@link CscClient}
         * @return CSC client
         * @throws CscException if client building fails (e.g. TLS init issues)
         */
        public CscClient build() throws CscException {
            return new CscClient(this.baseUrl, this.secondaryUrl, this.username, this.password, this.client, this.trustall);
        }
        
        /**
         * Set a primary CSC URL
         * @param baseUrl
         * @return this builder
         */
        public Builder withBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }
        
        /**
         * Set a secondary CSC URL (used if primary fails)
         * @param secondaryUrl
         * @return this builder
         */
        public Builder withSecondaryUrl(String secondaryUrl) {
            this.secondaryUrl = secondaryUrl;
            return this;
        }
        
        /**
         * Set the CSC Password
         * @param password
         * @return this builder
         */
        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }
        
        /**
         * Trust unknown and self-signed server certificates.
         * @param trust
         * @return this builder
         */
        public Builder withTrustInsecureConnections(boolean trust) {
            this.trustall = trust;
            return this;
        }
        
        /**
         * Set the CSC Username
         * @param username
         * @return this builder
         */
        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }
        
        /**
         * Set OkHTTP client
         * @param client
         * @return this builder
         */
        public Builder withClient(OkHttpClient client) {
            this.client = client;
            return this;
        }
        
    }
    
    
}
