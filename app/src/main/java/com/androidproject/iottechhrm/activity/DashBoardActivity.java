package com.androidproject.iottechhrm.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.androidproject.iottechhrm.DemoActivity.UserProfileActivity;
import com.androidproject.iottechhrm.login.LoginActivity;
import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.adapter.DashboardAdapter;
import com.androidproject.iottechhrm.inter.OnRecyler;
import com.androidproject.iottechhrm.model.Dash_List;
import com.androidproject.iottechhrm.retro.ApiClient;
import com.androidproject.iottechhrm.retro.ApiInterface;
import com.androidproject.iottechhrm.util.BaseActivity;
import com.androidproject.iottechhrm.util.CustomGridRecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardActivity extends BaseActivity {

    String name,s_img,s_email,designation;
    SharedPreferences pref;
    TextView tv_userName,tv_designation,tv_Uemail;
    CustomGridRecyclerView recyclerView;
    List<Dash_List> myLists;
    String id;
    Context context;
    DashboardAdapter dashboardAdapter;
    ImageView iv_setting,profile_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_dashboard);

        context = DashBoardActivity.this;

        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        hideToolbar();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        name = pref.getString("name", null);
        s_img = pref.getString("avtar",null);
        s_email = pref.getString("email",null);
        designation = pref.getString("designation",null);
        id = pref.getString("id",null);
        hitProfileGetApi(id);

        intialise();

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);


        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logoutDialog();

            }
        });

    }

    private void hitProfileGetApi(String _id) {
        Call<String> stringCall = null;
        showProgressBar();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("id",id).build();
        stringCall = apiInterface.getProfile(body);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String sResponse = response.body();
                System.out.println("user_Details:.....  "+sResponse);
                hideProgressBar();

                if (!TextUtils.isEmpty(sResponse)) {

                    try {
                        JSONObject jsonObject = new JSONObject(sResponse);
                        String type = jsonObject.getString("type");
                        String message = jsonObject.getString("message");
                        if (type.equalsIgnoreCase("success")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String id = jsonObject1.getString("id");
                            String name = jsonObject1.getString("name");
                            String designation = jsonObject1.getString("designation");
                            tv_userName.setText(name);

                            String avtar = jsonObject1.getString("avtar");
                            RequestOptions requestOptionowner = new RequestOptions();
                            requestOptionowner.placeholder(R.drawable.avtar);
                            requestOptionowner.error(R.drawable.avtar);

                            Glide.with(context)
                                    .setDefaultRequestOptions(requestOptionowner)
                                    .load(avtar).into(profile_img);
//                            Picasso.get()
//                                    .load(avtar)
//                                    .into(profile_img);
                            String email = jsonObject1.getString("email");
                            tv_Uemail.setText(email);
                            String contact = jsonObject1.getString("contact");
                            tv_designation.setText(designation);
                            String gender = jsonObject1.getString("gender");
                            String c_address = jsonObject1.getString("c_address");
                            String p_address = jsonObject1.getString("p_address");
                            String dob = jsonObject1.getString("dob");

                        } else {
                            Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                    }
                } else {
                    Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        });



    }



    private void logoutDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.setCancelable(false);
        TextView yesButton = (TextView) dialog.findViewById(R.id.tv_yes);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                Toast.makeText(DashBoardActivity.this, getResources().getString(R.string.Logout_Sucessfully), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });
        TextView noButton = (TextView) dialog.findViewById(R.id.tv_no);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void intialise() {
        tv_userName=findViewById(R.id.tv_wish);
        tv_designation=findViewById(R.id.tv_designation);
        tv_Uemail=findViewById(R.id.tv_usermail);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        myLists=new ArrayList<>();
        getdata();
        iv_setting=findViewById(R.id.iv_setting);
        profile_img=findViewById(R.id.profile_img);
    }

    private void getdata() {
        myLists.add(new Dash_List(R.drawable.profile,"Profile"));
        myLists.add(new Dash_List(R.drawable.attendance,"Attendance"));
        myLists.add(new Dash_List(R.drawable.salary,"Salary"));
        myLists.add(new Dash_List(R.drawable.leave,"Leave"));
        myLists.add(new Dash_List(R.drawable.loan_and_advance,"Loan & Advance"));
//        myLists.add(new Dash_List(R.drawable.info));
//        myLists.add(new Dash_List(R.drawable.calendar));
//        myLists.add(new Dash_List(R.drawable.notes));

        setAdapter();


    }

    private void setAdapter() {

        dashboardAdapter=new DashboardAdapter(this,myLists, new OnRecyler() {
            @Override
            public void onClick(String name, int pos) {

                if (name.equalsIgnoreCase("Profile"))
                {
                    startActivity(new Intent(DashBoardActivity.this, ProfileActivity.class));
                    finish();
                }
                if (name.equalsIgnoreCase("Leave"))
                {
                    startActivity(new Intent(DashBoardActivity.this,LeaveActivity.class));
                }
                if (name.equalsIgnoreCase("Attendance"))
                {
                    startActivity(new Intent(DashBoardActivity.this,AttendancePanelActivity.class));
                }
                if (name.equalsIgnoreCase("Salary"))
                {
                    startActivity(new Intent(DashBoardActivity.this,SalaryPanelActivity.class));
                }
                if (name.equalsIgnoreCase("Loan & Advance"))
                {
                    startActivity(new Intent(DashBoardActivity.this,LoanPanelActivity.class));
                }

            }

            @Override
            public void onLongClick(String s, int pos) {

            }
        });
        recyclerView.setAdapter(dashboardAdapter);

        runAnimationAgain();

    }

    private void runAnimationAgain() {


        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(DashBoardActivity.this, R.anim.gridlayout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        dashboardAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.exit_title);
        dialog.setMessage(R.string.exit_message);
        dialog.setIcon(R.drawable.hrm_logo);
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
}
