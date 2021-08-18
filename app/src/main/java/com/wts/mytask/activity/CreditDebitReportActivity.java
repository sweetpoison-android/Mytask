package com.wts.mytask.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.wts.mytask.myadaptor.CreditDebitReportAdapter;
import com.wts.mytask.modelclass.CreditDebitReportModel;
import com.wts.mytask.modelclass.MyWebserviceController;
import com.wts.mytask.R;

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

public class CreditDebitReportActivity extends AppCompatActivity {

    ImageView backButton;
    TextView report, fromDate, toDate, noDataFound;
    CardView cardView;
    LinearLayout fromDateLayout, toDateLayout;

    AutoCompleteTextView autoCompleteTextView;
    ImageView chooseTypeImg, noDataFoundImg;
    Button proceedBt;

    CreditDebitReportAdapter creditDebitReportAdapter;
    ArrayList<CreditDebitReportModel> arrayList;
    RecyclerView rv;

    ArrayList<String> filterType = new ArrayList<>();
    String sFromDate, stoDate;
    String userId;
    String searchBy = "ALL";
    String sdrUSer, scrUser, sid, sAmount, sPaymentType, sPaymentDate, sRemarks;

    Call<JsonObject> call;
    SharedPreferences shp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_debit_report);

        initViews();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.logingradientstartcolor));
        }

        String SReport = getIntent().getStringExtra("creditDebitReport");
        report.setText(SReport);
        shp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = shp.getString("userID", null);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreditDebitReportActivity.this, DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });


        filterType.add("ALL");
        filterType.add("DATE");

        autoCompleteTextView.setText("ALL", false);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, filterType);
        autoCompleteTextView.setAdapter(arrayAdapter);
        chooseTypeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showDropDown();
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchBy = filterType.get(i);

                if (searchBy.equalsIgnoreCase("DATE")) {
                    cardView.setVisibility(View.VISIBLE);

                } else if (searchBy.equalsIgnoreCase("ALL")) {
                    cardView.setVisibility(View.GONE);
                }
            }
        });


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        fromDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                new DatePickerDialog(CreditDebitReportActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                new DatePickerDialog(CreditDebitReportActivity.this, new DatePickerDialog.OnDateSetListener() {
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


        if (checkInternetState()) {
            if (SReport.equalsIgnoreCase("Credit Report")) {
                call = MyWebserviceController.getInstance().getApi().Creditreport(userId, searchBy, sFromDate, stoDate);
                getReport();
            } else if (SReport.equalsIgnoreCase("Debit Report")) {
                call = MyWebserviceController.getInstance().getApi().Debitreport(userId, searchBy, sFromDate, stoDate);
                getReport();
            }
        } else {
            Toast.makeText(this, "Please Connect With Internet", Toast.LENGTH_SHORT).show();
        }

        //////////////////////////////////////////////////////////////////TODAY FROM AND TO DATE

        proceedBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkInternetState()) {

                    if (SReport.equalsIgnoreCase("Credit Report")) {

                        call = MyWebserviceController.getInstance().getApi().Creditreport(userId, searchBy, sFromDate, stoDate);
                        getReport();

                    } else if (SReport.equalsIgnoreCase("Debit Report")) {
                        if (searchBy.equalsIgnoreCase("ALL")) ///////////////////if user did not select search by
                        {
                            new AlertDialog.Builder(CreditDebitReportActivity.this).setMessage("Please select filter type")
                                    .setPositiveButton("Ok", null).show();
                        } else {
                            call = MyWebserviceController.getInstance().getApi().Debitreport(userId, searchBy, sFromDate, stoDate);
                            getReport();
                        }
                    }

                } else {
                    Toast.makeText(CreditDebitReportActivity.this, "Please Connect with Internet", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void getReport() {
        ProgressDialog pd = new ProgressDialog(CreditDebitReportActivity.this);
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
                        if (responseCode.equalsIgnoreCase("TXN")) {

                            rv.setVisibility(View.VISIBLE);
                            noDataFound.setVisibility(View.GONE);
                            noDataFoundImg.setVisibility(View.GONE);


                            JSONArray jsonArray = jsonObject.getJSONArray("transactions");
                            arrayList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CreditDebitReportModel creditDebitReportModel = new CreditDebitReportModel();

                                sdrUSer = jsonObject1.getString("DrUser");
                                scrUser = jsonObject1.getString("CrUser");
                                sid = jsonObject1.getString("id");
                                sAmount = jsonObject1.getString("Amount");
                                sPaymentType = jsonObject1.getString("PaymentType");
                                sPaymentDate = jsonObject1.getString("PaymentDate");
                                sRemarks = jsonObject1.getString("Remarks");

                                creditDebitReportModel.setDrUser(sdrUSer);
                                creditDebitReportModel.setCrUser(scrUser);
                                creditDebitReportModel.setAmount(sAmount);
                                creditDebitReportModel.setID(sid);
                                creditDebitReportModel.setPaymentType(sPaymentType);
                                creditDebitReportModel.setPaymentDate(sPaymentDate);
                                creditDebitReportModel.setRemarks(sRemarks);

                                arrayList.add(creditDebitReportModel);

                            }
                            creditDebitReportAdapter = new CreditDebitReportAdapter(arrayList);
                            rv.setLayoutManager(new LinearLayoutManager(CreditDebitReportActivity.this, RecyclerView.VERTICAL, false));
                            rv.setAdapter(creditDebitReportAdapter);
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
        cardView = findViewById(R.id.creditDebitReport_cardview1);
        backButton = findViewById(R.id.creditDebitReportBackBt);
        report = findViewById(R.id.creditDebitReportTv);
        rv = findViewById(R.id.creditDebitReportRv);
        fromDateLayout = findViewById(R.id.creditDebitReport_fromDateLayout);
        toDateLayout = findViewById(R.id.creditDebitReportToDateLayout);
        fromDate = findViewById(R.id.creditDebitReportFromDate);
        toDate = findViewById(R.id.creditDebitReportToDate);
        autoCompleteTextView = findViewById(R.id.creditDebitReportAutocompletetextview);
        chooseTypeImg = findViewById(R.id.creditDebitReportDownarrowimageview);
        proceedBt = findViewById(R.id.creditDebitReportProceedBt);
        noDataFound = findViewById(R.id.creditDebitReportNoDataFoundTv);
        noDataFoundImg = findViewById(R.id.creditDebitReportNoDataFoundImg);
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