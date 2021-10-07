package fi.methics.laverca.csc.json.signatures;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre>
{
  "timestamp": "MGwCAQEGCSsGAQQB7U8CATAxMA0GCWCGSAFlAwQCAQUABCCrCqnrjH0VxXyQQlfnFJRx1jjrviTs7/GjKghr2AmluQIIVs5D8OUB4p4YDzIwMTQxMTE5MTEzMjM5WjADAgEBAgkAnWn2SSIWlXk="
}
 * </pre>
 */
public class CscTimestampResp extends GsonMessage {

    @SerializedName("timestamp")
    public String timestamp;
    
}
