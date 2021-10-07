package fi.methics.laverca.csc.json.credentials;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre>
{
  "credentialID": "GX0112348",
  "certificates": "chain",
  "certInfo": true,
  "authInfo": true
}
 * </pre>
 */
public class CscCredentialsInfoReq extends GsonMessage {

    @SerializedName("credentialID")
    public String credentialID;
    
    @SerializedName("certificates")
    public String certificates = "single"; // default value is single
    
    @SerializedName("certInfo")
    public boolean certInfo;
    
    @SerializedName("authInfo")
    public boolean authInfo;
    
}
