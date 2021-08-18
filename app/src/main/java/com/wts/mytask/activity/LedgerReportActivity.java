package com.wts.mytask.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wts.mytask.R;
import com.wts.mytask.modelclass.LedgerReportModel;
import com.wts.mytask.modelclass.MyWebserviceController;
import com.wts.mytask.myadaptor.LedgerReportAdapter;
import com.wts.mytask.myadaptor.RechargeReportAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LedgerReportActivity extends AppCompatActivity {

    ImageView backBt, downArrowImg, noDataFoundImg;
    TextView fromDate, toDate, noDataFound;

    LinearLayout fromDateLayout, toDateLayout;
    AutoCompleteTextView autoCompleteTextView;
    Button proceedBt;
    RecyclerView rv;

    ArrayList<String> dropDownList;
    ArrayList<LedgerReportModel> arrayList;
    LedgerReportAdapter ledgerReportAdapter;
    LedgerReportModel ledgerReportModel;
    String searchByItem = "ALL";
    String userId;
    String sFromDate, stoDate;
    String sOldBal, sAmount, sNewBal, sBalanceId, sPaymentType, sRemarks, sTransactionDate, sTransactionType, sTransactionTo, sMobileNo;

    SharedPreferences shp;
    Call<JsonObject> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_report);

        initViews();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.logingradientstartcolor));
        }

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LedgerReportActivity.this, DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });

        dropDownList = new ArrayList<>();
        dropDownList.add("ALL");
        dropDownList.add("Credit");
        dropDownList.add("Debit");

        autoCompleteTextView.setText("ALL", false);

        ArrayAdapter<String> autoCompAdapter = new ArrayAdapter<>(LedgerReportActivity.this, android.R.layout.simple_list_item_1, dropDownList);
        autoCompleteTextView.setAdapter(autoCompAdapter);
        downArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showDropDown();
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                searchByItem = dropDownList.get(i);

            }
        });

        shp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = shp.getString("userID", null);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


        fromDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                new DatePickerDialog(LedgerReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        Calendar fromdate1 = Calendar.getInstance();
                        fromdate1.set(i, i1, i2);
                        fromDate.setText(simpleDateFormat.format(fromdate1.getTime()));
                        sFromDate = apiDateFormat.format(fromdate1.getTime());

                    }
                }, year, month, day).show();

            }
        });

        toDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                new DatePickerDialog(LedgerReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar toDate1 = Calendar.getInstance();
                        toDate1.set(i, i1, i2);
                        toDate.setText(simpleDateFormat.format(toDate1.getTime()));
                        stoDate = apiDateFormat.format(toDate1.getTime());
                    }
                }, year, month, day).show();
            }
        });

        //////////////////////////////////////////////////////////////////TODAY FROM AND TO DATE
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar newDate1 = Calendar.getInstance();
        newDate1.set(year, month, day);
        fromDate.setText(simpleDateFormat.format(newDate1.getTime()));
        sFromDate = apiDateFormat.format(newDate1.getTime());
        toDate.setText(simpleDateFormat.format(newDate1.getTime()));
        stoDate = apiDateFormat.format(newDate1.getTime());

        ///////////////////////////////////////////////////////////////////////

        proceedBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceedBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (checkInternetState()) {
                            call = MyWebserviceController.getInstance().getApi().getLeger(userId, searchByItem, sFromDate, stoDate);
                            getLedgerReport();
                        } else {
                            Toast.makeText(LedgerReportActivity.this, "Please connect with Internet", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });


    }


    public void getLedgerReport() {

        ProgressDialog pd = new ProgressDialog(LedgerReportActivity.this);
        pd.setMessage("Loading .......");
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

                        if (responseCode.equalsIgnoreCase("TXN")) {

                            rv.setVisibility(View.VISIBLE);
                            noDataFoundImg.setVisibility(View.GONE);
                            noDataFound.setVisibility(View.GONE);

                            JSONArray jsonArray = jsonObject.getJSONArray("transactions");
                            arrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                ledgerReportModel = new LedgerReportModel();

                                sOldBal = jsonObject1.getString("old_bal");
                                sAmount = jsonObject1.getString("amount");
                                sNewBal = jsonObject1.getString("new_bal");
                                sBalanceId = jsonObject1.getString("balance_id");
                                sPaymentType = jsonObject1.getString("transaction_type");
                                sRemarks = jsonObject1.getString("remarks");
                                sTransactionDate = jsonObject1.getString("transaction_date");
                                sTransactionType = jsonObject1.getString("cr_dr_type");
                                sTransactionTo = jsonObject1.getString("targetUser");
                                sMobileNo = jsonObject1.getString("MobileNo");

                                ledgerReportModel.setOldBal(sOldBal);
                                ledgerReportModel.setNewBal(sNewBal);
                                ledgerReportModel.setAmount(sAmount);
                                ledgerReportModel.setBalanceId(sBalanceId);
                                ledgerReportModel.setPaymentType(sPaymentType);
                                ledgerReportModel.setRemarks(sRemarks);
                                ledgerReportModel.setTransactionDate(sTransactionDate);
                                ledgerReportModel.setCrDrType(sTransactionType);
                                ledgerReportModel.setTargetUser(sTransactionTo);
                                ledgerReportModel.setMobileNo(sMobileNo);

                                arrayList.add(ledgerReportModel);

                            }

                            ledgerReportAdapter = new LedgerReportAdapter(arrayList);
                            rv.setLayoutManager(new LinearLayoutManager(LedgerReportActivity.this, RecyclerView.VERTICAL, false));
                            rv.setAdapter(ledgerReportAdapter);
                            pd.dismiss();

                        } else if (responseCode.equalsIgnoreCase("ERR")) {
                            pd.dismiss();
                            rv.setVisibility(View.GONE);
                            noDataFoundImg.setVisibility(View.VISIBLE);
                            noDataFound.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        pd.dismiss();
                        rv.setVisibility(View.GONE);
                        noDataFoundImg.setVisibility(View.VISIBLE);
                        noDataFound.setVisibility(View.VISIBLE);
                    }
                } else {
                    pd.dismiss();
                    rv.setVisibility(View.GONE);
                    noDataFoundImg.setVisibility(View.VISIBLE);
                    noDataFound.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                pd.dismiss();
                rv.setVisibility(View.GONE);
                noDataFoundImg.setVisibility(View.VISIBLE);
                noDataFound.setVisibility(View.VISIBLE);

            }
        });

    }

    public void initViews() {
        backBt = findViewById(R.id.ledgerReportBackBt);
        autoCompleteTextView = findViewById(R.id.ledgerReportAutocompletetextview);
        downArrowImg = findViewById(R.id.ledgerReportDownarrowimageview);
        fromDateLayout = findViewById(R.id.ledgerReport_fromDateLayout);
        toDateLayout = findViewById(R.id.ledgerReportToDateLayout);
        fromDate = findViewById(R.id.ledgerReportFromDate);
        toDate = findViewById(R.id.ledgerReportToDate);
        proceedBt = findViewById(R.id.ledgerReportProceedBt);
        rv = findViewById(R.id.ledgerReportRv);
        noDataFoundImg = findViewById(R.id.ledgerReportNoDataFoundImg);
        noDataFound = findViewById(R.id.ledgerReportNoDataFoundTv);
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