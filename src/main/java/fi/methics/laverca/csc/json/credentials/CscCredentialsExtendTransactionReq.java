package fi.methics.laverca.csc.json.credentials;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * 
{
  "credentialID": "GX0112348",
  "hash": "[WlTTnNwS3FiY3NlNEllaXFsRGcvSFd1ST0=]",
  "SAD": "_TiHRG-bAH3XlFQZ3ndFhkXf9P24/CKN69L8gdSYp5_pw",
  "clientData": "12345678"
}
 *
 */
public class CscCredentialsExtendTransactionReq extends GsonMessage {


    @SerializedName("credentialID")
    public String credentialID;
    

    @SerializedName("hash")
    public String hash;
    

    @SerializedName("SAD")
    public String SAD;
    

    @SerializedName("clientData")
    public String clientData;
    
}
