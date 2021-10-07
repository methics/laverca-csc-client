package fi.methics.laverca.csc.json.info;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre>
{
  "lang": "EN"
}
 * </pre>
 */
public class CscInfoReq extends GsonMessage {

    @SerializedName("lang")
    public String lang;
    
}
