package com.wts.mytask.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.wts.mytask.R;
import com.wts.mytask.modelclass.WebServiceInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText tietusername, tietpassword;

    Button login_button;
    TextView register_textview, forgetpassword_textview;

    String deviceId;
    String Susername, Spassword;
    WebServiceInterface webServiceInterface;
    String userid, ownername, usernameresponse, usertype, mobileno, pancard, dpimg, aadharcard, status, deviceidresponse, devicestatus, outledid;

    AlertDialog alertDialog;
    TextInputEditText tietfusername, tietfmobile;
    Button freset;
    TextView fcancel;
    String Sfusername, Sfmobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tietusername = findViewById(R.id.login_emailedittext);
        tietpassword = findViewById(R.id.login_passwordedittext);
        login_button = findViewById(R.id.login_submitButton);
        register_textview = findViewById(R.id.login_textregister);
        forgetpassword_textview = findViewById(R.id.login_textforgetpassword);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.logingradientstartcolor));
        }  //change statusbar color .......................

        deviceId = getDeviceId(LoginActivity.this);
        webServiceInterface = WebServiceInterface.retrofit.create(WebServiceInterface.class);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkInternetState()) {
                    if (checkInputs()) {
                        logIn();
                    }
                } else {
                    showSnackBar();
                }
            }
        });

        forgetpassword_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternetState()) {
                    forgetPassword();
                } else {
                    showSnackBar();
                }
            }
        });

        register_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });

    }

    public void logIn() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Logging in");
        pd.setMessage("Please wait while logging in");
        pd.setCancelable(false);
        pd.show();

        Susername = tietusername.getText().toString().trim();
        Spassword = tietpassword.getText().toString().trim();

        if (deviceId == null) {
            pd.dismiss();
            deviceId = getDeviceId(this);
        } else {
            Call<JsonObject> call = webServiceInterface.login(Susername, Spassword, deviceId);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));

                            String responsecode = jsonObject.getString("response_code");
                            if (responsecode.equalsIgnoreCase("TXN")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("transactions");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    userid = jsonObject1.getString("userid");
                                    ownername = jsonObject1.getString("ownername");
                                    usernameresponse = jsonObject1.getString("username");
                                    usertype = jsonObject1.getString("usertype");
                                    mobileno = jsonObject1.getString("mobileno");
                                    pancard = jsonObject1.getString("pancard");
                                    dpimg = jsonObject1.getString("dpimg");
                                    aadharcard = jsonObject1.getString("aadharcard");
                                    status = jsonObject1.getString("status");
                                    deviceidresponse = jsonObject1.getString("deviceid");
                                    devicestatus = jsonObject1.getString("devicestatus");
                                    outledid = jsonObject1.getString("OutletId");

                                }

                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("userID", userid);
                                editor.putString("ownerName", ownername);
                                editor.putString("userNameResponse", usernameresponse);
                                editor.putString("userType", usertype);
                                editor.putString("mobileNo", mobileno);
                                editor.putString("panCard", pancard);
                                editor.putString("dpimg", dpimg);
                                editor.putString("aadharCard", aadharcard);
                                editor.putString("status", status);
                                editor.putString("deviceIdResponse", deviceidresponse);
                                editor.putString("deviceStatus", devicestatus);
                                editor.putString("outletId", outledid);
                                editor.apply();

                                pd.dismiss();

                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                finish();

                            } else if (responsecode.equalsIgnoreCase("ERR")) {
                                pd.dismiss();
                                String errorMessage = jsonObject.getString("transactions");
                                String errorTitle = jsonObject.getString("response_msg");

                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle(errorTitle)
                                        .setMessage(errorMessage).setPositiveButton("Ok", null)
                                        .show();
                            } else {
                                pd.dismiss();

                                new AlertDialog.Builder(LoginActivity.this).setTitle("Error")
                                        .setMessage("Something went wrong")
                                        .setPositiveButton("Ok", null).show();
                            }

                        } catch (JSONException e) {
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {

                        new AlertDialog.Builder(LoginActivity.this).setMessage("Something went wrong!!!")
                                .setPositiveButton("Ok", null)
                                .show();
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    pd.dismiss();
                    new AlertDialog.Builder(LoginActivity.this).setMessage(t.getMessage())
                            .setTitle("Alert")
                            .setPositiveButton("Ok", null).show();
                }
            });
        }

    }

    public void forgetPassword() {
        View v = LayoutInflater.from(LoginActivity.this).inflate(R.layout.forgetpassword_dialog, null, false);
        tietfusername = v.findViewById(R.id.forgetpassword_usernameedittext);
        tietfmobile = v.findViewById(R.id.forgetpassword_mobileedittext);
        freset = v.findViewById(R.id.forgetpassword_resetButton);
        fcancel = v.findViewById(R.id.forgetpassword_canceltv);

        alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.setView(v);

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

        freset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (forgetpasswordCheckInputs()) {
                    Sfusername = tietfusername.getText().toString().trim();
                    Sfmobile = tietfmobile.getText().toString().trim();
                    forgetPasswordRecover(Sfusername, Sfmobile);
                }
            }
        });
        fcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void forgetPasswordRecover(String fusername, String fmobile) {
        Call<JsonObject> call = webServiceInterface.forgetpassword(fusername, fmobile);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                        String response_message = jsonObject.getString("response_msg");

                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Status")
                                .setMessage(response_message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        alertDialog.dismiss();
                                    }
                                }).show();


                    } catch (JSONException e) {
                        e.printStackTrace();

                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Alert")
                            .setMessage("Something went wrong")
                            .setPositiveButton("Ok", null).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                new AlertDialog.Builder(LoginActivity.this).setMessage(t.getMessage())
                        .setTitle("Alert")
                        .setPositiveButton("Ok", null).show();
            }
        });
    }


    private boolean checkInputs() {
        if (!TextUtils.isEmpty(tietusername.getText())) {
            if (!TextUtils.isEmpty(tietpassword.getText())) {

                return true;
            } else {
                tietpassword.setError("Password can't be empty.");
                return false;
            }
        } else {
            tietusername.setError("User Name can't be empty.");
            return false;
        }
    }

    public boolean forgetpasswordCheckInputs() {
        if (!TextUtils.isEmpty(tietfusername.getText())) {
            if (!TextUtils.isEmpty(tietfmobile.getText())) {
                return true;
            }
            else
                {
                tietfmobile.setError("Please Enter MobileNo");
                return false;
            }
        }
        else
            {
            tietfusername.setError("Please Enter UserName");
            return false;
        }

    }

    public String getDeviceId(Context context) {


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                return null;
            }
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }


    private void showSnackBar() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.loginLayoutScrollview), "No Internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private boolean checkInternetState() {

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }
}