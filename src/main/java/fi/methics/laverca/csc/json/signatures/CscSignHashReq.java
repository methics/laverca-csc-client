package fi.methics.laverca.csc.json.signatures;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre> 
{
  "SAD": "_TiHRG-bAH3XlFQZ3ndFhkXf9P24/CKN69L8gdSYp5_pw",
  "hash": [
    "sTOgwOm+474gFj0q0x1iSNspKqbcse4IeiqlDg/HWuI=",
    "c1RPZ3dPbSs0NzRnRmowcTB4MWlTTnNwS3FiY3NlNEllaXFsRGcvSFd1ST0="
  ],
  "hashAlgo": "2.16.840.1.101.3.4.2.1",
  "signAlgo": "1.2.840.113549.1.1.1",
  "clientData": "12345678"
}
 * </pre>
 */
public class CscSignHashReq extends GsonMessage {

    @SerializedName("credentialID")
    public String credentialID;
    
    @SerializedName("SAD")
    public String SAD;
    
    @SerializedName("hash")
    public List<String> hash;

    @SerializedName("hashAlgo")
    public String hashAlgo;
    
    @SerializedName("signAlgo")
    public String signAlgo;
    
    @SerializedName("signAlgoParams")
    public String signAlgoParams;
    
    @SerializedName("clientData")
    public String clientData;
    
    /**
     * Parse DTBD from the request ClientData
     * @return DTBD or null if not present
     */
    public String getDtbd() {
        ClientData data = this.parseClientData();
        if (data == null) {
            return null;
        }
        return data.dtbd;
    }
    
    /**
     * Parse language from the request ClientData
     * @return language or null if not present
     */
    public String getLanguage() {
        ClientData data = this.parseClientData();
        if (data == null) {
            return null;
        }
        return data.language;
    }
    
    private ClientData parseClientData() {
        if (this.clientData == null) return null;
        return ClientData.fromBase64(clientData, ClientData.class);
    }
    
    public static class ClientData extends GsonMessage {
        @SerializedName("language")
        public String language;
        
        @SerializedName("dtbd")
        public String dtbd;
    }
    
}
