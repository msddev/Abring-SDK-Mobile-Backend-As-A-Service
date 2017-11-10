package ir.abring.abringlibrary.models.abringregister;

import com.google.gson.annotations.SerializedName;

public class AbringResult {

	@SerializedName("player_id")
	private String playerId;

	@SerializedName("player_info")
	private AbringPlayerInfo playerInfo;

	@SerializedName("token")
	private String token;

	public void setPlayerId(String playerId){
		this.playerId = playerId;
	}

	public String getPlayerId(){
		return playerId;
	}

	public void setPlayerInfo(AbringPlayerInfo playerInfo){
		this.playerInfo = playerInfo;
	}

	public AbringPlayerInfo getPlayerInfo(){
		return playerInfo;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}
}