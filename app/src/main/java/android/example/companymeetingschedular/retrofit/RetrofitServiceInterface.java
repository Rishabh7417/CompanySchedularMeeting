package android.example.companymeetingschedular.retrofit;

import android.example.companymeetingschedular.model.MeetingInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface RetrofitServiceInterface {

    @GET("schedule")
    Call<ArrayList<MeetingInfo>> fetchMeetingsInfoRequest(@Query("date") String selectedDate);

}
