package com.example.rajat.meeting.network;


import com.example.rajat.meeting.model.Meeting;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApi {

    @GET("/api/schedule")
    Call<List<Meeting>> fetchMeetings(@Query("date") String date);

}