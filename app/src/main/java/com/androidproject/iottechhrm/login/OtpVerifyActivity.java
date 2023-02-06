package com.androidproject.iottechhrm.login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.activity.DashBoardActivity;
import com.androidproject.iottechhrm.otpedittext.OnCompleteListener;
import com.androidproject.iottechhrm.otpedittext.OtpEditText;
import com.androidproject.iottechhrm.retro.ApiClient;
import com.androidproject.iottechhrm.retro.ApiInterface;
import com.androidproject.iottechhrm.util.BaseActivity;
import com.androidproject.iottechhrm.util.Utility;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerifyActivity extends BaseActivity {

    OtpEditText otpEditText;
    Context context;
    String email;
    String code;
    EditText et_pass;
    EditText et_cpass;
    TextInputLayout textlayout_pass;
    TextInputLayout textlayout_cpass;

    String password,c_password;

    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_otp_verify);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        hideToolbar();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        intialise();

        otpEditText.setOnCompleteListener(new OnCompleteListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(String value) {
                if (checkValidation()) {
                    hitOtpVerifyApi();
                }
            }
        });

    }

    private boolean checkValidation() {
        boolean check = true;
        code = otpEditText.getText().toString();
        if (TextUtils.isEmpty(code)) {
            otpEditText.triggerErrorAnimation();
            Toast.makeText(context, getResources().getString(R.string.please_enter_the_valid_OTP), Toast.LENGTH_SHORT).show();
            check = false;
        }

        return check;
    }

    private void hitOtpVerifyApi() {

        Call<String> stringCall = null;
        if (Utility.isNetworkAvailable(context)) {
            showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//            Log.i("cdsv",familyId+" "+name+" "+contact+" "+age_group+" "+multiFamilyImage);
            stringCall = apiInterface.otpVerify(email, code);
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

                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                                changePasswordDialog();

//                                startActivity(new Intent(context,OtpVerifyActivity.class));


                            } else {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                                otpEditText.triggerErrorAnimation();

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

    private void changePasswordDialog() {

        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.password_change);
        dialog.setCancelable(false);
        EditText et_email = dialog.findViewById(R.id.et_email);
        et_email.setText(email);
        et_email.setEnabled(false);
        et_pass = dialog.findViewById(R.id.et_pass);
        et_cpass = dialog.findViewById(R.id.et_cpass);
        textlayout_pass = dialog.findViewById(R.id.textlayout_pass);
        textlayout_cpass = dialog.findViewById(R.id.textlayout_cpass);
        ImageView iv_close=dialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        TextView yesButton = (TextView) dialog.findViewById(R.id.tv_yes);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPasswordValidation()) {
                    hitPasswordApi();
                }



            }
        });

        et_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textlayout_pass.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_cpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textlayout_cpass.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        TextView noButton = (TextView) dialog.findViewById(R.id.tv_no);
//        noButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        dialog.show();

    }

    private void hitPasswordApi() {

        Call<String> stringCall = null;
        if (Utility.isNetworkAvailable(context)) {
            showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//            Log.i("cdsv",familyId+" "+name+" "+contact+" "+age_group+" "+multiFamilyImage);
            stringCall = apiInterface.changePassword(email, password,c_password);
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

                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

//                                startActivity(new Intent(context,OtpVerifyActivity.class));


                            } else {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                                otpEditText.triggerErrorAnimation();

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

    private boolean checkPasswordValidation() {
        boolean check=true;

        password = et_pass.getText().toString();
        if (!validate(password)) {
//            inputPassword.setError(getResources().getString(R.string.password_minimum_Six_characters_at_least));
            textlayout_pass.setError(getResources().getString(R.string.password_minimum_Six_characters_at_least));
            textlayout_pass.requestFocus();
            check = false;
        }

        c_password = et_cpass.getText().toString();
        if (!validate(c_password)) {
//            input_confirm_password.setError(getResources().getString(R.string.password_minimum_Six_characters_at_least));
            textlayout_cpass.setError(getResources().getString(R.string.password_minimum_Six_characters_at_least));
            textlayout_cpass.requestFocus();
            check = false;
        }

        if (!password.equals(c_password)) {
//            input_confirm_password.setError(getResources().getString(R.string.Your_confirm_password_not_match_with_password));
            textlayout_cpass.setError(getResources().getString(R.string.Your_confirm_password_not_match_with_password));
            textlayout_cpass.requestFocus();
            check = false;
        }

        return check;
    }

    private void hitApi() {
    }

    private void intialise() {
        context = this;
        otpEditText = findViewById(R.id.oev_view);
    }
}
