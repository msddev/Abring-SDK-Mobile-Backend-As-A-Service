package ir.abring.abringlibrary.models.register;

import com.google.gson.annotations.SerializedName;

public class Result{

	@SerializedName("player_id")
	private String playerId;

	@SerializedName("player_info")
	private PlayerInfo playerInfo;

	@SerializedName("token")
	private String token;

	public void setPlayerId(String playerId){
		this.playerId = playerId;
	}

	public String getPlayerId(){
		return playerId;
	}

	public void setPlayerInfo(PlayerInfo playerInfo){
		this.playerInfo = playerInfo;
	}

	public PlayerInfo getPlayerInfo(){
		return playerInfo;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}
}