package com.example.rajat.meeting;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.rajat.meeting.databinding.ActivityNewMeetingBinding;
import com.example.rajat.meeting.model.Meeting;
import com.example.rajat.meeting.network.RetrofitApi;
import com.example.rajat.meeting.network.RetrofitApiClient;
import com.example.rajat.meeting.utils.Helper;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityNewMeetingBinding mBinding;
    private static final String TAG = "NewMeetingActivity";
    private String mDate;
    private int mHour, mMinute;
    private String mStartTime, mEndTime = "";
    private Date sTime, eTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_meeting);

        mBinding.btnClose.setOnClickListener(this);
        mBinding.btnDate.setOnClickListener(this);
        mBinding.btnStartTime.setOnClickListener(this);
        mBinding.btnEndTime.setOnClickListener(this);
        mBinding.btnSubmit.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String json = bundle.getString("date", "");

        Date date = new Gson().fromJson(json, Date.class);

        if (!DateUtils.isToday(date.getTime()) && System.currentTimeMillis() > date.getTime()) {
            mBinding.btnSubmit.setEnabled(false);
        }
        mDate = DateFormat.format("dd-MM-yyyy", date).toString();
        mBinding.btnDate.setText(mDate);

    }

    private void showTimePicker(final boolean isStartTime) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int pHour,
                                          int pMinute) {

                        mHour = pHour;
                        mMinute = pMinute;

                        if (isStartTime) {

                            mStartTime = String.format("%s:%s", mHour, mMinute);
                            mBinding.btnStartTime.setText(mStartTime);

                        } else {
                            mEndTime = String.format("%s:%s", mHour, mMinute);
                            mBinding.btnEndTime.setText(mEndTime);
                        }
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void showDatePicker() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                mDateListener,
                year, month, day
        );
        dialog.show();
    }

    DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {


            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();

            if (!DateUtils.isToday(date.getTime()) && System.currentTimeMillis() > date.getTime()) {
                mBinding.btnSubmit.setEnabled(false);
            } else {
                mBinding.btnSubmit.setEnabled(true);

            }

            month = month + 1;

            Log.d("onDateSet", month + "/" + day + "/" + year);
            mDate = String.format("%s-%s-%s", day, month, year);
            mBinding.btnDate.setText(mDate);

        }
    };

    public void processSubmit() {

        if (mBinding.btnStartTime.getText().length() == 0) {

            Helper.showAlert(this, "Select the Start Time");
            return;

        } else if (mBinding.btnEndTime.getText().length() == 0) {

            Helper.showAlert(this, "Select the End Time");
            return;

        } else if (mBinding.btnStartTime.getText().toString().equals(mBinding.btnEndTime.getText().toString())) {

            Helper.showAlert(this, "Start & End Time is same");
            return;

        } else if (mBinding.textInputEditText.getText().toString().length() == 0) {

            Helper.showAlert(this, "Description is empty");
            return;

        }


        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());

        try {
            sTime = sdf.parse(mStartTime);
            eTime = sdf.parse(mEndTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert eTime != null;
        assert sTime != null;

        if (eTime.before(sTime)) {

            Helper.showAlert(this, "End Time is before the Start Time");

        } else {

            fetchData(mDate);
        }

    }





    private void fetchData(String date) {

        if (!Helper.isNetworkAvailable(this)) {

            Helper.showAlert(this, getString(R.string.no_network));
            return;
        }

        Call<List<Meeting>> call = RetrofitApiClient.getInstance().fetchMeetings(date);
        call.enqueue(new Callback<List<Meeting>>() {
            @Override
            public void onResponse(@NonNull Call<List<Meeting>> call,
                                   @NonNull Response<List<Meeting>> response) {

                if (response.body() == null) {
                    Helper.showAlert(NewMeetingActivity.this, getString(R.string.api_failed));
                    return;
                }
                List<Meeting> list = response.body();

                if (list.isEmpty()) {
                    Helper.showMessage(NewMeetingActivity.this, "Slot is available");
                }


                boolean isSlotValid = true;
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());

                for (Meeting meeting : list) {

                    try {
                        Date meetStartTime = sdf.parse(meeting.getStartTime());
                        Date meetEndTime = sdf.parse(meeting.getEndTime());

                        if ((sTime.after(meetStartTime) && sTime.before(meetEndTime))
                                ||
                                ((eTime.after(meetStartTime) && eTime.before(meetEndTime)))) {

                            isSlotValid = false;

                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                if (isSlotValid) {
                    Helper.showMessage(NewMeetingActivity.this, "Slot is available");
                } else {
                    Helper.showAlert(NewMeetingActivity.this, "Slot is not available");
                }

                Log.i(TAG, "onResponse List Size: " + list.size());

            }

            @Override
            public void onFailure(@NonNull Call<List<Meeting>> call, @NonNull Throwable t) {

                Helper.showAlert(NewMeetingActivity.this, getString(R.string.api_failed));
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_close:
                finish();
                break;

            case R.id.btn_date:
                showDatePicker();
                break;

            case R.id.btn_start_time:
                showTimePicker(true);
                break;
            case R.id.btn_end_time:
                showTimePicker(false);
                break;
            case R.id.btn_submit:
                processSubmit();
                break;

        }
    }
}
