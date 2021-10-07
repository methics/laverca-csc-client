package fi.methics.laverca.csc.json.signatures;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre>

{
  "hash": "sTOgwOm+474gFj0q0x1iSNspKqbcse4IeiqlDg/HWuI=",
  "hashAlgo": "2.16.840.1.101.3.4.2.1",
  "clientData": "12345678"
}
 * </pre>
 */
public class CscTimestampReq extends GsonMessage {


    @SerializedName("hash")
    public String hash;
    
    @SerializedName("hashAlgo")
    public String hashAlgo;

    @SerializedName("clientData")
    public String clientData;
    
}
