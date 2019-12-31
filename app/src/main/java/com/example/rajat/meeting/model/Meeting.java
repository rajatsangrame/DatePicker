package com.example.rajat.meeting.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Meeting {

	@SerializedName("start_time")
	private String startTime;

	@SerializedName("end_time")
	private String endTime;

	@SerializedName("description")
	private String description;

	@SerializedName("participants")
	private List<String> participants;

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}

	public void setEndTime(String endTime){
		this.endTime = endTime;
	}

	public String getEndTime(){
		return endTime;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setParticipants(List<String> participants){
		this.participants = participants;
	}

	public List<String> getParticipants(){
		return participants;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"start_time = '" + startTime + '\'' + 
			",end_time = '" + endTime + '\'' + 
			",description = '" + description + '\'' + 
			",participants = '" + participants + '\'' + 
			"}";
		}
}