package ir.abring.abringlibrary.network;

public class AbringApiError {

    /**
     * code : 400
     * result :
     * message : invalid verify code
     * timestamp : 1510968434
     */

    private String code;
    private String result;
    private String message;
    private String timestamp;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
