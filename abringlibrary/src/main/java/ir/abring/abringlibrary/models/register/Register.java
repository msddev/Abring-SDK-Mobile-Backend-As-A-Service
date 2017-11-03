package ir.abring.abringlibrary.models.register;

import com.google.gson.annotations.SerializedName;

public class Register {

	@SerializedName("result")
	private Result result;

	@SerializedName("code")
	private String code;

	@SerializedName("message")
	private String message;

	@SerializedName("timestamp")
	private String timestamp;

	public void setResult(Result result){
		this.result = result;
	}

	public Result getResult(){
		return result;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setTimestamp(String timestamp){
		this.timestamp = timestamp;
	}

	public String getTimestamp(){
		return timestamp;
	}
}