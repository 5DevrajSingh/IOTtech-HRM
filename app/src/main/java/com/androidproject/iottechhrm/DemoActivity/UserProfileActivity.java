package com.androidproject.iottechhrm.DemoActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.airbnb.lottie.utils.Utils;
import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.activity.ProfileActivity;
import com.androidproject.iottechhrm.retro.ApiClient;
import com.androidproject.iottechhrm.retro.ApiInterface;
import com.androidproject.iottechhrm.util.BaseActivity;
import com.androidproject.iottechhrm.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends BaseActivity {

    int mYear, mMonth, mDay;
    String month = "";
    String day = "";
    String format = "";
    EditText et_dob, et_name, et_email, et_contact, et_current, et_permanent;
    ImageView iv_dob;
    CircleImageView user_pic;
    TextInputLayout textlayout_name, textlayout_email, textlayout_contact, textinput_dob, textlayout_current, textlayout_permanent;
    Spinner spin_gender;
    TextView btn_submit;
    String[] genders = {"Select Gender", "Male", "Female"};
    String gender;
    String imageProfilePath;
    ImageView userImg;

    String id;
    SharedPreferences pref;

    Bitmap myBitmap;
    Uri picUri;
    private final static int PICK_IMAGE = 107;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private Uri outputFileUri;
    MultipartBody.Part multiImage;

    Context context;


    private static final int HELP_OWNER_FAMILY = 003;
    private static final int HELP_CAMERA_FAMILY_IMAGE = 004;

    String ownerImage;


    String p_address, c_address, email, name, dob, contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_user_profile);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        hideToolbar();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        id = pref.getString("id", null);
        intialise();
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(UserProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, genders);
        spin_gender.setAdapter(classAdapter);
        spin_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Typeface typeface = ResourcesCompat.getFont(UserProfileActivity.this, R.font.baumans);
                ((TextView) parent.getChildAt(0)).setTypeface(typeface);

                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                gender = genders[position];

//                Toast.makeText(getActivity(), ""+jobType, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        hitProfileGetApi();
        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePicker(et_dob, 1);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                UserProfileUpdateApi();
                if (checkProfileValidation()) {
//                    addProfileApi();
                    UserProfileUpdateApi();
                }
            }
        });
        user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
                Uri imageUri = data.getData();
                System.out.println("UserPic...   "+imageUri);
                user_pic.setImageURI(imageUri);
                try {
                    imageProfilePath = Utility.getPath(imageUri, UserProfileActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                File file = new File(imageProfilePath);
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                multiImage = MultipartBody.Part.createFormData("avtar", file.getName(), mFile);

            }


    }
    private void hitProfileGetApi() {
        Call<String> stringCall = null;
        showProgressBar();
        System.out.println("usre_Id  "+id);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("id",id).build();
        stringCall = apiInterface.getProfile(body);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String sResponse = response.body();
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
                            et_name.setText(name);

                            String avtar = jsonObject1.getString("avtar");
//                            RequestOptions requestOptionowner = new RequestOptions();
//                            requestOptionowner.placeholder(R.drawable.avtar);
//                            requestOptionowner.error(R.drawable.avtar);

//                            Glide.with(context).load(avtar).into(user_pic);


                            String email = jsonObject1.getString("email");
                            et_email.setText(email);
                            et_email.setEnabled(false);
                            String contact = jsonObject1.getString("contact");
                            et_contact.setText(contact);
                            String gender = jsonObject1.getString("gender");
                            spin_gender.setSelection(getPositionGender(gender));
                            spin_gender.setEnabled(false);
                            String c_address = jsonObject1.getString("c_address");
                            et_current.setText(c_address);
                            String p_address = jsonObject1.getString("p_address");
                            et_permanent.setText(p_address);
                            String dob = jsonObject1.getString("dob");
                            et_dob.setText(dob);
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
    private void UserProfileUpdateApi() {
        Call<String> stringCall = null;
        showProgressBar();
        System.out.println("usre_Id  "+id);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        stringCall = apiInterface.updateProfile(id,et_name.getText().toString(),et_email.getText().toString(),et_contact.getText().toString(),et_permanent.getText().toString(),et_current.getText().toString(),gender,et_dob.getText().toString(),multiImage);
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String sResponse = response.body();
                System.out.println("UserProfileUpdateRespons...   "+sResponse);
                hideProgressBar();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        });



    }
    private int getPositionGender(String gender) {

        int pos = 0;
        for (int i = 0; i < genders.length; i++) {
            if (gender.equals(genders[i])) {
                pos = i;
                return pos;
            }

        }
        return pos;

    }
    private boolean checkProfileValidation() {
        boolean check = true;

        name = et_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            textlayout_name.setError(getResources().getString(R.string.please_required));
            textlayout_name.requestFocus();
            check = false;
        }

        email = et_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            textlayout_email.setError(getResources().getString(R.string.please_required));
            textlayout_email.requestFocus();
            check = false;
        }

        contact = et_contact.getText().toString();
        if (!isValid(contact)) {
            textlayout_contact.setError(getResources().getString(R.string.Invalid_Number));
            textlayout_contact.requestFocus();
            check = false;
        }

        c_address = et_current.getText().toString();
        if (TextUtils.isEmpty(c_address)) {
            textlayout_current.setError(getResources().getString(R.string.please_required));
            textlayout_current.requestFocus();
            check = false;
        }

        p_address = et_permanent.getText().toString();
        if (TextUtils.isEmpty(p_address)) {
            textlayout_permanent.setError(getResources().getString(R.string.please_required));
            textlayout_permanent.requestFocus();
            check = false;
        }
        if (TextUtils.isEmpty(gender) || gender.equalsIgnoreCase("Select Gender")) {
            Toast.makeText(context, "please choose gender", Toast.LENGTH_SHORT).show();
            spin_gender.requestFocus();
            check = false;
        }

        dob = et_dob.getText().toString();
        if (TextUtils.isEmpty(dob)) {
            textinput_dob.setError(getResources().getString(R.string.please_required));
            textinput_dob.requestFocus();
            iv_dob.setVisibility(View.GONE);
            check = false;
        }


        return check;
    }
    private void intialise() {
        context = UserProfileActivity.this;
        et_dob = findViewById(R.id.et_dob);
        iv_dob = findViewById(R.id.iv_dob);
        user_pic = findViewById(R.id.user_pic);
        spin_gender = findViewById(R.id.spin_gender);
        btn_submit = findViewById(R.id.btn_submit);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_contact = findViewById(R.id.et_contact);
        et_current = findViewById(R.id.et_current);
        et_permanent = findViewById(R.id.et_permanent);
        textlayout_email = findViewById(R.id.textlayout_email);
        textinput_dob = findViewById(R.id.textinput_dob);

    }
    private void datePicker(EditText edittext, int i) {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(UserProfileActivity.this,
                new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        if (monthOfYear < 9) {
                            month = "0" + (monthOfYear + 1);
                        } else {
                            month = "" + (monthOfYear + 1);
                        }

                        if (dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        } else {

                            day = "" + dayOfMonth;
                        }


                        edittext.setText(year + "-" + month + "-" + day);
                        if (i == 1) {
                            edittext.setError(null);
                            iv_dob.setVisibility(View.VISIBLE);
                        }


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
}
