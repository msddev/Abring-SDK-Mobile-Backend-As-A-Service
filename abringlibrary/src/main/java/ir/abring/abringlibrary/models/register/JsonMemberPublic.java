package ir.abring.abringlibrary.models.register;

import com.google.gson.annotations.SerializedName;

public class JsonMemberPublic{

	@SerializedName("leaderboard_profile")
	private String leaderboardProfile;

	public void setLeaderboardProfile(String leaderboardProfile){
		this.leaderboardProfile = leaderboardProfile;
	}

	public String getLeaderboardProfile(){
		return leaderboardProfile;
	}
}