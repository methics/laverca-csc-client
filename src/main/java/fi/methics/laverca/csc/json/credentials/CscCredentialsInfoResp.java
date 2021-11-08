package fi.methics.laverca.csc.json.credentials;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.csc.json.GsonMessage;

/**
 * <pre>
{
  "key": {
    "status": "enabled",
    "algo": [
      "1.2.840.113549.1.1.1",
      "0.4.0.127.0.7.1.1.4.1.3"
    ],
    "len": 2048
  },
  "cert": {
    "status": "valid",
    "certificates": [
      "<Base64-encoded_X.509_end_entity_certificate>",
      "<Base64-encoded_X.509_intermediate_CA_certificate>",
      "<Base64-encoded_X.509_root_CA_certificate>"
    ],
    "issuerDN": "<X.500_issuer_DN_printable_string>",
    "serialNumber": "5AAC41CD8FA22B953640",
    "subjectDN": "<X.500_subject_DN_printable_string>",
    "validFrom": "20180101100000Z",
    "validTo": "20190101095959Z",
    "authMode": "explicit",
    "PIN": {
      "presence": "true",
      "label": "PIN",
      "description": "Please enter the signature PIN"
    },
    "OTP": {
      "presence": "true",
      "type": "offline",
      "ID": "MB01-K741200",
      "provider": "totp",
      "format": "N",
      "label": "Mobile OTP",
      "description": "Please enter the 6 digit code you received by SMS"
    },
    "multisign": 5,
    "lang": "en-US"
  }
}
 * </pre>
 */
public class CscCredentialsInfoResp extends GsonMessage {

    
    @SerializedName("key")
    public InfoRespKey key;
    
    @SerializedName("cert")
    public InfoRespCert cert;
    
    @SerializedName("description")
    public String description;

    @SerializedName("authMode")
    public String authMode;
    
    @SerializedName("SCAL")
    public String SCAL;

    @SerializedName("multisign")
    public String multisign;
    
    @SerializedName("lang")
    public String lang;
    
    public static class InfoRespKey extends GsonMessage {
        @SerializedName("status")
        public String status;
        
        @SerializedName("algo")
        public List<String> algo;
        
        @SerializedName("len")
        public int len;
    }
    
    public static class InfoRespCert extends GsonMessage {
        @SerializedName("status")
        public String status;
        
        @SerializedName("certificates")
        public List<String> certificates;
        
        @SerializedName("issuerDN")
        public String issuerDN;
        
        @SerializedName("subjectDN")
        public String subjectDN;
        
        @SerializedName("serialNumber")
        public String serialNumber;
        
        @SerializedName("validFrom")
        public String validFrom;
        
        @SerializedName("validTo")
        public String validTo;
        
        
        @SerializedName("PIN")
        public String PIN;
        
        @SerializedName("OTP")
        public String OTP;
        
    }
    
    public static class InfoRespPIN extends GsonMessage {
        @SerializedName("presence")
        public String presence;
        
        @SerializedName("label")
        public String label;
        
        @SerializedName("description")
        public String description;
    }
    
    public static class InfoRespOTP extends GsonMessage {
        @SerializedName("presence")
        public String presence;
        
        @SerializedName("type")
        public String type;
        
        @SerializedName("ID")
        public String ID;
        
        @SerializedName("provider")
        public String provider;
        
        @SerializedName("format")
        public String format;
        
        @SerializedName("label")
        public String label;
                
        @SerializedName("description")
        public String description;
    }
    
    /**
     * Does the response indicate that we should use SCAL2?
     * @return true for SCAL2
     */
    public boolean isScal2() {
        return "2".equals(this.SCAL);
    }
    
}
