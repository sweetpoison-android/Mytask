package com.wts.mytask.myadaptor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wts.mytask.R;
import com.wts.mytask.modelclass.CreditDebitReportModel;
import com.wts.mytask.modelclass.LedgerReportModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.wts.mytask.R.drawable.loginbuttonbackground;

public class LedgerReportAdapter extends RecyclerView.Adapter<LedgerReportAdapter.MyViewHolder> {

    int mExpandablePosition = -1;

    ArrayList<LedgerReportModel> arrayList;

    public LedgerReportAdapter(ArrayList<LedgerReportModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledgerreportitem, null, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LedgerReportAdapter.MyViewHolder holder, int position) {

        final boolean isExpanded = position == mExpandablePosition;
        holder.layout2.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.layout1.setActivated(isExpanded);
        holder.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandablePosition = isExpanded ? -1:position;
                notifyDataSetChanged();


            }
        });

        holder.oldBal.setText(arrayList.get(position).getOldBal());
        holder.amount.setText(arrayList.get(position).getAmount());
        holder.newBal.setText(arrayList.get(position).getNewBal());
        holder.transactionId.setText(arrayList.get(position).getBalanceId());
        holder.paymentType.setText(arrayList.get(position).getPaymentType());
        holder.remarks.setText(arrayList.get(position).getRemarks());
        holder.transactionDate.setText(arrayList.get(position).getTransactionDate());
        holder.transactionType.setText(arrayList.get(position).getCrDrType());
        holder.transactionTo.setText(arrayList.get(position).getTargetUser());
        holder.mobileNo.setText(arrayList.get(position).getMobileNo());

        holder.shareImageLayout.setAnimation(AnimationUtils.loadAnimation(holder.shareImageLayout.getContext(), R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView oldBal, amount, newBal, transactionId, paymentType, remarks, transactionDate, transactionType, transactionTo, mobileNo;

        LinearLayout layout1, layout2,shareImageLayout;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            oldBal = itemView.findViewById(R.id.ledgerReportItemOldBal);
            amount = itemView.findViewById(R.id.ledgerReportItemAmount);
            newBal = itemView.findViewById(R.id.ledgerReportItemNewBal);
            transactionId = itemView.findViewById(R.id.ledgerReportItemTransactionId);
            paymentType = itemView.findViewById(R.id.ledgerReportItemPaymentType);
            remarks = itemView.findViewById(R.id.ledgerReportItemRemarks);
            transactionDate = itemView.findViewById(R.id.ledgerReportItemTransactionDate);
            transactionType = itemView.findViewById(R.id.ledgerReportItemTransactionType);
            transactionTo = itemView.findViewById(R.id.ledgerReportItemTransactionTo);
            mobileNo = itemView.findViewById(R.id.ledgerReportItemMobileNo);

            layout1 = itemView.findViewById(R.id.ledgerReportItemMainLayout1);
            layout2 = itemView.findViewById(R.id.ledgerReportItemMainLayout2);
            shareImageLayout = itemView.findViewById(R.id.ledgerReportItemShareImageLayout);

            shareImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bitmap imgBitmap = BitmapFactory.decodeResource(oldBal.getContext().getResources(), R.drawable.mytasklogo);
                    String imgBitmapPath = MediaStore.Images.Media.insertImage(oldBal.getContext().getContentResolver(), imgBitmap, "title" + System.currentTimeMillis(), null);
                    Uri imgBitmapUri = Uri.parse(imgBitmapPath);

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                  //  shareIntent.setPackage("com.whatsapp");  // for only on whatsapp sharing
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri);
                    shareIntent.setType("image/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, " Old Balance : " + arrayList.get(getAdapterPosition()).getOldBal() + "\n"
                            + " Transaction Amount : " + arrayList.get(getAdapterPosition()).getAmount() + "\n"
                            + " New Balance : " + arrayList.get(getAdapterPosition()).getNewBal() + "\n"
                            + " Transaction Type : " + arrayList.get(getAdapterPosition()).getCrDrType());

                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CSP SEWA");
                    oldBal.getContext().startActivity(Intent.createChooser(shareIntent, "Share this"));

                }
            });

        }
    }

}
