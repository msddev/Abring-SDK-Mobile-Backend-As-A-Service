package ir.abring.abringlibrary.models.abringregister;

import com.google.gson.annotations.SerializedName;

public class AbringJsonMemberPublic {

	@SerializedName("leaderboard_profile")
	private String leaderboardProfile;

	public void setLeaderboardProfile(String leaderboardProfile){
		this.leaderboardProfile = leaderboardProfile;
	}

	public String getLeaderboardProfile(){
		return leaderboardProfile;
	}
}