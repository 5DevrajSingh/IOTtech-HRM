package com.androidproject.iottechhrm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.util.BaseActivity;

public class AttendancePanelActivity extends BaseActivity {
    
    RelativeLayout rl_attendance_detail,rl_overtime_detail;
    ImageView iv_back;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_attendance_panel);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        hideToolbar();
        
        intialise();

        rl_attendance_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AttendancePanelActivity.this,AttendaceActivity.class));
            }
        });

        rl_overtime_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AttendancePanelActivity.this,OverTimeActivity.class));
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        
    }

    private void intialise() {
        rl_attendance_detail=findViewById(R.id.rl_attendance_detail);
        rl_overtime_detail=findViewById(R.id.rl_overtime_detail);
        iv_back=findViewById(R.id.iv_back);
    }
}
