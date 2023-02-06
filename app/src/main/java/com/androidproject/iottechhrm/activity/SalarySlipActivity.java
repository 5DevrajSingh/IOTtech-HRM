package com.androidproject.iottechhrm.activity;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PdfPrint;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.monthpicker.DateMonthDialogListener;
import com.androidproject.iottechhrm.monthpicker.MonthType;
import com.androidproject.iottechhrm.monthpicker.OnCancelMonthDialogListener;
import com.androidproject.iottechhrm.monthpicker.RackMonthPicker;
import com.androidproject.iottechhrm.retro.ApiClient;
import com.androidproject.iottechhrm.util.BaseActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.Arrays;

public class SalarySlipActivity extends BaseActivity {


    ImageView iv_back;
    SharedPreferences pref;
    String id;
    RelativeLayout rl_from_date;
    RackMonthPicker rackMonthPicker;
    TextInputLayout textinput_dob;
    EditText et_dob;

    WebView webView;
    boolean progressCheck;
    String check;
    private boolean allowSave = true;
    private TextView textView;
    private int PERMISSION_REQUEST = 0;
    String link,invoice_no;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_salary_slip);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        hideToolbar();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        id = pref.getString("id", null);

        intialise();

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rackMonthPicker.show();
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        rackMonthPicker = new RackMonthPicker(this)
                .setMonthType(MonthType.TEXT)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
//                        System.out.println(month);
//                        System.out.println(startDate);
//                        System.out.println(endDate);
//                        System.out.println(year);
//                        System.out.println(monthLabel);

                        String m;

                        et_dob.setText(monthLabel);

                        showProgressBar();
                        progressCheck = true;
                        if (month<10)
                        {
                            m="0"+month;
                        }
                        else
                        {
                            m= String.valueOf(month);
                        }
//                        Log.i("bdcb",ApiClient.BASE_URL+"salarySlip/"+id+"/"+year+"-"+m);
                        webView.loadUrl(ApiClient.BASE_URL+"salarySlip/"+id+"/"+year+"-"+m);

                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });


                webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(SalarySlipActivity.this, "Oh no! " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           final SslErrorHandler handler, SslError error) {
//                Toast.makeText(VendorInVoiceActivity.this, "SSL Error! " + error, Toast.LENGTH_SHORT).show();
//                handler.proceed();

                final AlertDialog.Builder builder = new AlertDialog.Builder(SalarySlipActivity.this);
                builder.setMessage(R.string.notification_error_ssl_cert_invalid);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (progressCheck) {
                    hideProgressBar();
                    progressCheck = false;
                }

                findViewById(R.id.fab).setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }
        });

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(2);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);
//        webView.getSettings().setBuiltInZoomControls(false);




    }

    private void intialise() {
        iv_back=findViewById(R.id.iv_back);
        rl_from_date=findViewById(R.id.rl_from_date);
        textinput_dob=findViewById(R.id.textinput_dob);
        et_dob=findViewById(R.id.et_dob);
        textView = findViewById(R.id.textView);
        webView = (WebView) findViewById(R.id.webView1);
    }


    public void printPDF(View view)
    {
        savePdf();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void savePdf() {
        if(!allowSave)
            return;
        allowSave = false;
        textView.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PERMISSION_GRANTED) {
//            String fileName = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
            String fileName = String.format("%s.pdf", getResources().getString(R.string.app_name)+" "+invoice_no);
            final PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(fileName);
            PrintAttributes printAttributes = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A3)
                    .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                    .build();
            final File file = Environment.getExternalStorageDirectory();
            new PdfPrint(printAttributes).print(
                    printAdapter,
                    file,
                    fileName,
                    new PdfPrint.CallbackPrint() {
                        @Override
                        public void onSuccess(String path) {
                            textView.setVisibility(View.GONE);
                            allowSave = true;
                            Toast.makeText(getApplicationContext(),
                                    String.format(getResources().getString(R.string.Your_file_is_saved_in)+" %s", path),
                                    Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Exception ex) {
                            textView.setVisibility(View.GONE);
                            allowSave = true;
                            Toast.makeText(getApplicationContext(),
                                    String.format("Exception while saving the file and the exception is %s", ex.getMessage()),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults[Arrays.asList(permissions).indexOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)] == PERMISSION_GRANTED) {
                savePdf();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
