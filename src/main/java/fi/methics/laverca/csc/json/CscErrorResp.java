package fi.methics.laverca.csc.json;

import java.io.IOException;

import com.google.gson.annotations.SerializedName;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
        try (ResponseBody body = response.body()) {
            String json = body.string();
            return fromJson(json, CscErrorResp.class);
        }
    }
    
}