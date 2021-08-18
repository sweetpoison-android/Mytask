package com.wts.mytask.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.wts.mytask.R;
import com.wts.mytask.modelclass.MyWebserviceController;
import com.wts.mytask.modelclass.WebServiceInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView tv_walletbalance;
    LinearLayout linearWalletBalance;
    TextView walletBalance;
    String wallet_balance;
    String userid;

    DrawerLayout dl;
    ImageView navigationicon;
    NavigationView nv;
    BottomNavigationView bnv;

    LinearLayout prepaidlayout, postpaidlayout, dthLayout;
    SharedPreferences shp;

    ImageSlider imageSlider;
    ArrayList<SlideModel> imageList;

    WebServiceInterface webServiceInterface;

    boolean b = false;

    //   change password ..............

    TextInputEditText tietUserId, tietPassword, tietNewPassword, tietConfirmPassword;
    AlertDialog alertDialog;
    String sUsedId, sPassword, sNewPassword, sConfirmPassword;
    Button btChangePassword;
    TextView tvCancel;
////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();

        bnv.setItemIconTintList(null);
        nv.setItemIconTintList(null);

        // .............status bar colcor change...............................

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.logingradientstartcolor));
        }

//////////////////////////////////////////////////////////////////////

        nv.setNavigationItemSelectedListener(this);

        linearWalletBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder bld = new AlertDialog.Builder(DashboardActivity.this);
                bld.setTitle("Wallet Balance");
                bld.setMessage("you have ₹ " + wallet_balance + " /-");
                bld.setIcon(R.drawable.wallet_1);

                bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                Dialog dialog = bld.create();
                if (dialog.getWindow() != null) {
                    dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
                }
                if (wallet_balance != null) {
                    dialog.show();
                } else {
                    if (checkInternetState()) {

                        startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        Toast.makeText(DashboardActivity.this, "Please connect with internet", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        webServiceInterface = WebServiceInterface.retrofit.create(WebServiceInterface.class);

        prepaidlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(DashboardActivity.this, PrepaidRechargeActivity.class);
                in.putExtra("title", "Prepaid Recharge");
                in.putExtra("service", "Mobile");
                startActivity(in);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });

        postpaidlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(DashboardActivity.this, PrepaidRechargeActivity.class);
                in.putExtra("title", "Postpaid Recharge");
                in.putExtra("service", "Postpaid");
                startActivity(in);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });

        dthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(DashboardActivity.this, PrepaidRechargeActivity.class);
                in.putExtra("title", "DTH Recharge");
                in.putExtra("service", "DTH");
                startActivity(in);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });

        imageList = new ArrayList<SlideModel>();

        imageList.add(new SlideModel(R.drawable.mytaskimage, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.mytaskimage2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.mytaskimage3, ScaleTypes.FIT));

        imageSlider = findViewById(R.id.contentmain_imageslider);
        imageSlider.setImageList(imageList);
        imageSlider.setImageList(imageList, ScaleTypes.FIT);

        imageSlider.startSliding(2000);// with new period

        navigationicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.openDrawer(GravityCompat.START);

            }
        });

        // BottomNavigation item click ......................................

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.bottomnavigation_profile:
                        Toast.makeText(DashboardActivity.this, "BottomNavigation Profile selected", Toast.LENGTH_SHORT).show();
                        break;


                    case R.id.bottomnavigation_fundrequest:
                        Toast.makeText(DashboardActivity.this, "bottom fund request", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.bottomnavigation_logout:
                        logOutAlertDialog();
                        break;

                    default: {
                    }
                }

                return false;
            }
        });

