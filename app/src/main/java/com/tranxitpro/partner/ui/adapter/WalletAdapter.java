package com.tranxitpro.partner.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tranxitpro.partner.MvpApplication;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.data.network.model.Transaction;
import com.tranxitpro.partner.data.network.model.WalletTransation;
import com.tranxitpro.partner.ui.activity.wallet_detail.WalletDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.MyViewHolder> {

    private List<WalletTransation> mWallets;
    private Context mContext;

    public WalletAdapter(List<WalletTransation> wallets) {
        this.mWallets = wallets;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_wallet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvId.setText(mWallets.get(position).getTransactionAlias());
        holder.tvDate.setText(parseDateToddMMyyyy(mWallets.get(position).getTransactions().get(0).getCreatedAt()));

        if (mWallets.get(position).getTransactions().get(0).getType().equalsIgnoreCase("C")) {
            holder.tvAmt.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            holder.tvAmt.setText(String.format("%s %s",
                    Constants.Currency,
                    MvpApplication.getInstance().getNewNumberFormat(mWallets.get(position).getAmount())));
        } else {
            holder.tvAmt.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            holder.tvAmt.setText(String.format("%s %s",
                    Constants.Currency,
                    MvpApplication.getInstance().getNewNumberFormat(mWallets.get(position).getAmount())));
        }
    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public int getItemCount() {
        return mWallets.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvId, tvDate, tvAmt;

        private MyViewHolder(View view) {
            super(view);
            tvId = view.findViewById(R.id.tvId);
            tvDate = view.findViewById(R.id.tvDate);
            tvAmt = view.findViewById(R.id.tvAmt);

            tvId.setOnClickListener(v -> {
                ArrayList<Transaction> transactionList = new ArrayList<>(mWallets.get(getAdapterPosition()).getTransactions());
                Intent intent = new Intent(mContext, WalletDetailActivity.class);
                intent.putExtra("transaction_list", transactionList);
                intent.putExtra("alias", mWallets.get(getAdapterPosition()).getTransactionAlias());
                mContext.startActivity(intent);
            });
        }
    }
}