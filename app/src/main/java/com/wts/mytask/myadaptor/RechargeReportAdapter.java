package com.wts.mytask.myadaptor;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wts.mytask.R;
import com.wts.mytask.modelclass.RechargeReportModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RechargeReportAdapter extends RecyclerView.Adapter<RechargeReportAdapter.MyViewHolder> {


    ArrayList<RechargeReportModel> list;

   int mExpandablePosition = -1;

    public RechargeReportAdapter(ArrayList<RechargeReportModel> list) {
        this.list = list;


    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rechargereportitem, null, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RechargeReportAdapter.MyViewHolder holder, int position) {

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

        Picasso.get().load(list.get(position).getImgUrl()).into(holder.operatorImg);
        holder.operatorName.setText(list.get(position).getOperatorName());
        holder.date.setText(list.get(position).getDate());
        holder.balance.setText(list.get(position).getBalance());
        holder.number.setText(list.get(position).getNumber());
        holder.amount.setText(list.get(position).getAmount());
        holder.commission.setText(list.get(position).getCommission());
        holder.surcharge.setText(list.get(position).getSurcharge());
        holder.cost.setText(list.get(position).getCost());
        holder.transactionStatus.setText(list.get(position).getStatus());

        if (list.get(position).getStatus().equalsIgnoreCase("Failed") || list.get(position).getStatus().equalsIgnoreCase("Failure"))
        {
            holder.transactionStatus.setTextColor(Color.parseColor("#870404"));
        }
        else holder.transactionStatus.setTextColor(Color.parseColor("#297103"));

           holder.operatorImg.setAnimation(AnimationUtils.loadAnimation(holder.amount.getContext(), R.anim.fade_scale_animation) );
        holder.transactionStatus.setAnimation(AnimationUtils.loadAnimation(holder.amount.getContext(), R.anim.slide_in_left) );

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView operatorImg;
        TextView operatorName,date,balance,number,transactionId,amount,commission,surcharge,cost,transactionStatus;

        LinearLayout layout1,layout2;

        View itemView;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.itemView=itemView;

            operatorImg = itemView.findViewById(R.id.rechargeReportItemImg);
            operatorName = itemView.findViewById(R.id.rechargeReportItemOperatorName);
            date = itemView.findViewById(R.id.rechargeReportItemDate);
            balance = itemView.findViewById(R.id.rechargeReportItemBalance);
            number = itemView.findViewById(R.id.rechareReportItemNumber);
            transactionId = itemView.findViewById(R.id.rechareReportItemTransactionId);
            amount = itemView.findViewById(R.id.rechareReportItemAmount);
            commission = itemView.findViewById(R.id.rechareReportItemCommission);
            surcharge = itemView.findViewById(R.id.rechareReportItemSurcharge);
            cost = itemView.findViewById(R.id.rechareReportItemCost);
            transactionStatus = itemView.findViewById(R.id.rechargeReportItemTransactionStatus);

            layout1 = itemView.findViewById(R.id.rechargeReportItemLayout1);
            layout2 = itemView.findViewById(R.id.rechargeReportItemMainLayout);


        }
    }

}
