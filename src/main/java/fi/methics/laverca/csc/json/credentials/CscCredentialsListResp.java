package fi.methics.laverca.csc.json.credentials;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre> 
{
  "credentialIDs": [
    "GX0112348",
    "HX0224685"
  ]
}
 * </pre>
 */
public class CscCredentialsListResp extends GsonMessage {

    @SerializedName("credentialIDs")
    public List<String> credentialIDs;
    
    @SerializedName("nextPageToken")
    public String nextPageToken;
    
}
