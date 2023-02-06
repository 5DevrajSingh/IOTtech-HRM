package com.androidproject.iottechhrm.activity;

import static android.Manifest.*;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.retro.ApiClient;
import com.androidproject.iottechhrm.retro.ApiInterface;
import com.androidproject.iottechhrm.util.BaseActivity;
import com.androidproject.iottechhrm.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    int mYear, mMonth, mDay;
    String month = "";
    String day = "";
    String format = "";
    EditText et_dob, et_name, et_email, et_contact, et_current, et_permanent;
    ImageView iv_dob;
    CircleImageView owner_pic;
    TextInputLayout textlayout_name, textlayout_email, textlayout_contact, textinput_dob, textlayout_current, textlayout_permanent;
    Spinner spin_gender;
    TextView btn_submit;
//    String[] genders = {"Select Gender", "Male", "Female"};
    List<String> genders;
    String gender;

    String id;
    SharedPreferences pref;

    Context context;

    private static final int HELP_OWNER_FAMILY = 003;
    private static final int PICK_FROM_GALLERY = 003;
    private static final int HELP_CAMERA_FAMILY_IMAGE = 004;

    String ownerImage;

    MultipartBody.Part multiOwnerImage;

    String p_address, c_address, email, name, dob, contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_profile);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_700));
        hideToolbar();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        id = pref.getString("id", null);

        intialise();

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
                updateProfileActivity();
            }
        });

        spin_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                gender =String.valueOf( parent.getItemAtPosition(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        owner_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpSelectUserImage();
            }
        });





    }



    private void updateProfileActivity() {
        if (checkProfileValidation()) {
            addProfileApi();
        }
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

//        email = et_email.getText().toString();
//        if (TextUtils.isEmpty(email)) {
//            textlayout_email.setError(getResources().getString(R.string.please_required));
//            textlayout_email.requestFocus();
//            check = false;
//        }

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
    private void addProfileApi() {
        Call<String> stringCall = null;
        if (Utility.isNetworkAvailable(context)) {
            showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            stringCall = apiInterface.updateProfile(id, name, email, contact, p_address,c_address,gender,dob,multiOwnerImage);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
//                    Log.i("dsvsvd",sResponse);
                    hideProgressBar();
                    try {
                        if (!TextUtils.isEmpty(sResponse)) {
                            JSONObject jsonObject = new JSONObject(sResponse);
                            String result = jsonObject.getString("type");
                            String message = jsonObject.getString("message");
                            if (result.equalsIgnoreCase("success")) {

                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                String id=jsonObject1.getString("id");
                                String name=jsonObject1.getString("name");
                                et_name.setText(name);
                                String avtar=jsonObject1.getString("avtar");

                                Picasso.get()
                                        .load(avtar)
                                        .into(owner_pic);

                                String email=jsonObject1.getString("email");
                                et_email.setText(email);
                                String contact=jsonObject1.getString("contact");
                                et_contact.setText(contact);
                                gender=jsonObject1.getString("gender");
//                                spin_gender.setSelection(getPositionGender(gender));
                                String c_address=jsonObject1.getString("c_address");
                                et_current.setText(c_address);
                                String p_address=jsonObject1.getString("p_address");
                                et_permanent.setText(p_address);
                                String dob=jsonObject1.getString("dob");
                                et_dob.setText(dob);
                                ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, genders);
                                spin_gender.setAdapter(classAdapter);
                                spin_gender.setSelection(genders.indexOf(jsonObject1.getString("gender")));
                                Toast.makeText(context, getResources().getString(R.string.updated_successfully), Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                            }



                        } else {

                            Toast.makeText(context, "update response failed", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
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
    private void hitProfileGetApi() {
        Call<String> stringCall = null;
            showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("id",id).build();
            stringCall = apiInterface.getProfile(body);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
                    System.out.println("UserDetails....  "+sResponse);
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
//                                et_name.setEnabled(false);
                                String avtar = jsonObject1.getString("avtar");
                                RequestOptions requestOptionowner = new RequestOptions();
                                requestOptionowner.placeholder(R.drawable.avtar);
                                requestOptionowner.error(R.drawable.avtar);

                                Glide.with(context)
                                        .setDefaultRequestOptions(requestOptionowner)
                                        .load(avtar).into(owner_pic);

//                                Glide.with(context).load(avtar).into(owner_pic);

                                String email = jsonObject1.getString("email");
                                et_email.setText(email);
                                et_email.setEnabled(false);
                                String contact = jsonObject1.getString("contact");
                                et_contact.setText(contact);
                                gender = jsonObject1.getString("gender");
//                                spin_gender.setSelection(getPositionGender(gender));
//                                spin_gender.setEnabled(false);
                                String c_address = jsonObject1.getString("c_address");
                                et_current.setText(c_address);
                                String p_address = jsonObject1.getString("p_address");
                                et_permanent.setText(p_address);
                                String dob = jsonObject1.getString("dob");
                                et_dob.setText(dob);
                                ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, genders);
                                spin_gender.setAdapter(classAdapter);
                                spin_gender.setSelection(genders.indexOf(jsonObject1.getString("gender")));
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

    private void intialise() {
        context = ProfileActivity.this;
        et_dob = findViewById(R.id.et_dob);
        iv_dob = findViewById(R.id.iv_dob);
        owner_pic = findViewById(R.id.owner_pic);
        spin_gender = findViewById(R.id.spin_gender);
        btn_submit = findViewById(R.id.btn_submit);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_contact = findViewById(R.id.et_contact);
        et_current = findViewById(R.id.et_current);
        et_permanent = findViewById(R.id.et_permanent);

        textlayout_email = findViewById(R.id.textlayout_email);

        textinput_dob = findViewById(R.id.textinput_dob);
        genders = new ArrayList<>();
        genders.add("Select Gender");
        genders.add("Male");
        genders.add("Female");

    }


    private void datePicker(EditText edittext, int i) {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this,
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

    private void helpSelectUserImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), "com.androidproject.ezybazaar",f));
                    startActivityForResult(intent, HELP_CAMERA_FAMILY_IMAGE);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, HELP_OWNER_FAMILY);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if ((requestCode == HELP_OWNER_FAMILY && resultCode == RESULT_OK && data != null && data.getData() != null)) {

            Uri uri = data.getData();
            String filePath = getRealPathFromURIPath(uri, ProfileActivity.this);

//            Log.i("sacscdqq",im1);
            File file = new File(filePath);
            ownerImage = file.getName();
//            Toast.makeText(this, ""+userImageName, Toast.LENGTH_SHORT).show();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                Glide.with(context)
                        .load(bitmap)
                        .into(owner_pic);
//                iv1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            multiOwnerImage = MultipartBody.Part.createFormData("avtar", file.getName(), mFile);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());


        }


        if (requestCode == HELP_CAMERA_FAMILY_IMAGE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Glide.with(context)
                    .load(photo)
                    .into(owner_pic);


            Uri uri = getImageUri(context, photo);

            String filePath = getRealPathFromURIPath(uri, ProfileActivity.this);
//            Log.i("sacscdqq",im1);
            File file = new File(filePath);

            ownerImage = file.getName();

//            Toast.makeText(this, ""+userImageName, Toast.LENGTH_SHORT).show();


            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            multiOwnerImage = MultipartBody.Part.createFormData("avtar", file.getName(), mFile);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());


        }

    }

    public String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "BookMySecurity" + String.valueOf(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
        startActivity(intent);
        finish();
    }
}
