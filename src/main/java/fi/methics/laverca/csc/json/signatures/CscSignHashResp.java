package fi.methics.laverca.csc.json.signatures;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre>
{
  "signatures": [
    "KedJuTob5gtvYx9qM3k3gm7kbLBwVbEQRl26S2tmXjqNND7MRGtoew==",
    "Idhef7xzgtvYx9qM3k3gm7kbLBwVbE98239S2tm8hUh85KKsfdowel=="
  ]
}
 * </pre>
 */
public class CscSignHashResp extends GsonMessage {


    @SerializedName("signatures")
    public List<String> signatures;
    
}
