package fi.methics.laverca.csc;

import com.squareup.okhttp.Response;

import fi.methics.laverca.csc.json.CscErrorResp;

public class CscException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private CscErrorResp error;
    private int httpcode = -1;
    
    /**
     * Create an exception that highlights that the {@link CscClient#authLogin()}
     * must be called first
     * @return Exception
     */
    public static CscException createNotLoggedInException() {
        CscException ex = new CscException();
        ex.error = new CscErrorResp();
        ex.error.error = "client_error";
        ex.error.error_description = "Client is not logged in";
        return ex;
    }
    /**
     * Create an exception that highlights that the request is missing a parameter
     * @param param Name of missing parameter
     * @return Exception
     */
    public static CscException createMissingParamException(String param) {
        CscException ex = new CscException();
        ex.error = new CscErrorResp();
        ex.error.error = "client_error";
        ex.error.error_description = "Missing parameter " + param;
        return ex;
    }
    
    public CscException(Response response) {
        try {
            this.httpcode = response.code();
            this.error    = CscErrorResp.fromResponse(response);
        } catch (Exception e) {
            this.error = new CscErrorResp();
            error.error = "server_error";
            error.error_description = "Failed to parse error:" + e.getMessage();
            e.printStackTrace();
        }
    }
    
    public CscException(Exception e) {
        this.error = new CscErrorResp();
        error.error = "server_error";
        error.error_description = "Request failed: " + e.getMessage();
    }

    private CscException() {
        
    }
    
    /**
     * Get error JSON
     * @return error JSON if available
     */
    public CscErrorResp getError() {
        return this.error;
    }
    
    /**
     * Get HTTP statuscode of the error
     * @return HTTP statuscode or -1 if not available
     */
    public int getHttpStatusCode() {
        return this.httpcode;
    }
    
}
