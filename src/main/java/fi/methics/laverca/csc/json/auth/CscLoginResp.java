package fi.methics.laverca.csc.json.auth;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre>
{
  "access_token": "4/CKN69L8gdSYp5_pwH3XlFQZ3ndFhkXf9P2_TiHRG-bA",
  "refresh_token": "_TiHRG-bA-H3XlFQZ3ndFhkXf9P24/CKN69L8gdSYp5_pw",
  "expires_in": 3600
}
 * </pre>
 */
public class CscLoginResp extends GsonMessage {

    @SerializedName("access_token")
    public String access_token;
 
    @SerializedName("refresh_token")
    public String refresh_token;
    
    @SerializedName("expires_in")
    public int expires_in;
    
}
