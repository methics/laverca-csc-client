package fi.methics.laverca.csc.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import fi.methics.laverca.csc.CscException;


/**
 * Generic GSON message object.
 * Obfuscation is not done to any classes extending this.
 */
public abstract class GsonMessage {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping()
                                                     .setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                                                     .create();

    /**
     * Parse a JSON String to given type
     * @param json JSON String
     * @param t    Output class
     * @return Resulting object
     * @throws JsonSyntaxException if JSON cannot be parsed to given type
     * @see {@link Gson#fromJson(String, Class)}
     */
    public static <T> T fromJson(final String json, final Class<T> t) {
        return GSON.fromJson(json, t);
    }
    
    /**
     * Parse a JSON String to given type
     * @param json JSON String
     * @param t    Output class
     * @return Resulting object
     * @throws JsonSyntaxException if JSON cannot be parsed to given type
     * @see {@link Gson#fromJson(String, Class)}
     */
    public static <T> T fromBase64(final String json, final Class<T> t) {
        return GSON.fromJson(new String(Base64.getDecoder().decode(json), StandardCharsets.UTF_8), t);
    }

    /**
     * Parse a JSON byte[] to given type
     * @param bytes JSON byte[]
     * @param t     Output class
     * @return Resulting object
     * @throws JsonSyntaxException if JSON cannot be parsed to given type
     * @see {@link Gson#fromJson(String, Class)}
     */
    public static <T> T fromBytes(final byte[] bytes, final Class<T> clazz) {
        if (bytes == null)  return null;
        return GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), clazz);
    }
    

    /**
     * Parse a JSON byte[] to given type
     * @param response HTTP response
     * @param t     Output class
     * @return Resulting object
     * @throws IOException 
     * @throws CscException
     * @throws JsonSyntaxException if JSON cannot be parsed to given type
     * @see {@link Gson#fromJson(String, Class)}
     */
    public static <T> T fromResponse(final Response response, final Class<T> clazz) throws IOException {
        if (response == null) return null;
        
        if (response.isSuccessful()) {
            return fromJson(response.body().string(), clazz);
        } else {
            throw new CscException(response);
        }
    }
    
    /**
     * Parse a JSON InputStream to given type
     * @param is   JSON InputStream
     * @param t    Output class
     * @return Resulting object
     * @throws JsonSyntaxException if JSON cannot be parsed to given type
     * @see {@link Gson#fromJson(String, Class)}
     */
    public static <T> T fromStream(final InputStream is, final Class<T> clazz) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[4096];
        while ((read = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, read);
        }
        return GSON.fromJson(new String(buffer.toByteArray(), StandardCharsets.UTF_8), clazz);
    }

    /**
     * Parse a JSON String to given type
     * @param json JSON String
     * @param t    Output class
     * @return Resulting object
     */
    public static <T> T fromJson(final String json, final Type t) {
        return GSON.fromJson(json, t);
    }

    /**
     * Convert this object to a JSON String
     * @return JSON String
     */
    public String toJson() {
        return GSON.toJson(this);
    }
    
    @Override
    public String toString() {
        return this.toJson();
    }
    
    /**
     * Convert this JSON object to a String and encode it with base64
     * <p>This is useful for example when sending JSON objects via SOAP
     * @return
     */
    public String toBase64() {
        return Base64.getEncoder().encodeToString(this.getBytes());
    }
    
    /**
     * Convert this JSON to {@link RequestBody}
     * @return  {@link RequestBody}
     */
    public RequestBody toRequestBody() {
        return RequestBody.create(JSON, this.toJson());
    }
    
    /**
     * Get byte[] representation of this JSON
     * @return byte[]
     */
    public byte[] getBytes() {
        return this.toJson().getBytes(StandardCharsets.UTF_8);
    }

    protected static String base64(byte[] data) {
        if (data == null) return "null";
        return Base64.getEncoder().encodeToString(data);
    }

    protected static byte[] base64(String data) {
        if (data == null) return null;
        return Base64.getDecoder().decode(data);
    }
}
