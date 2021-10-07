package fi.methics.laverca.csc.json.credentials;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre> 
{
  "credentialID": "GX0112348",
  "clientData": "12345678"
}
 * </pre>
 */
public class CscCredentialsSendOtpReq extends GsonMessage {

    @SerializedName("credentialID")
    public String credentialID;

    @SerializedName("clientData")
    public String clientData;
}
