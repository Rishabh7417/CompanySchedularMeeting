package android.example.companymeetingschedular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.example.companymeetingschedular.adapters.MeetingsListAdapter;
import android.example.companymeetingschedular.model.MeetingInfo;
import android.example.companymeetingschedular.retrofit.RetrofitClientInstance;
import android.example.companymeetingschedular.retrofit.RetrofitServiceInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout maintoolbar_layout;
    TextView toolbar_head_tv;
    TextView previous_btn_tv;
    TextView next_btn_tv;

    RecyclerView meetingListRecyclerView;
    TextView schedulebtn_tv;

    ProgressBar progressBar;

    MeetingsListAdapter meetingsListAdapter;
    ArrayList<MeetingInfo> meetingInfoArrayList = new ArrayList<>();
    Date currDate = new Date();
    Date date = new Date();
    Date nextDate = new Date();
    final DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    DateFormat sdf = new SimpleDateFormat("hh:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setClicklisteners();
        previous_btn_tv.setVisibility(View.GONE);
        toolbar_head_tv.setText(formatter.format(currDate));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        meetingListRecyclerView.setLayoutManager(linearLayoutManager);
        meetingsListAdapter = new MeetingsListAdapter(meetingInfoArrayList);
        meetingListRecyclerView.setAdapter(meetingsListAdapter);

        fetchMeetingsForParticularDate(formatter.format(currDate));


    }

    private void findViews()
    {
        maintoolbar_layout = findViewById(R.id.maintoolbar_layout);
        toolbar_head_tv = maintoolbar_layout.findViewById(R.id.toolbar_head_tv);
        previous_btn_tv = maintoolbar_layout.findViewById(R.id.previous_btn_tv);
        next_btn_tv = maintoolbar_layout.findViewById(R.id.next_btn_tv);

        meetingListRecyclerView = findViewById(R.id.meetingListRecyclerView);
        schedulebtn_tv = findViewById(R.id.schedulebtn_tv);
        progressBar = findViewById(R.id.progressBar);

    }

    private void setClicklisteners()
    {
        schedulebtn_tv.setOnClickListener(this);
        previous_btn_tv.setOnClickListener(this);
        next_btn_tv.setOnClickListener(this);
    }

    private void fetchMeetingsForParticularDate(String date)
    {
        RetrofitServiceInterface service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitServiceInterface.class);
        Call<ArrayList<MeetingInfo>> fetchExecutivesCall = service.fetchMeetingsInfoRequest(date);
        progressBar.setVisibility(View.VISIBLE);

        fetchExecutivesCall.enqueue(new Callback<ArrayList<MeetingInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<MeetingInfo>> call, Response<ArrayList<MeetingInfo>> response) {
//                Toast.makeText(RegistrationActivity.this, "Location fetch Successfull", Toast.LENGTH_SHORT).show();
                Gson g = new Gson();
                Log.d("Meetings",g.toJson(response.body())+" "+response.code());
                if(response.code() == 200) {
                    progressBar.setVisibility(View.GONE);
                    if(response.body().size()>0) {
                        meetingInfoArrayList.clear();
                        meetingInfoArrayList.addAll(sortList(response.body()));
                        meetingsListAdapter.notifyDataSetChanged();
                    }
                    else
                    {

                    }
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<MeetingInfo>> call, Throwable t) {
                Gson g = new Gson();
                progressBar.setVisibility(View.GONE);
                Log.d("Executives",g.toJson(t.getMessage()));
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<MeetingInfo> sortList(ArrayList<MeetingInfo> list)
    {
        for(int i = 0;i<list.size();i++)
        {
            for(int j =0;j<list.size()-1-i;j++)
            {
                try {
                    long curr = sdf.parse(list.get(j).getStart_time()).getTime();
                    long next = sdf.parse(list.get(j+1).getStart_time()).getTime();
                    MeetingInfo tempInfo;
                    if(curr>next)
                    {
                        tempInfo = list.get(j);
                        list.set(j,list.get(j+1));
                        list.set(j+1,tempInfo);
                    }
                }
                catch (Exception e)
                {

                }
            }
        }
        return list;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.schedulebtn_tv:
                Intent scheduleIntent = new Intent(MainActivity.this,ScheduleMeetingActivity.class);
                startActivity(scheduleIntent);
                break;

            case R.id.next_btn_tv:
                if(currDate.equals(date))
                {
                    previous_btn_tv.setVisibility(View.VISIBLE);
                }
                date = Utils.getNextDate(date);
                toolbar_head_tv.setText(formatter.format(date));
                fetchMeetingsForParticularDate(formatter.format(date));
                break;

            case R.id.previous_btn_tv:

                date = Utils.getPrevDate(date);
                toolbar_head_tv.setText(formatter.format(date));
                fetchMeetingsForParticularDate(formatter.format(date));
                if(currDate.equals(date))
                {
                    previous_btn_tv.setVisibility(View.GONE);
                }
                break;
        }
    }
}
