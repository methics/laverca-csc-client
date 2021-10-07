package fi.methics.laverca.csc.json.credentials;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre>
{
  "maxResults": "10"
} 
 * </pre>
 */
public class CscCredentialsListReq extends GsonMessage {

    @SerializedName("maxResults")
    public int maxResults;

    @SerializedName("pageToken")
    public String pageToken;
    
    public int getMaxResults() {
        return this.maxResults;
    }
    
    public int getPageToken() {
        if (pageToken == null) return 0;
        try {
            return Integer.valueOf(pageToken);
        } catch (Exception e) {
            return 0;
        }
    }
    
}
