package fi.methics.laverca.csc.json.info;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;


/**
 *<pre>
{
  "specs": "1.0.2.3",
  "name": "ACME Trust Services",
  "logo": "https://service.domain.org/images/logo.png",
  "region": "IT",
  "lang": "en-US",
  "authType": [
    "basic",
    "oauth2code",
    "oauth2implicit"
  ],
  "oauth2": "https://www.domain.org/",
  "methods": [
    "auth/login",
    "auth/revoke",
    "credentials/list",
    "credentials/info",
    "credentials/authorize",
    "credentials/sendOTP",
    "signatures/signHash"
  ]
}
 *</pre>
 */
public class CscInfoResp extends GsonMessage {

    @SerializedName("specs")
    public String specs;
    
    @SerializedName("name")
    public String name;

    @SerializedName("logo")
    public String logo;
    
    @SerializedName("region")
    public String region;
    
    @SerializedName("lang")
    public String lang;
    
    @SerializedName("authType")
    public List<String> authType;
    
    @SerializedName("oauth2")
    public String oauth2;
    
    @SerializedName("methods")
    public List<String> methods;
    
}
