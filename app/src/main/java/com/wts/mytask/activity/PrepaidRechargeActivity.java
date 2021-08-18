package com.wts.mytask.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.wts.mytask.R;
import com.wts.mytask.modelclass.WebServiceInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrepaidRechargeActivity extends AppCompatActivity {

    ImageView profileImg;
    TextView tv_title;
    TextInputEditText tietNumber, tietAmount;
    AutoCompleteTextView autoCompleteTextView;
    ImageView autocompleteimage, backbutton;
    Button rechargeButton;
    TextView tv_cancel;

    ArrayList<String> operatorNameList = new ArrayList<>();
    ArrayList<String> operatorIdList = new ArrayList<>();


    String title, userid, serviceName;
    String serviceId, serviceNameResponse;
    String operatorId, operatorName;

    SharedPreferences shp;

    WebServiceInterface webServiceInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepaid_recharge);

        initViews();

        // .............status bar colcor change...............................

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.logingradientstartcolor));
        }

//////////////////////////////////////////////////////////////////////


        webServiceInterface = WebServiceInterface.retrofit.create(WebServiceInterface.class);

        title = getIntent().getStringExtra("title");
        tv_title.setText(title);
        serviceName = getIntent().getStringExtra("service");

        shp = PreferenceManager.getDefaultSharedPreferences(PrepaidRechargeActivity.this);
        userid = shp.getString("userID", null);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrepaidRechargeActivity.this, DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });

        rechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.recharge_status_dialog, null, false);
                AlertDialog.Builder bld = new AlertDialog.Builder(PrepaidRechargeActivity.this);
                bld.setView(v);


                Dialog dialog = bld.create();
                bld.show();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrepaidRechargeActivity.this, DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, operatorNameList);
        autoCompleteTextView.setAdapter(adapter);

        autocompleteimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showDropDown();
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Toast.makeText(PrepaidRechargeActivity.this, operatorNameList.get(i).toString() + "\n" + operatorIdList.get(i).toString(), Toast.LENGTH_SHORT).show();

            }
        });

        MyGetService(userid, serviceName);
        setViews(title);

    }

    public void MyGetService(String userid, String serviceName) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading ....");
        pd.setProgress(android.R.style.Widget_ProgressBar_Horizontal);
        pd.setCancelable(false);
        pd.show();

        Call<JsonObject> call = webServiceInterface.getService();
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
                                serviceId = jsonObject1.getString("serviceid");
                                serviceNameResponse = jsonObject1.getString("servicename");

                                if (serviceNameResponse.equalsIgnoreCase(serviceName)) {
                                    pd.dismiss();
                                    getOperators(serviceId);
                                    break;
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        pd.dismiss();
                        new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Alert")
                                .setMessage(e.getMessage())
                                .create();
                    }
                } else {
                    pd.dismiss();
                    new AlertDialog.Builder(getApplicationContext())
                            .setMessage("Alert")
                            .create();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                pd.dismiss();
                new AlertDialog.Builder(getApplicationContext())
                        .setMessage("Alert")
                        .create();
            }
        });

    }

    public void getOperators(String serviceid) {
//        ProgressDialog pd = new ProgressDialog(getApplicationContext());
//                pd.setMessage("Loading...");
//        pd.setProgress(android.R.style.Widget_ProgressBar_Horizontal);
//        pd.setCancelable(false);
//        pd.show();

        Call<JsonObject> call = webServiceInterface.getOperators(serviceid);
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
                                operatorId = jsonObject1.getString("id");
                                operatorName = jsonObject1.getString("OperatorName");

                                operatorNameList.add(operatorName);
                                operatorIdList.add(operatorId);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
//                    pd.dismiss();
//                    new AlertDialog.Builder(getApplicationContext())
//                            .setMessage("Error")
//                            .create();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                pd.dismiss();
//                new AlertDialog.Builder(getApplicationContext())
//                        .setMessage("Error")
//                        .create();
            }
        });
    }

    public void setViews(String title) {
        if (title.equalsIgnoreCase("Prepaid Recharge")) {
            profileImg.setImageResource(R.drawable.prepaid1);
            tietNumber.setHint("Enter Mobile Number");
            tietAmount.setHint("Enter Amount");

        } else if (title.equalsIgnoreCase("Postpaid Recharge")) {
            profileImg.setImageResource(R.drawable.postpaid1);
            tietNumber.setHint("Enter Mobile Number");
            tietAmount.setHint("Enter Amount");

        } else if (title.equalsIgnoreCase("DTH Recharge")) {
            profileImg.setImageResource(R.drawable.dth);
            tietNumber.setHint("Enter DTH Number");
            tietAmount.setHint("Enter Amount");

        }
    }

    public void initViews() {
        profileImg = findViewById(R.id.prepaidrecharge_profileimage);
        backbutton = findViewById(R.id.prepaidrecharge_backbutton);
        tv_title = findViewById(R.id.prepaidrecharge_textview);
        tietNumber = findViewById(R.id.prepaidrecharge_numberdittext);
        tietAmount = findViewById(R.id.prepaidrecharge_amountedittext);
        autoCompleteTextView = findViewById(R.id.prepaidrecharge_autocompletetextview);
        autocompleteimage = findViewById(R.id.prepaidrecharge_downarrowimageview);
        rechargeButton = findViewById(R.id.prepaidrecharge_rechargeButton);
        tv_cancel = findViewById(R.id.prepaidrecharge_textviewCancel);

    }

}