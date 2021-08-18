package com.wts.mytask.myadaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.wts.mytask.R;
import com.wts.mytask.modelclass.CreditDebitReportModel;
import com.wts.mytask.modelclass.RechargeReportModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CreditDebitReportAdapter extends RecyclerView.Adapter<CreditDebitReportAdapter.MyviewHolder> {

    ArrayList<CreditDebitReportModel> list;

    int mExpandablePosition = -1;

    public CreditDebitReportAdapter(ArrayList<CreditDebitReportModel> list) {
        this.list = list;

    }

    @NonNull
    @NotNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_debit_report_item, null, false);
        return new MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CreditDebitReportAdapter.MyviewHolder holder, int position) {


        final boolean isExpanded = position == mExpandablePosition;
        holder.layout2.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.layout1.setActivated(isExpanded);
        holder.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandablePosition = isExpanded ? -1:position;
                notifyDataSetChanged();
                holder.layout1.setAnimation(AnimationUtils.loadAnimation(holder.crUser.getContext(), R.anim.fade_scale_animation));

            }
        });


        holder.drUser.setText(list.get(position).getDrUser());
        holder.crUser.setText(list.get(position).getCrUser());
        holder.amount.setText(list.get(position).getAmount());
        holder.id.setText(list.get(position).getID());
        holder.paymentType.setText(list.get(position).getPaymentType());
        holder.paymentDate.setText(list.get(position).getPaymentDate());
        holder.remarks.setText(list.get(position).getRemarks());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView  drUser,crUser, amount,id, paymentType, paymentDate, remarks;
        CardView cardView;

        LinearLayout layout1,layout2;
        View itemView;

        public MyviewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.itemView=itemView;


            drUser = itemView.findViewById(R.id.creditDebitReportItemDrUser);
            crUser = itemView.findViewById(R.id.creditDebitReportItemCrUser);
            amount = itemView.findViewById(R.id.creditDebitReportItemAmount);
            id = itemView.findViewById(R.id.creditDebitReportItemId);
            paymentType = itemView.findViewById(R.id.creditDebitReportItemPaymentType);
            paymentDate = itemView.findViewById(R.id.creditDebitReportItemPaymentDate);
            remarks = itemView.findViewById(R.id.creditDebitReportItemRemarks);
            cardView = itemView.findViewById(R.id.creditDebitReportItemCardView);

            layout1 = itemView.findViewById(R.id.creditDebitReportItemMainLayout1);
            layout2 = itemView.findViewById(R.id.creditDebitReportItemMainLayout2);



        }
    }
}
