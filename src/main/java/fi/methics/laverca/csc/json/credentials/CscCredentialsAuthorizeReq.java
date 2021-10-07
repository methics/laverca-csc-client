package fi.methics.laverca.csc.json.credentials;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre> 
{
  "credentialID": "GX0112348",
  "numSignatures": 2,
  "hash": "[sTOgwOm+474gFj0q0x1iSNspKqbcse4IeiqlDg/HWuI=,c1RPZ3dPbSs0NzRnRmowcTB4MWlTTnNwS3FiY3NlNEllaXFsRGcvSFd1ST0=]",
  "PIN": "12345678",
  "OTP": "738496",
  "clientData": "12345678"
}
 * </pre>
 */
public class CscCredentialsAuthorizeReq extends GsonMessage {

    @SerializedName("credentialID")
    public String credentialID;

    @SerializedName("numSignatures")
    public int numSignatures;
    
    @SerializedName("hash")
    public List<String> hash;

    @SerializedName("PIN")
    public String PIN;

    @SerializedName("OTP")
    public String OTP;

    @SerializedName("clientData")
    public String clientData;
    
    @SerializedName("description")
    public String description;
    
}
