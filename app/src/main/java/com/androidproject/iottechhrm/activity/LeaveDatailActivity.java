package com.androidproject.iottechhrm.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.adapter.LeaveAdapter;
import com.androidproject.iottechhrm.inter.OnRecyler;
import com.androidproject.iottechhrm.model.Leave_Detail;
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

public class LeaveDatailActivity extends BaseActivity {

    CustomLinearRecyclerview customLinearRecyclerview;
    ImageView iv_back;
    SharedPreferences pref;
    String id;
    Context context;
    ArrayList<Leave_Detail> leave_details;
    LeaveAdapter leaveAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_leave_detail);
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
            stringCall = apiInterface.leaveDetail(body);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
                    System.out.println("LeaveResponse....  "+sResponse);
//                    Log.i("dsvsvd",sResponse);
                    hideProgressBar();
                    try {
                        if (!TextUtils.isEmpty(sResponse)) {
                            JSONObject jsonObject = new JSONObject(sResponse);
                            String result = jsonObject.getString("type");
                            String message = jsonObject.getString("message");
                            if (result.equalsIgnoreCase("success")) {
                                leave_details=new ArrayList<>();
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String emp_id=jsonObject1.getString("emp_id");
                                    String category=jsonObject1.getString("category");
                                    String period=jsonObject1.getString("period");
                                    String startDate=jsonObject1.getString("startDate");
                                    String endDate=jsonObject1.getString("endDate");
                                    String lastDate=jsonObject1.getString("lastDate");
                                    String lastPeriod=jsonObject1.getString("lastPeriod");
                                    String lastCategory=jsonObject1.getString("lastCategory");
                                    String address=jsonObject1.getString("address");
                                    String bottomSection=jsonObject1.getString("bottomSection");
                                    String status=jsonObject1.getString("status");
                                    String applied_at=jsonObject1.getString("applied_at");
                                    leave_details.add(new Leave_Detail(emp_id,category,period,startDate,endDate,lastDate,lastPeriod,lastCategory,address,bottomSection,status,applied_at));
                                }


                                setAdapter(leave_details);


                            } else {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                            }



                        } else {

                            Toast.makeText(context, "Data not fatched", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                }
            });

        } else {
            Toast.makeText(context, getResources().getString(R.string.please_check_your_connection), Toast.LENGTH_SHORT).show();
        }

    }

    private void setAdapter(ArrayList<Leave_Detail> leave_details) {
        leaveAdapter=new LeaveAdapter(context,leave_details, new OnRecyler() {
            @Override
            public void onClick(String s, int pos) {

            }

            @Override
            public void onLongClick(String s, int pos) {

            }
        });

        customLinearRecyclerview.setAdapter(leaveAdapter);

        runAnimationAgain();

    }

    private void intialise() {
        context=this;
        customLinearRecyclerview=findViewById(R.id.customLinearRecyclerview);
        customLinearRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        iv_back=findViewById(R.id.iv_back);
    }

    private void runAnimationAgain() {


        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.gridlayout_animation_from_bottom);

        customLinearRecyclerview.setLayoutAnimation(controller);
        leaveAdapter.notifyDataSetChanged();
        customLinearRecyclerview.scheduleLayoutAnimation();

    }

}
