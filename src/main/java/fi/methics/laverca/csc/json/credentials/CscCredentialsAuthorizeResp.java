package fi.methics.laverca.csc.json.credentials;

import java.time.Instant;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre>
{
  "SAD": "1/UsHDJ98349h9fgh9348hKKHDkHWVkl/8hsAW5usc8_5="
} 
 * </pre>
 */
public class CscCredentialsAuthorizeResp extends GsonMessage {

    private Instant creationTime;
    
    @SerializedName("SAD")
    public String SAD;
    
    @SerializedName("expiresIn")
    public Integer expiresIn;
    
    public CscCredentialsAuthorizeResp() {
        this.creationTime = Instant.now();
    }
    
    /**
     * Check if this authorize response is expired
     * @return
     */
    public boolean isExpired() {
        if (this.expiresIn == null) return false; // No way to know
        return Instant.now().minusSeconds(this.expiresIn.intValue()).isAfter(creationTime);
    }
    
}
