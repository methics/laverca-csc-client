package fi.methics.laverca.csc.json.credentials;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre>
{
  "SAD": "1/UsHDJ98349h9fgh9348hKKHDkHWVkl/8hsAW5usc8_5="
} 
 * </pre>
 */
public class CscCredentialsExtendTransactionResp extends GsonMessage {

    @SerializedName("SAD")
    public String SAD;
    
    
}
