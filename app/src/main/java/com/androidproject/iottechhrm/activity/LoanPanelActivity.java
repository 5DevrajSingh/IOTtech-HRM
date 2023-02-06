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

public class LoanPanelActivity extends BaseActivity {

    RelativeLayout rl_loan_detail,rl_instalment;
    ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_loan_panel);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        hideToolbar();

        intialise();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rl_loan_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoanPanelActivity.this,LoanDetailActivity.class));
            }
        });

        rl_instalment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoanPanelActivity.this,LoanInstalmentActivity.class));
            }
        });

    }

    private void intialise() {
        rl_loan_detail=findViewById(R.id.rl_loan_detail);
        rl_instalment=findViewById(R.id.rl_instalment);
        iv_back=findViewById(R.id.iv_back);
    }
}
