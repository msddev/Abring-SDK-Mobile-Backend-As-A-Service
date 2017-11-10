package ir.abring.abringlibrary.models.abringregister;

import com.google.gson.annotations.SerializedName;

public class AbringRegister {

	@SerializedName("result")
	private AbringResult result;

	@SerializedName("code")
	private String code;

	@SerializedName("message")
	private String message;

	@SerializedName("timestamp")
	private String timestamp;

	public void setResult(AbringResult result){
		this.result = result;
	}

	public AbringResult getResult(){
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