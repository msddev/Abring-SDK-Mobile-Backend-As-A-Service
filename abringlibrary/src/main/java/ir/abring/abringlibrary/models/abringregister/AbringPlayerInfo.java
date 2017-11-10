package ir.abring.abringlibrary.models.abringregister;

import com.google.gson.annotations.SerializedName;

public class AbringPlayerInfo {

	@SerializedName("player_id")
	private String playerId;

	@SerializedName("public")
	private AbringJsonMemberPublic jsonMemberPublic;

	@SerializedName("create_time")
	private String createTime;

	@SerializedName("phone")
	private String phone;

	@SerializedName("profile")
	private String profile;

	@SerializedName("name")
	private String name;

	@SerializedName("avatar")
	private String avatar;

	@SerializedName("app_id")
	private String appId;

	@SerializedName("email")
	private String email;

	@SerializedName("friends")
	private AbringFriends friends;

	@SerializedName("username")
	private String username;

	@SerializedName("tags")
	private AbringTags tags;

	public void setPlayerId(String playerId){
		this.playerId = playerId;
	}

	public String getPlayerId(){
		return playerId;
	}

	public void setJsonMemberPublic(AbringJsonMemberPublic jsonMemberPublic){
		this.jsonMemberPublic = jsonMemberPublic;
	}

	public AbringJsonMemberPublic getJsonMemberPublic(){
		return jsonMemberPublic;
	}

	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getCreateTime(){
		return createTime;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setProfile(String profile){
		this.profile = profile;
	}

	public String getProfile(){
		return profile;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return avatar;
	}

	public void setAppId(String appId){
		this.appId = appId;
	}

	public String getAppId(){
		return appId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setFriends(AbringFriends friends){
		this.friends = friends;
	}

	public AbringFriends getFriends(){
		return friends;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public void setTags(AbringTags tags){
		this.tags = tags;
	}

	public AbringTags getTags(){
		return tags;
	}
}