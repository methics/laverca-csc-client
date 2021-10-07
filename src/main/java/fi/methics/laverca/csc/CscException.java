package fi.methics.laverca.csc;

import java.io.IOException;

import com.squareup.okhttp.Response;

import fi.methics.laverca.csc.json.CscErrorResp;

public class CscException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private CscErrorResp error;
    
    public CscException(Response response) {
        try {
            this.error = CscErrorResp.fromResponse(response);
        } catch (Exception e) {
            this.error = new CscErrorResp();
            error.error = "server_error";
            error.error_description = "Failed to parse error:" + e.getMessage();
            e.printStackTrace();
        }
    }
    
    public CscException(IOException e) {
        this.error = new CscErrorResp();
        error.error = "server_error";
        error.error_description = "Request failed: " + e.getMessage();
    }

    public CscErrorResp getError() {
        return this.error;
    }
    
}
