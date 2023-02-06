package com.androidproject.iottechhrm.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.inter.CalendarListener;
import com.androidproject.iottechhrm.inter.DayDecorator;
import com.androidproject.iottechhrm.retro.ApiClient;
import com.androidproject.iottechhrm.retro.ApiInterface;
import com.androidproject.iottechhrm.util.BaseActivity;
import com.androidproject.iottechhrm.util.CustomCalendarView;
import com.androidproject.iottechhrm.util.DayView;
import com.androidproject.iottechhrm.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendaceActivity extends BaseActivity {

    SharedPreferences pref;
    String user_id;
    CardView cv_claender;
    CustomCalendarView calendarView;
    Calendar currentCalendar;

    List<String> presentList = new ArrayList<>();
    List<String> absentList = new ArrayList<>();
    List<String> leaveList = new ArrayList<>();
    List<String> holidayList = new ArrayList<>();
    List<String> weekendNameList = new ArrayList<>();
    List<String> weekendDatess = new ArrayList<>();

    LottieAnimationView logo_data;

    ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_attendance);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        hideToolbar();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        user_id = pref.getString("id", null);

        intialise();

        hitCalenderApi();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//                    selectedDateTv.setText("Selected date is " + df.format(date));
//                String date3=df.format(date);
//                if (presentList.contains(date3)) {
//                    Toast.makeText(AttendanceCalenderActivity.this, "" + df.format(date), Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("mm");
//                Toast.makeText(Report_StudentActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void hitCalenderApi() {
        Call<String> stringCall = null;
        if (Utility.isNetworkAvailable(AttendaceActivity.this)) {
            showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id", user_id)
                    .build();
            stringCall = apiInterface.getEmployeeAttendance(body);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String sResponse = response.body();
                    hideProgressBar();
                    if (!TextUtils.isEmpty(sResponse)) {
                        try {
                            JSONObject jsonObject = new JSONObject(sResponse);
                            String type = jsonObject.getString("type");
                            String message = jsonObject.getString("message");
                            if (type.equalsIgnoreCase("success")) {
                                cv_claender.setVisibility(View.VISIBLE);
                                logo_data.setVisibility(View.GONE);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                                    String classs = jsonObject1.getString("class");
                                String emp_id = jsonObject1.getString("emp_id");
                                String total_present = jsonObject1.getString("totalPresent");
//                                    tv_present.setText(total_present);
                                String present = jsonObject1.getString("presentDate");
                                presentList = Arrays.asList(present.split(","));
                                String total_absent = jsonObject1.getString("totalAbsent");
//                                    tv_absent.setText(total_absent);
                                String absent = jsonObject1.getString("absentDate");
                                absentList = Arrays.asList(absent.split(","));
                                String total_leave = jsonObject1.getString("totalLeave");
//                                    tv_leave.setText(total_leave);
                                String leave = jsonObject1.getString("leaveDate");
                                leaveList = Arrays.asList(leave.split(","));
//                                String total_holiday = jsonObject1.getString("total_holiday");
//                                String holiday = jsonObject1.getString("holiday");
////                                    tv_holidays.setText(total_holiday);
//                                holidayList = Arrays.asList(holiday.split(","));
//                                String weekend = jsonObject1.getString("weekend");
//                                weekendNameList = Arrays.asList(weekend.split(","));
//                                String weekendDate = jsonObject1.getString("weekendDate");
//                                weekendDatess = Arrays.asList(weekendDate.split(","));


                                List<DayDecorator> decorators = new ArrayList<>();
                                decorators.add(new DisabledColorDecorator());
                                calendarView.setDecorators(decorators);
                                calendarView.refreshCalendar(currentCalendar);


                            } else {
                                cv_claender.setVisibility(View.GONE);
                                logo_data.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            cv_claender.setVisibility(View.GONE);
                            logo_data.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                            Toast.makeText(AttendaceActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                            hideProgressBar();
                        }
                    } else {
                        cv_claender.setVisibility(View.GONE);
                        logo_data.setVisibility(View.VISIBLE);
                        Toast.makeText(AttendaceActivity.this, "Data not fetched", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    cv_claender.setVisibility(View.GONE);
                    logo_data.setVisibility(View.VISIBLE);
                    Toast.makeText(AttendaceActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                }
            });


        } else {
            cv_claender.setVisibility(View.GONE);
            logo_data.setVisibility(View.VISIBLE);
            Toast.makeText(AttendaceActivity.this, getResources().getString(R.string.please_check_your_connection), Toast.LENGTH_SHORT).show();
            hideProgressBar();
        }

    }


    private void intialise() {

        iv_back = findViewById(R.id.iv_back);

        logo_data = findViewById(R.id.logo_data);

        cv_claender = findViewById(R.id.cv_claender);
        //  calendarView.setVisibility(View.GONE);
        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

        //Initialize calendar with date
        currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);
    }

    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");//formating according to my need
            String date = formatter.format(dayView.getDate());

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            String dayOfTheWeek = sdf.format(dayView.getDate());

            if (dayOfTheWeek.equals("Sunday")) {
                int color = Color.parseColor("#ac3235");
//               dayView.setBackgroundColor(color);
                dayView.setTextColor(color);
            }

//            if (weekendNameList.equals(dayOfTheWeek))
//            {
//                int color = Color.parseColor("#ac3235");
////               dayView.setBackgroundColor(color);
//                dayOfWeek.setTextColor(color);
//            }

            if (weekendDatess.contains(date)) {
                int color = Color.parseColor("#ac3235");
//               dayView.setBackgroundColor(color);
                dayView.setTextColor(color);
            }

            if (presentList.contains(date)) {
                int color = Color.parseColor("#579F2B");
                dayView.setBackgroundColor(color);
            }
            if (absentList.contains(date)) {
                int color = Color.parseColor("#F40002");
                dayView.setBackgroundColor(color);
            }
            if (leaveList.contains(date)) {
                int color = Color.parseColor("#F3AF22");
                dayView.setBackgroundColor(color);
            }
            if (holidayList.contains(date)) {
                int color = Color.parseColor("#5483D4");
                dayView.setBackgroundColor(color);
            }
//            if (CalendarUtils.isPastDay(dayView.getDate())) {
//                int color = Color.parseColor("#a9afb9");
//                dayView.setBackgroundColor(color);
//            }

        }
    }

}
