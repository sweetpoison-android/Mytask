package com.wts.mytask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.wts.mytask.modelclass.MyWebserviceController;
import com.wts.mytask.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditDevitBalanceActivity extends AppCompatActivity {

    String creditDebitBalance;
    String id, userName, ownerName;
    String userid;
    String amount, remarks;

    SharedPreferences shp;

    ArrayList<String> idArrayList = new ArrayList<>();
    ArrayList<String> ownerArrayList = new ArrayList<>();
    ArrayList<String> userNameArrayList = new ArrayList<>();

    ProgressDialog pd;
    AlertDialog.Builder bld;

    ImageView profileimage, backbutton, downArrowImage;
    TextView creditDebitText, cancelText;
    Button proceed;

    TextInputEditText tietAmount, tietRemark;
    AutoCompleteTextView autoCompleteTextView;

    String selectedId;
    String selectedUsername = "Select User";

    Call<JsonObject> call;

    boolean isSuccessful = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_devit_balance);

        initViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.logingradientstartcolor));
        }

        creditDebitBalance = getIntent().getStringExtra("creditDebitBalance");
        creditDebitText.setText(creditDebitBalance);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreditDevitBalanceActivity.this, DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreditDevitBalanceActivity.this, DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });

        if (creditDebitBalance.equalsIgnoreCase("Credit Balance")) {
            profileimage.setImageResource(R.drawable.credit);
        }
        else if (creditDebitBalance.equalsIgnoreCase("Debit Balance")) {
            profileimage.setImageResource(R.drawable.debit);
        }

        shp = PreferenceManager.getDefaultSharedPreferences(CreditDevitBalanceActivity.this);
        userid = shp.getString("userID", null);

        getusers(userid);

        // autoCompleteTextView.setThreshold(1);

        autoCompleteTextView.setHint(selectedUsername);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userNameArrayList);
        autoCompleteTextView.setAdapter(adapter);

        downArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showDropDown();

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedId = idArrayList.get(i).toString();
                selectedUsername = userNameArrayList.get(i).toString();

                Toast.makeText(CreditDevitBalanceActivity.this, selectedUsername + "\n" + selectedId, Toast.LENGTH_SHORT).show();

            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSuccessful) {
                    if (checkInternetState()) {
                        if (checkInputs()) {
                            checkCreditDebitBalance();
                        } else {

                        }
                    }
                }
            }
        });

    }

    public void checkCreditDebitBalance() {

        amount = tietAmount.getText().toString().trim();
        remarks = tietRemark.getText().toString().trim();

        if (creditDebitBalance.equalsIgnoreCase("Credit Balance")) {
            call = MyWebserviceController.getInstance().getApi().creditbalance(userid, remarks, amount, selectedId);
            doCreditDebit();
        }
        else if (creditDebitBalance.equalsIgnoreCase("Debit Balance"))
        {
            call = MyWebserviceController.getInstance().getApi().debitbalance(userid, remarks, amount, selectedId);
            doCreditDebit();
        }
    }

    public void doCreditDebit() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pd.setCancelable(false);
        pd.show();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                        String responseCode = jsonObject.getString("response_code");
                        String responseMessage = jsonObject.getString("response_msg");
                        String transactions = jsonObject.getString("transactions");

                        if (responseCode.equalsIgnoreCase("TXN")) {

                            //creditDebitBalanceDialog();
                            pd.dismiss();

                            View v = LayoutInflater.from(CreditDevitBalanceActivity.this).inflate(R.layout.recharge_status_dialog, null, false);
                          AlertDialog.Builder  bld1 = new AlertDialog.Builder(CreditDevitBalanceActivity.this);
                            bld1.setView(v);
                            bld1.setCancelable(false);
                           Dialog dialog1 = bld1.create();
                            dialog1.show();


                           ImageView img = v.findViewById(R.id.rechargeStatus_profileimage);
                           TextView titleTv = v.findViewById(R.id.rechargeStatus_rechargeStatusText);
                           TextView numberTv = v.findViewById(R.id.rechargeStatus_numberText);
                           TextView amountTv = v.findViewById(R.id.rechargeStatus_amountText);
                           TextView statusTv = v.findViewById(R.id.rechargeStatus_statusText);
                           Button ok = v.findViewById(R.id.rechargeStatus_okButton);

                            img.setImageResource(R.drawable.success);
                            titleTv.setText(creditDebitBalance);
                            numberTv.setVisibility(View.GONE);
                            statusTv.setText("Status : " + responseMessage);
                            statusTv.setTextColor(Color.parseColor("#00A300"));
                            amountTv.setText("Balance : " + transactions);

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog1.dismiss();
                                }
                            });



                        }
                        else if (responseCode.equalsIgnoreCase("ERR")) {

                            String status = jsonObject.getString("transactions");

                           // creditDebitBalanceDialog();
                            pd.dismiss();

                            View v = LayoutInflater.from(CreditDevitBalanceActivity.this).inflate(R.layout.recharge_status_dialog, null, false);
                           AlertDialog.Builder bld2 = new AlertDialog.Builder(CreditDevitBalanceActivity.this);
                            bld2.setView(v);
                            bld2.setCancelable(false);
                           Dialog dialog2 = bld2.create();
                            dialog2.show();

                            ImageView img = v.findViewById(R.id.rechargeStatus_profileimage);
                            TextView titleTv = v.findViewById(R.id.rechargeStatus_rechargeStatusText);
                            TextView numberTv = v.findViewById(R.id.rechargeStatus_numberText);
                            TextView amountTv = v.findViewById(R.id.rechargeStatus_amountText);
                            TextView statusTv = v.findViewById(R.id.rechargeStatus_statusText);
                            Button ok = v.findViewById(R.id.rechargeStatus_okButton);

                            img.setImageResource(R.drawable.cancel);
                            titleTv.setText(creditDebitBalance);
                            numberTv.setVisibility(View.GONE);
                            amountTv.setVisibility(View.GONE);
                            statusTv.setText("Status : " + status);
                            statusTv.setTextColor(Color.parseColor("#C70039"));

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog2.dismiss();
                                }
                            });


                        }

                        else {
                            pd.dismiss();
                            new androidx.appcompat.app.AlertDialog.Builder(CreditDevitBalanceActivity.this)
                                    .setTitle("Alert")
                                    .setMessage("Something went wrong")
                                    .setPositiveButton("Ok", null).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        new AlertDialog.Builder(CreditDevitBalanceActivity.this)
                                .setMessage(e.getMessage().toString())
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                new AlertDialog.Builder(CreditDevitBalanceActivity.this)
                        .setMessage(t.getMessage().toString())
                        .setPositiveButton("OK", null)
                        .show();
            }
        });


    }


    public void getusers(String userid) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pd.setCancelable(false);
        pd.show();

        Call<JsonObject> call = MyWebserviceController.getInstance().getApi().getusers(userid);
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
                                id = jsonObject1.getString("id");
                                userName = jsonObject1.getString("Username");
                                ownerName = jsonObject1.getString("OwnerName");

                                idArrayList.add(id);
                                ownerArrayList.add(ownerName);
                                userNameArrayList.add(userName);

                            }
                            pd.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    isSuccessful = true;
                } else {
                    pd.dismiss();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                pd.dismiss();
            }
        });


    }

    public void initViews() {
        profileimage = findViewById(R.id.creditDebitBalance_profileimage);
        backbutton = findViewById(R.id.creditDebitBalance_backbutton);
        creditDebitText = findViewById(R.id.creditDebitBalance_textview);
        tietAmount = findViewById(R.id.creditDebitBalance_amountEdittext);
        tietRemark = findViewById(R.id.creditDebitBalance_remarkEdittext);
        autoCompleteTextView = findViewById(R.id.creditDebitBalance_autocompletetextview);
        downArrowImage = findViewById(R.id.creditDebitBalance_downarrowimageview);
        proceed = findViewById(R.id.creditDebitBalance_proceedButton);
        cancelText = findViewById(R.id.creditDebitBalance_textviewCancel);
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

    private boolean checkInputs() {
        if (!TextUtils.isEmpty(tietAmount.getText())) {
            if (!TextUtils.isEmpty(tietRemark.getText())) {
                if (!selectedUsername.equalsIgnoreCase("Select User")) {
                    return true;
                } else {
                    new androidx.appcompat.app.AlertDialog.Builder(CreditDevitBalanceActivity.this).setMessage("Select User")
                            .setPositiveButton("Ok", null).show();
                    return false;
                }
            } else {
                tietRemark.setError("Add Remark.");
                return false;
            }
        } else {
            tietAmount.setError("Enter Amount.");
            return false;
        }
    }
}


