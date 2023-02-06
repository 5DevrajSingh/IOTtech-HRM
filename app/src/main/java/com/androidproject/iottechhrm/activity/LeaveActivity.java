package com.androidproject.iottechhrm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.util.BaseActivity;

public class LeaveActivity extends BaseActivity {
    ImageView iv_back,iv_detail;
    RelativeLayout rl_apply,rl_detail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.actvity_leave);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        hideToolbar();

        intialise();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rl_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LeaveActivity.this,CreateLeaveActivity.class));
            }
        });

        iv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LeaveActivity.this,LeaveDatailActivity.class));
            }
        });
    }

    private void intialise() {
        iv_back=findViewById(R.id.iv_back);
        rl_apply=findViewById(R.id.rl_apply);
        rl_detail=findViewById(R.id.rl_detail);
        iv_detail=findViewById(R.id.iv_detail);
    }
}
