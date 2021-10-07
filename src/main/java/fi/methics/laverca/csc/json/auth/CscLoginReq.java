package fi.methics.laverca.csc.json.auth;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre>
{
  "remeberMe": true
}
 * </pre>
 */
public class CscLoginReq extends GsonMessage {

    @SerializedName("refresh_token")
    public String refresh_token;
    
    @SerializedName("remeberMe")
    public boolean remeberMe;
    
    @SerializedName("clientData")
    public String clientData;
    
}