////////////////////////////////////////////////////////////////////////////////

        View v = nv.getHeaderView(0);
        v.findViewById(R.id.navheader_imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "Header image clicked", Toast.LENGTH_SHORT).show();
            }
        });

        shp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userid = shp.getString("userID", null);

        if (checkInternetState()) {
            getWalletBalance();
        } else {
            Toast.makeText(this, "Please connect with Internet", Toast.LENGTH_SHORT).show();
            tv_walletbalance.setText("₹" + 0.00 + "/-");
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_creditReports:
                Intent credit = new Intent(DashboardActivity.this, CreditDebitReportActivity.class);
                credit.putExtra("creditDebitReport", "Credit Report");
                startActivity(credit);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
                break;

            case R.id.navigation_debitReports:
                Intent debit = new Intent(DashboardActivity.this, CreditDebitReportActivity.class);
                debit.putExtra("creditDebitReport", "Debit Report");
                startActivity(debit);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
                break;

            case R.id.navigation_rechargeReport:
                Intent recharge = new Intent(DashboardActivity.this, RechargeReportActivity.class);
                recharge.putExtra("rechargeReport", "Recharge Report");
                startActivity(recharge);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
                break;

            case R.id.navigationLedger:
                startActivity(new Intent(DashboardActivity.this, LedgerReportActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
                break;


            case R.id.navigationCreditBalance:
                Intent creditIntent = new Intent(DashboardActivity.this, CreditDevitBalanceActivity.class);
                creditIntent.putExtra("creditDebitBalance", "Credit Balance");
                startActivity(creditIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
                break;

            case R.id.navigationDebitBalance:
                Intent debitIntent = new Intent(DashboardActivity.this, CreditDevitBalanceActivity.class);
                debitIntent.putExtra("creditDebitBalance", "Debit Balance");
                startActivity(debitIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
                break;

            case R.id.navigationChangePassword:

                changePassword();
                dl.closeDrawer(GravityCompat.START);
                break;

        }

        return false;
    }

    public void changePassword() {
        View v = LayoutInflater.from(DashboardActivity.this).inflate(R.layout.change_password_dialog, null, false);
        tietPassword = v.findViewById(R.id.changePassword_passwordedittext);
        tietNewPassword = v.findViewById(R.id.changePassword_newPasswordedittext);
        tietConfirmPassword = v.findViewById(R.id.changePassword_confirmPasswordedittext);
        btChangePassword = v.findViewById(R.id.forgetpassword_changePasswordButton);
        tvCancel = v.findViewById(R.id.changePassword_canceltv);

        alertDialog = new AlertDialog.Builder(DashboardActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.setView(v);

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

        btChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changePasswordCheckInput()) {

                    sPassword = tietPassword.getText().toString().trim();
                    sNewPassword = tietNewPassword.getText().toString();
                     changePasswordRecover(userid, sPassword, sNewPassword);
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void changePasswordRecover(String userID, String password, String NewPassword) {
        ProgressDialog pd = new ProgressDialog(DashboardActivity.this);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        pd.setMessage("Loading ...");
        pd.show();

         Call<JsonObject> call = MyWebserviceController.getInstance().getApi().changePassword(userID, password, NewPassword);
         call.enqueue(new Callback<JsonObject>() {
             @Override
             public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                 if (response.isSuccessful())
                 {
                     try {
                         JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                         String responseCode = jsonObject.getString("response_code");
                         if (responseCode.equalsIgnoreCase("TXN"))
                         {

                             pd.dismiss();
                             String responseMessage = jsonObject.getString("response_msg");
                             String changeStatus = jsonObject.getString("transactions");
                             new AlertDialog.Builder(DashboardActivity.this)
                                     .setTitle(responseMessage)
                                     .setMessage(changeStatus)
                                     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialogInterface, int i) {
                                           recreate();

                                         }
                                     })
                                     .show();
                         }
                         else if (responseCode.equalsIgnoreCase("ERR"))
                         {
                             pd.dismiss();
                             String responseMessage = jsonObject.getString("response_msg");
                             String changeStatus = jsonObject.getString("transactions");
                             new AlertDialog.Builder(DashboardActivity.this)
                                     .setTitle(responseMessage)
                                     .setMessage(changeStatus)
                                     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                         }
                                     })
                                     .show();
                         }

                     } catch (JSONException e) {
                         e.printStackTrace();
                         errorDialog();
                     }
                 }
                 else
                 {
                    errorDialog();
                 }
             }

             @Override
             public void onFailure(Call<JsonObject> call, Throwable t) {
          errorDialog();
             }
         });
    }

    public void errorDialog() {
        new AlertDialog.Builder(DashboardActivity.this)
                .setTitle("Alert")
                .setMessage("Something went wrong")
                .show();
    }

    public void logOutAlertDialog() {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setTitle("Logout");
        bld.setMessage("Are you sure you want to logout...");
        bld.setCancelable(false);
        bld.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SharedPreferences.Editor edt = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                edt.clear();
                edt.apply();

                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });
        bld.setNeutralButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        Dialog dialog = bld.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        }
        dialog.show();
    }

    public void getWalletBalance() {
        Call<JsonObject> call = webServiceInterface.getBalance(userid);
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
                                wallet_balance = jsonObject1.getString("balance");
                            }
                        }

                        tv_walletbalance.setText("₹" + wallet_balance + "/-");


                    } catch (JSONException e) {
                        e.printStackTrace();
                        tv_walletbalance.setText("₹" + 0.00 + "/-");
                    }
                } else {
                    tv_walletbalance.setText("₹" + 0.00 + "/-");
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


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

    public boolean changePasswordCheckInput() {

            if (!TextUtils.isEmpty(tietPassword.getText())) {
                if (!TextUtils.isEmpty(tietNewPassword.getText())) {

                    if (!TextUtils.isEmpty(tietConfirmPassword.getText())) {
                        if (tietNewPassword.getText().toString().trim().equals(tietConfirmPassword.getText().toString().trim())) {
                            return true;
                        } else {
                            tietNewPassword.setError( "Password and Confirm Password must be same");
                            Toast.makeText(this, "Password and Confirm Password must be same", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    } else {
                        tietConfirmPassword.setError("Please enter Confirm Password.");
                        return false;
                    }

                } else {
                    tietNewPassword.setError("Please enter New Password.");
                    return false;
                }
            } else {
                tietPassword.setError("Please enter Password.");
                return false;
            }
        }



    public void initViews() {
        tv_walletbalance = findViewById(R.id.appbar_walletbalance);
        linearWalletBalance = findViewById(R.id.appbar_walletlayout);
        walletBalance = findViewById(R.id.appbar_wallettextview);
        dl = findViewById(R.id.dashboard_drawer_layout);
        navigationicon = findViewById(R.id.appbar_navigationicon);
        nv = findViewById(R.id.dashboard_nav);
        bnv = findViewById(R.id.bottom_navigation);
        prepaidlayout = findViewById(R.id.contentmain_prepaidlinearlayout);
        postpaidlayout = findViewById(R.id.contentmain_postpaidlinearlayout);
        dthLayout = findViewById(R.id.contentmain_dthlinearlayout);
    }


}