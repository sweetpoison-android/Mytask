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
import com.wts.mytask.modelclass.MyWebserviceController;
import com.wts.mytask.R;
import com.wts.mytask.myadaptor.RechargeReportAdapter;
import com.wts.mytask.modelclass.RechargeReportModel;

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

public class RechargeReportActivity extends AppCompatActivity {

    ImageView backButton, downArrow, noDateFoundImg;
    TextView title, fromDate, toDate, noDataFound;
    RecyclerView rv;
    RechargeReportAdapter rechargeReportAdapter;
    ArrayList<RechargeReportModel> arrayList;
    RechargeReportModel rechargeReportModel;

    AutoCompleteTextView autoCompleteTextView;
    LinearLayout fromDateLayout, toDateLayout;
    Button proceedBt;

    String sTitle, searchByItem = "ALL", userId, sFromDate, stoDate;
    ArrayList<String> dropDownList;
    String operatorImg, operatorName, date, balance, number, transactionId, amount, commission, surcharge, cost, status;

    SharedPreferences shp;
    Call<JsonObject> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_report);

       initViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.logingradientstartcolor));
        }

        sTitle = getIntent().getStringExtra("rechargeReport");
        title.setText(sTitle);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RechargeReportActivity.this, DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });

        dropDownList = new ArrayList<>();
        dropDownList.add("ALL");
        dropDownList.add("Mobile");
        dropDownList.add("Postpaid");
        dropDownList.add("DTH");
        dropDownList.add("Money");

        //autoCompleteTextView.setHint("Search By");

        autoCompleteTextView.setText("ALL", false);

        ArrayAdapter<String> autoCompAdapter = new ArrayAdapter<>(RechargeReportActivity.this, android.R.layout.simple_list_item_1, dropDownList);
        autoCompleteTextView.setAdapter(autoCompAdapter);
        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showDropDown();
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchByItem = dropDownList.get(i);
               // Toast.makeText(RechargeReportActivity.this, searchByItem, Toast.LENGTH_SHORT).show();
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

                new DatePickerDialog(RechargeReportActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                new DatePickerDialog(RechargeReportActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                if (checkInternetState()) {
                    call = MyWebserviceController.getInstance().getApi().getReport(userId, searchByItem, sFromDate, stoDate);
                    getRechargeReport();
                } else {
                    Toast.makeText(RechargeReportActivity.this, "Please connect with Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void getRechargeReport() {

        ProgressDialog pd = new ProgressDialog(RechargeReportActivity.this);
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
                            noDateFoundImg.setVisibility(View.GONE);
                            noDataFound.setVisibility(View.GONE);

                            JSONArray jsonArray = jsonObject.getJSONArray("transactions");
                            arrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                rechargeReportModel = new RechargeReportModel();


                                operatorImg = jsonObject1.getString("img");
                                operatorName = jsonObject1.getString("opname");
                                date = jsonObject1.getString("tdatetime");
                                balance = jsonObject1.getString("balance");
                                number = jsonObject1.getString("number");
                                transactionId = jsonObject1.getString("transactionid");
                                amount = jsonObject1.getString("amount");
                                commission = jsonObject1.getString("comm");
                                surcharge = jsonObject1.getString("surcharge");
                                cost = jsonObject1.getString("cost");
                                status = jsonObject1.getString("status");

                                rechargeReportModel.setImgUrl(operatorImg);
                                rechargeReportModel.setOperatorName(operatorName);
                                rechargeReportModel.setDate(date);
                                rechargeReportModel.setBalance(balance);
                                rechargeReportModel.setNumber(number);
                                rechargeReportModel.setTransactionId(transactionId);
                                rechargeReportModel.setAmount(amount);
                                rechargeReportModel.setCommission(commission);
                                rechargeReportModel.setSurcharge(surcharge);
                                rechargeReportModel.setCost(cost);
                                rechargeReportModel.setStatus(status);

                                arrayList.add(rechargeReportModel);
                            }

                           rechargeReportAdapter = new RechargeReportAdapter(arrayList);
                            rv.setLayoutManager(new LinearLayoutManager(RechargeReportActivity.this, RecyclerView.VERTICAL, false));
                            rv.setAdapter(rechargeReportAdapter);
                            pd.dismiss();

                        } else if (responseCode.equalsIgnoreCase("ERR")) {
                            pd.dismiss();
                            rv.setVisibility(View.GONE);
                            noDateFoundImg.setVisibility(View.VISIBLE);
                            noDataFound.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        pd.dismiss();
                        rv.setVisibility(View.GONE);
                        noDateFoundImg.setVisibility(View.VISIBLE);
                        noDataFound.setVisibility(View.VISIBLE);
                    }
                } else {
                    pd.dismiss();
                    rv.setVisibility(View.GONE);
                    noDateFoundImg.setVisibility(View.VISIBLE);
                    noDataFound.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                pd.dismiss();
                rv.setVisibility(View.GONE);
                noDateFoundImg.setVisibility(View.VISIBLE);
                noDataFound.setVisibility(View.VISIBLE);

            }
        });

    }

    public void initViews() {
        backButton = findViewById(R.id.rechargeReportBackBt);
        title = findViewById(R.id.rechargeReportTv);
        autoCompleteTextView = findViewById(R.id.rechargeReportAutocompletetextview);
        downArrow = findViewById(R.id.rechargeReportDownarrowimageview);
        fromDateLayout = findViewById(R.id.rechargeReport_fromDateLayout);
        toDateLayout = findViewById(R.id.rechargeReportToDateLayout);
        fromDate = findViewById(R.id.rechargeReportFromDate);
        toDate = findViewById(R.id.rechargeReportToDate);
        proceedBt = findViewById(R.id.rechargeReportProceedBt);
        noDateFoundImg = findViewById(R.id.rechargeReportNoDataFoundImg);
        noDataFound = findViewById(R.id.creditDebitReportNoDataFoundTv);
        rv = findViewById(R.id.rechargeReportRv);

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