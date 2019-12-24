package android.example.companymeetingschedular;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.example.companymeetingschedular.model.MeetingInfo;
import android.example.companymeetingschedular.retrofit.RetrofitClientInstance;
import android.example.companymeetingschedular.retrofit.RetrofitServiceInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout maintoolbar_layout;
    TextView toolbar_head_tv;
    TextView backBtn;
    TextView nextBtn;

    TextView meetingdate_btn_tv;
    TextView starttime_btn_tv;
    TextView endtime_btn_tv;
    EditText desc_et;
    TextView submitbtn_tv;


    DateFormat sdf = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_meeting);

        findViews();
        setClickListeners();

        backBtn.setText(getResources().getString(R.string.back));
        nextBtn.setVisibility(View.GONE);
        toolbar_head_tv.setText(getResources().getString(R.string.schedule_a_meeting_text));

    }

    private void findViews()
    {
        maintoolbar_layout = findViewById(R.id.maintoolbar_layout);
        backBtn = maintoolbar_layout.findViewById(R.id.previous_btn_tv);
        nextBtn = maintoolbar_layout.findViewById(R.id.next_btn_tv);
        toolbar_head_tv = maintoolbar_layout.findViewById(R.id.toolbar_head_tv);

        meetingdate_btn_tv = findViewById(R.id.meetingdate_btn_tv);
        starttime_btn_tv = findViewById(R.id.starttime_btn_tv);
        endtime_btn_tv = findViewById(R.id.endtime_btn_tv);
        desc_et = findViewById(R.id.desc_et);
        submitbtn_tv = findViewById(R.id.submitbtn_tv);
    }

    private void setClickListeners()
    {
        backBtn.setOnClickListener(this);
        meetingdate_btn_tv.setOnClickListener(this);
        starttime_btn_tv.setOnClickListener(this);
        endtime_btn_tv.setOnClickListener(this);
        submitbtn_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.previous_btn_tv:
                onBackPressed();
                break;

            case R.id.meetingdate_btn_tv:
                showDatePickerDialog(meetingdate_btn_tv);
                break;

            case R.id.starttime_btn_tv:
                showTimePickerDialog(starttime_btn_tv);
                break;

            case R.id.endtime_btn_tv:
                showTimePickerDialog(endtime_btn_tv);
                break;

            case R.id.submitbtn_tv:

                validateData();

        }
    }


    private void fetchMeetingsForParticularDate(String date, final OnDataFetchedListener onDataFetchedListener)
    {
        RetrofitServiceInterface service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitServiceInterface.class);
        Call<ArrayList<MeetingInfo>> fetchExecutivesCall = service.fetchMeetingsInfoRequest(date);

        fetchExecutivesCall.enqueue(new Callback<ArrayList<MeetingInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<MeetingInfo>> call, Response<ArrayList<MeetingInfo>> response) {
//                Toast.makeText(RegistrationActivity.this, "Location fetch Successfull", Toast.LENGTH_SHORT).show();
                Gson g = new Gson();
                Log.d("Meetings",g.toJson(response.body())+" "+response.code());
                if(response.code() == 200) {
                        onDataFetchedListener.onFetch(response.body());
                }
                else
                {
                    Toast.makeText(ScheduleMeetingActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<MeetingInfo>> call, Throwable t) {
                Gson g = new Gson();
                Log.d("Executives",g.toJson(t.getMessage()));
                Toast.makeText(ScheduleMeetingActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog(final TextView textView)
    {
        final Calendar now = Calendar.getInstance();
        final DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        int storedYear;
        int storedMonth;
        int storedDay;

        String storedDate = textView.getText().toString();

        storedYear = now.get(Calendar.YEAR);
        storedMonth = now.get(Calendar.MONTH);
        storedDay = now.get(Calendar.DAY_OF_MONTH);


        android.app.DatePickerDialog dpd=new android.app.DatePickerDialog(
                ScheduleMeetingActivity.this,
                new android.app.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month++;
                        String dateToShow =((dayOfMonth<10)?("0"+dayOfMonth):dayOfMonth)+"-"+((month<10)?("0"+month):month)+"-"+year;
                        textView.setText(dateToShow);
                        try {

                        }catch(Exception e)
                        {
                            Log.e("Error",e.toString());
                        }

                    }
                },
                storedYear,
                storedMonth,
                storedDay
        );

        Date minDate = new Date();

        dpd.getDatePicker().setMinDate(minDate.getTime());
        dpd.show();
    }

    private void showTimePickerDialog(final TextView textView)
    {

        Calendar now3 = Calendar.getInstance();

        int currentHour = now3.get(Calendar.HOUR_OF_DAY);
        int currentMin = now3.get(Calendar.MINUTE);

            new android.app.TimePickerDialog(
                    ScheduleMeetingActivity.this,
                    new android.app.TimePickerDialog.OnTimeSetListener(){

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            String timeToStore = ((hourOfDay < 10) ? ("0" + hourOfDay) : hourOfDay) + ":" + ((minute < 10) ? ("0" + minute) : minute);

                            textView.setTag(timeToStore);
                            textView.setText(timeToStore);
                        }
                    }

                    ,
                    currentHour,
                    currentMin,
                    true
            ).show()
            ;
    }

    private void validateData()
    {
        if(meetingdate_btn_tv.getText().toString().isEmpty() ||
                starttime_btn_tv.getText().toString().isEmpty() ||
                endtime_btn_tv.getText().toString().isEmpty() ||
                desc_et.getText().toString().isEmpty())
        {
            Toast.makeText(ScheduleMeetingActivity.this,"Please fill all data",Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if(sdf.parse(starttime_btn_tv.getText().toString()).getTime() > sdf.parse(endtime_btn_tv.getText().toString()).getTime())
            {
                Toast.makeText(ScheduleMeetingActivity.this,"Please fill valid time",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        catch (Exception e)
        {

        }


        fetchMeetingsForParticularDate(meetingdate_btn_tv.getText().toString(), new OnDataFetchedListener() {
            @Override
            public void onFetch(ArrayList<MeetingInfo> meetingInfoArrayList) {
                boolean flag = false;
              for(MeetingInfo meetingInfo : meetingInfoArrayList)
              {
                  if(!checkIfSlotAvailable(meetingInfo.getStart_time(),meetingInfo.getEnd_time())) {
                      flag = true;
                      Toast.makeText(ScheduleMeetingActivity.this, "Slot Not Available", Toast.LENGTH_SHORT).show();
                      break;
                  }
              }
              if(!flag)
                  Toast.makeText(ScheduleMeetingActivity.this, "Slot Available", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean checkIfSlotAvailable(String startTime , String endtime)
    {
        try {
            long choosenStartTime = sdf.parse(starttime_btn_tv.getText().toString()).getTime();
            long choosenEndTime = sdf.parse(endtime_btn_tv.getText().toString()).getTime();
            long selectedStartTime = sdf.parse(startTime).getTime();
            long selectedEndTime = sdf.parse(endtime).getTime();

            if(choosenStartTime>selectedStartTime && choosenStartTime < selectedEndTime)
            {
                return false;
            }
            else if(choosenEndTime > selectedStartTime && choosenEndTime < selectedEndTime)
            {
                return false;
            }
            else if(choosenStartTime<selectedStartTime && choosenEndTime > selectedEndTime)
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }



    interface OnDataFetchedListener
    {
        void onFetch(ArrayList<MeetingInfo> meetingInfoArrayList);
    }

}
