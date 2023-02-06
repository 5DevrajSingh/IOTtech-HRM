package com.androidproject.iottechhrm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.util.BaseActivity;

public class SalaryPanelActivity extends BaseActivity {
    RelativeLayout rl_salary_detail,rl_salary_slip,pf_details;
    Context context;
    ImageView iv_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_salary_panel);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        hideToolbar();

        intialise();

        rl_salary_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,SalaryDetailActivity.class));
            }
        });

        rl_salary_slip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,SalarySlipActivity.class));
            }
        });

        pf_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,PfActivity.class));
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void intialise()
    {
        context=this;
        rl_salary_detail=findViewById(R.id.rl_salary_detail);
        iv_back=findViewById(R.id.iv_back);
        rl_salary_slip=findViewById(R.id.rl_salary_slip);
        pf_details=findViewById(R.id.pf_details);
    }
}
