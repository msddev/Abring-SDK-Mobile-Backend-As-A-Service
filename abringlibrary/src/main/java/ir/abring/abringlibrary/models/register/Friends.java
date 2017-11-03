package ir.abring.abringlibrary.models.register;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Friends{

	@SerializedName("invitations")
	private List<Object> invitations;

	@SerializedName("members")
	private List<Object> members;

	@SerializedName("requests")
	private List<Object> requests;

	public void setInvitations(List<Object> invitations){
		this.invitations = invitations;
	}

	public List<Object> getInvitations(){
		return invitations;
	}

	public void setMembers(List<Object> members){
		this.members = members;
	}

	public List<Object> getMembers(){
		return members;
	}

	public void setRequests(List<Object> requests){
		this.requests = requests;
	}

	public List<Object> getRequests(){
		return requests;
	}
}