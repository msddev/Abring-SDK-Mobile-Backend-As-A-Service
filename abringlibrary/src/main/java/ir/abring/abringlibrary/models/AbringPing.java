package ir.abring.abringlibrary.models;

public class AbringPing {

    /**
     * code : 200
     * result : 1.509665938729434E9
     * message : Action executed successfully!
     * timestamp : 1509665938
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
