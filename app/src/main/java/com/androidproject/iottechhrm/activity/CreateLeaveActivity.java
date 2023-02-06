package com.androidproject.iottechhrm.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.retro.ApiClient;
import com.androidproject.iottechhrm.retro.ApiInterface;
import com.androidproject.iottechhrm.util.BaseActivity;
import com.androidproject.iottechhrm.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateLeaveActivity extends BaseActivity {
    Spinner spin_leave_cat, spin_last_leave_cat;
    Context context;
    String[] leaveTypes = {"Select Leave Category", "Casual Leave", "Sick Leave", "Earned Leave", "Special Leave"};
    String[] lastLeaveTypes = {"Select Leave Category", "Casual Leave", "Sick Leave", "Earned Leave", "Special Leave"};
    String category, lastCategory;
    EditText et_pl, et_start_date, et_end_date, et_date_of_retuen, et_plastLeave, et_leave_add, et_reason, et_bottom_section;
    ImageView iv_start_date, iv_end_date, iv_date_of_retuen;
    TextView btn_submit;
    TextInputLayout textinput_pl, textinput_start_date, textinput_end_date, textinput_date_of_retuen, textinput_plastLeave, textinput_leave_add, textinput_reason, textinput_bottom_section;

    int mYear, mMonth, mDay;
    String month = "";
    String day = "";
    String format = "";

    String id, period, startDate, endDate, lastDate, lastPeriod, address, reason, bottomSection;
    SharedPreferences pref;
    ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_create_leave);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        hideToolbar();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        id = pref.getString("id", null);

        intialise();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, leaveTypes);
        spin_leave_cat.setAdapter(classAdapter);
        spin_leave_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Typeface typeface = ResourcesCompat.getFont(context, R.font.baumans);
                ((TextView) parent.getChildAt(0)).setTypeface(typeface);

                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                category = leaveTypes[position];

//                Toast.makeText(getActivity(), ""+jobType, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> lastLeaveAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, lastLeaveTypes);
        spin_last_leave_cat.setAdapter(lastLeaveAdapter);
        spin_last_leave_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Typeface typeface = ResourcesCompat.getFont(context, R.font.baumans);
                ((TextView) parent.getChildAt(0)).setTypeface(typeface);

                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                lastCategory = lastLeaveTypes[position];

