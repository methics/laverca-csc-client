package fi.methics.laverca.csc.json.auth;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre>
{
  "token": "_TiHRG-bA-H3XlFQZ3ndFhkXf9P24/CKN69L8gdSYp5_pw",
  "token_type_hint": "refresh_token",
  "clientData": "12345678"
}
 * </pre>
 */
public class CscRevokeReq extends GsonMessage {

    @SerializedName("token")
    public String token;
 
    @SerializedName("token_type_hint")
    public String token_type_hint;
    
    @SerializedName("clientData")
    public String clientData;
    
}
