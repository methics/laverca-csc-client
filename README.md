# laverca-csc-client
Simple Java client for CSC v1.0.4.0 API

The project supports the following CSC API end points:
- /info
- /auth/login
- /auth/revoke
- /credentials/list
- /credentials/info
- /credentials/authorize
- /signatures/signHash

# Example Usage
Get CSC service info
```java
CscClient client = new CscClient.Builder().withBaseUrl(BASE_URL)
                                          .build();
CscInfoResp info = client.getInfo();
```

List credentials
```java
CscClient client = new CscClient.Builder().withBaseUrl(BASE_URL)
                                          .withUsername(USERNAME)
                                          .withPassword(API_KEY)
                                          .build();
client.authLogin();
CscCredentialsListRes credentials = client.listCredentials();
```

Sign a hash
```java
CscClient client = new CscClient.Builder().withBaseUrl(BASE_URL)
                                          .withUsername(USERNAME)
                                          .withPassword(API_KEY)
                                          .build();
client.authLogin();
CscCredentialsListRes credentials = client.listCredentials();
CscSignHashResp          signhash = client.signHash(credentials.credentialIDs.get(0), Arrays.asList(SHA256_HASH), CscClient.RSA_WITH_SHA256);
String                  signature = signHash.signatures.get(0);
```