//                Toast.makeText(getActivity(), ""+jobType, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(et_start_date, 1);
            }
        });

        et_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(et_end_date, 2);
            }
        });

        et_date_of_retuen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(et_date_of_retuen, 3);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    applyLeaveApi();
                }
            }
        });

        et_start_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textinput_start_date.setError(null);
                iv_start_date.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_end_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textinput_end_date.setError(null);
                iv_end_date.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_date_of_retuen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textinput_date_of_retuen.setError(null);
                iv_date_of_retuen.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_pl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textinput_pl.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_plastLeave.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textinput_plastLeave.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_leave_add.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textinput_leave_add.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textinput_reason.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_bottom_section.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textinput_bottom_section.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private boolean checkValidation() {
        boolean check = true;
        if (category.equalsIgnoreCase("Select Leave Category")) {
            Toast.makeText(context, "please choose Leave Category", Toast.LENGTH_SHORT).show();
            spin_leave_cat.requestFocus();
            check = false;
        }

        period = et_pl.getText().toString();
        if (TextUtils.isEmpty(period)) {
            textinput_pl.setError(getResources().getString(R.string.please_required));
            textinput_pl.requestFocus();
            check = false;
        }

        startDate = et_start_date.getText().toString();
        if (TextUtils.isEmpty(startDate)) {
            textinput_start_date.setError(getResources().getString(R.string.please_required));
            textinput_start_date.requestFocus();
            iv_start_date.setVisibility(View.GONE);
            check = false;
        }

        endDate = et_end_date.getText().toString();
        if (TextUtils.isEmpty(endDate)) {
            textinput_end_date.setError(getResources().getString(R.string.please_required));
            textinput_end_date.requestFocus();
            iv_end_date.setVisibility(View.GONE);
            check = false;
        }

//        lastDate = et_date_of_retuen.getText().toString();
//        if (TextUtils.isEmpty(lastDate)) {
//            textinput_date_of_retuen.setError(getResources().getString(R.string.please_required));
//            textinput_date_of_retuen.requestFocus();
//            iv_date_of_retuen.setVisibility(View.GONE);
//            check = false;
//        }

//        lastPeriod = et_plastLeave.getText().toString();
//        if (TextUtils.isEmpty(lastDate)) {
//            textinput_plastLeave.setError(getResources().getString(R.string.please_required));
//            textinput_plastLeave.requestFocus();
//            check = false;
//        }

//        if (lastCategory.equalsIgnoreCase("Select Leave Category")) {
//            Toast.makeText(context, "please choose Leave Category", Toast.LENGTH_SHORT).show();
//            spin_last_leave_cat.requestFocus();
//            check = false;
//        }

//        address = et_leave_add.getText().toString();
//        if (TextUtils.isEmpty(address)) {
//            textinput_leave_add.setError(getResources().getString(R.string.please_required));
//            textinput_leave_add.requestFocus();
//            check = false;
//        }

        reason = et_reason.getText().toString();
        if (TextUtils.isEmpty(reason)) {
            textinput_reason.setError(getResources().getString(R.string.please_required));
            textinput_reason.requestFocus();
            check = false;
        }

        bottomSection = et_bottom_section.getText().toString();
        if (TextUtils.isEmpty(bottomSection)) {
            textinput_bottom_section.setError(getResources().getString(R.string.please_required));
            textinput_bottom_section.requestFocus();
            check = false;
        }

        return check;
    }

    private void applyLeaveApi() {

        Call<String> stringCall = null;
        if (Utility.isNetworkAvailable(context)) {
            showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//            Log.i("cdsv",familyId+" "+name+" "+contact+" "+age_group+" "+multiFamilyImage);
            stringCall = apiInterface.createLeave(id, category, period, startDate, endDate,lastDate,lastPeriod,lastCategory,address,reason,bottomSection);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
//                    Log.i("dsvsvd",sResponse);
                    hideProgressBar();
                    try {
                        if (!TextUtils.isEmpty(sResponse)) {
                            JSONObject jsonObject = new JSONObject(sResponse);
                            String result = jsonObject.getString("type");
                            String message = jsonObject.getString("message");
                            if (result.equalsIgnoreCase("success")) {

                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CreateLeaveActivity.this,LeaveDatailActivity.class));
                                finish();

                            } else {
                               Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                            }



                        } else {

                            Toast.makeText(context, getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                }
            });

        } else {
            Toast.makeText(context, getResources().getString(R.string.please_check_your_connection), Toast.LENGTH_SHORT).show();
        }


    }

    private void datePicker(EditText edittext, int i) {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        if (monthOfYear < 9) {
                            month = "0" + (monthOfYear + 1);
                        } else {
                            month = "" + (monthOfYear + 1);
                        }

                        if (dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        } else {

                            day = "" + dayOfMonth;
                        }


                        edittext.setText(year + "-" + month + "-" + day);
                        if (i == 1) {
                            edittext.setError(null);
                            iv_start_date.setVisibility(View.VISIBLE);
                        }
                        if (i == 2) {
                            edittext.setError(null);
                            iv_end_date.setVisibility(View.VISIBLE);
                        }
                        if (i == 3) {
                            edittext.setError(null);
                            iv_date_of_retuen.setVisibility(View.VISIBLE);
                        }


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void intialise() {
        context = this;
        spin_leave_cat = findViewById(R.id.spin_leave_cat);
        et_pl = findViewById(R.id.et_pl);
        et_start_date = findViewById(R.id.et_start_date);
        et_end_date = findViewById(R.id.et_end_date);
        et_date_of_retuen = findViewById(R.id.et_date_of_retuen);
        et_plastLeave = findViewById(R.id.et_plastLeave);
        et_leave_add = findViewById(R.id.et_leave_add);
        et_reason = findViewById(R.id.et_reason);
        et_bottom_section = findViewById(R.id.et_bottom_section);
        iv_start_date = findViewById(R.id.iv_start_date);
        iv_end_date = findViewById(R.id.iv_end_date);
        iv_date_of_retuen = findViewById(R.id.iv_date_of_retuen);
        btn_submit = findViewById(R.id.btn_submit);
        textinput_pl = findViewById(R.id.textinput_pl);
        textinput_start_date = findViewById(R.id.textinput_start_date);
        textinput_end_date = findViewById(R.id.textinput_end_date);
        textinput_date_of_retuen = findViewById(R.id.textinput_date_of_retuen);
        textinput_plastLeave = findViewById(R.id.textinput_plastLeave);
        textinput_leave_add = findViewById(R.id.textinput_leave_add);
        textinput_reason = findViewById(R.id.textinput_reason);
        textinput_bottom_section = findViewById(R.id.textinput_bottom_section);
        spin_last_leave_cat=findViewById(R.id.spin_last_leave_cat);
        iv_back=findViewById(R.id.iv_back);
    }
}
