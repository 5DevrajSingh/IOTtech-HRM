package com.androidproject.iottechhrm.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.activity.DashBoardActivity;
import com.androidproject.iottechhrm.retro.ApiClient;
import com.androidproject.iottechhrm.retro.ApiInterface;
import com.androidproject.iottechhrm.util.BaseActivity;
import com.androidproject.iottechhrm.util.Utility;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    EditText et_email, et_password;
    TextView tv_login;
    Context context;
    String password, email;
    SharedPreferences pref;
    TextInputLayout textlayout_pass,textlayout_email;
    TextView tv_forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_login);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        hideToolbar();

        intialise();

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitApi();
            }
        });

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textlayout_email.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
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

        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,OtpMainActivity.class));
            }
        });

    }

    private void hitApi() {

        Call<String> stringCall = null;
        if (Utility.isNetworkAvailable(this)) {
            if (checkValidation()) {
                showProgressBar();
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                stringCall = apiInterface.loginApi(email,password);
                stringCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String sResponse = response.body();
//                        Log.i("sddsv",sResponse);
                        hideProgressBar();
                        try {
                            if (!TextUtils.isEmpty(sResponse)) {
                                JSONObject jsonObject = new JSONObject(sResponse);
                                String type = jsonObject.getString("type");
                                String message = jsonObject.getString("message");
                                if (type.equalsIgnoreCase("success")) {
                                    Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    String id = jsonObject1.getString("id");
                                    String name = jsonObject1.getString("name");
                                    String avtar = jsonObject1.getString("avtar");
                                    String email = jsonObject1.getString("email");
                                    String contact = jsonObject1.getString("contact");
                                    String gender = jsonObject1.getString("gender");

                                    pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("id", id);
                                    editor.putString("name", name);
                                    editor.putString("avtar", avtar);
                                    editor.putString("email", email);
                                    editor.putString("contact", contact);
                                    editor.putString("gender", gender);
                                    editor.commit();

                                    Intent intent=new Intent(context, DashBoardActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                                }
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


            }
        } else {
            Toast.makeText(context, getResources().getString(R.string.Please_Check_Your_Internet), Toast.LENGTH_SHORT).show();
        }


    }

    private boolean checkValidation() {
        boolean check = true;

        email = et_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            textlayout_email.setError(getResources().getString(R.string.Email_is_required));
            textlayout_email.requestFocus();
            check = false;
        }

        password = et_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            textlayout_pass.setError(getResources().getString(R.string.Password_is_required));
            textlayout_email.requestFocus();
            check = false;
        }

        return check;
    }

    private void intialise() {
        context = this;
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_login = findViewById(R.id.tv_login);
        textlayout_pass=findViewById(R.id.textlayout_pass);
        textlayout_email=findViewById(R.id.textlayout_email);
        tv_forgot=findViewById(R.id.tv_forgot);
    }
}