package fi.methics.laverca.csc.json;

import java.io.IOException;

import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.Response;

/**
 * <pre>
{
  "error": "invalid_request",
  "error_description": "That thing there was not right"
}
 * </pre>
 */
public class CscErrorResp extends GsonMessage {

    @SerializedName("error")
    public String error;
    
    @SerializedName("error_description")
    public String error_description;
    
    public static CscErrorResp fromResponse(final Response response) throws IOException {
        if (response == null) return null;
        String json = response.body().string();
        return fromJson(json, CscErrorResp.class);
    }
    
}