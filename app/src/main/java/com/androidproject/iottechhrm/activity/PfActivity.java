package com.androidproject.iottechhrm.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.adapter.PFAdapter;
import com.androidproject.iottechhrm.adapter.SalaryDetailAdapter;
import com.androidproject.iottechhrm.inter.OnRecyler;
import com.androidproject.iottechhrm.model.PF_Detail;
import com.androidproject.iottechhrm.model.Salary_Detail;
import com.androidproject.iottechhrm.retro.ApiClient;
import com.androidproject.iottechhrm.retro.ApiInterface;
import com.androidproject.iottechhrm.util.BaseActivity;
import com.androidproject.iottechhrm.util.CustomLinearRecyclerview;
import com.androidproject.iottechhrm.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PfActivity extends BaseActivity {

    ImageView iv_back;
    CustomLinearRecyclerview customLinearRecyclerview;
    Context context;
    ArrayList<PF_Detail> pf_details;
    SharedPreferences pref;
    String id;
    PFAdapter pfAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_pf);
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

        hitApi();

    }

    private void intialise() {
        context=this;
        iv_back=findViewById(R.id.iv_back);
        customLinearRecyclerview=findViewById(R.id.customLinearRecyclerview);
        customLinearRecyclerview.setLayoutManager(new LinearLayoutManager(this));
    }


    private void hitApi()
    {
        Call<String> stringCall = null;
        if (Utility.isNetworkAvailable(context)) {
            showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//            Log.i("cdsv",familyId+" "+name+" "+contact+" "+age_group+" "+multiFamilyImage);
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id", id)
                    .build();
            stringCall = apiInterface.pfDetail(body);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
                    Log.i("dsvsvd",sResponse);
                    hideProgressBar();
                    try {
                        if (!TextUtils.isEmpty(sResponse)) {
                            JSONObject jsonObject = new JSONObject(sResponse);
                            String result = jsonObject.getString("type");
                            String message = jsonObject.getString("message");
                            if (result.equalsIgnoreCase("success")) {
                                pf_details=new ArrayList<>();
                                JSONArray jsonArray=jsonObject.getJSONArray("pfDetails");
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String provident_fund=jsonObject1.getString("provident_fund");
                                    String payment_month=jsonObject1.getString("payment_month");
                                    String gross_salary=jsonObject1.getString("gross_salary");
                                    String basic_salary=jsonObject1.getString("basic_salary");
                                    String net_salary=jsonObject1.getString("net_salary");
                                    String bonus=jsonObject1.getString("bonus");

                                    pf_details.add(new PF_Detail(provident_fund,payment_month,gross_salary,basic_salary,net_salary,bonus));
                                }


                                setAdapter(pf_details);


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

    private void setAdapter(ArrayList<PF_Detail> pf_details) {
        pfAdapter=new PFAdapter(context,pf_details, new OnRecyler() {
            @Override
            public void onClick(String s, int pos) {

            }

            @Override
            public void onLongClick(String s, int pos) {

            }
        });

        customLinearRecyclerview.setAdapter(pfAdapter);

        runAnimationAgain();
    }

    private void runAnimationAgain() {


        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.gridlayout_animation_from_bottom);

        customLinearRecyclerview.setLayoutAnimation(controller);
        pfAdapter.notifyDataSetChanged();
        customLinearRecyclerview.scheduleLayoutAnimation();

    }

}
