package ir.abring.abringlibrary.network;

public class ApiError {

    /**
     * name : Internal Server Error
     * message : خطای داخلی سرور رخ داده است.
     * code : 0
     * status : 500
     */

    private String name;
    private String message;
    private int code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
