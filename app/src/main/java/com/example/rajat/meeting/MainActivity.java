package com.example.rajat.meeting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.example.rajat.meeting.databinding.ActivityMainBinding;
import com.example.rajat.meeting.model.Meeting;
import com.example.rajat.meeting.network.RetrofitApiClient;
import com.example.rajat.meeting.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    public static final int ADD_NEW_MEETING = 1001;
    private ActivityMainBinding mBinding;
    private Date mCurrentDate;
    private int mTempCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.btnAdd.setOnClickListener(this);
        mBinding.btnNext.setOnClickListener(this);
        mBinding.btnPrev.setOnClickListener(this);

        updateToday();

    }


    private void updateToday() {

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        String date = DateFormat.format("dd-MM-yyyy", today).toString();
        mBinding.title.setText(date);
        fetchData(date);
        mCurrentDate = today;

    }

    private void updateNext() {

        mTempCount = mTempCount + 1;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, mTempCount);
        Date nextDay = calendar.getTime();
        String date = DateFormat.format("dd-MM-yyyy", nextDay).toString();
        mBinding.title.setText(date);
        fetchData(date);
        mCurrentDate = nextDay;

    }

    private void updatePrevious() {

        mTempCount = mTempCount - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, mTempCount);
        Date prevDay = calendar.getTime();
        String date = DateFormat.format("dd-MM-yyyy", prevDay).toString();
        mBinding.title.setText(date);
        fetchData(date);
        mCurrentDate = prevDay;
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
                    Helper.showAlert(MainActivity.this, getString(R.string.api_failed));
                    return;
                }
                List<Meeting> list = response.body();

                if (list.isEmpty()) {
                    Helper.showAlert(MainActivity.this, getString(R.string.no_meetings_found));
                }

                updateAdapter(list);
                Log.i(TAG, "onResponse List Size: " + list.size());

            }

            @Override
            public void onFailure(@NonNull Call<List<Meeting>> call, @NonNull Throwable t) {

                Helper.showAlert(MainActivity.this, getString(R.string.api_failed));
            }
        });
    }

    private void updateAdapter(List<Meeting> meetings) {

        if (meetings == null) {
            return;
        }

        MeetingAdapter adapter = new MeetingAdapter(meetings);
        mBinding.recycleView.setAdapter(adapter);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_next:
                updateNext();
                break;
            case R.id.btn_prev:
                updatePrevious();
                break;
            case R.id.btn_add:
                Intent intent = new Intent(MainActivity.this, NewMeetingActivity.class);
                intent.putExtra("date", new Gson().toJson(mCurrentDate));
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == ADD_NEW_MEETING) {

            //update the list
        }
    }
}
