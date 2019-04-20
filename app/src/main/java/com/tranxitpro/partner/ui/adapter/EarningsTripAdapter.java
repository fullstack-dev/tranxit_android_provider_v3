package com.tranxitpro.partner.ui.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tranxitpro.partner.MvpApplication;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.data.network.model.Ride;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


public class EarningsTripAdapter extends RecyclerView.Adapter<EarningsTripAdapter.MyViewHolder> {

    private List<Ride> list;

    public EarningsTripAdapter(List<Ride> list) {
        this.list = list;
    }

    public void setList(List<Ride> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_earnings, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Ride ride = list.get(position);
        holder.lblDistance.setText(ride.getDistance() + " Km");
        if (ride.getPayment() != null)
            holder.lblAmount.setText(Constants.Currency + " " +
                    MvpApplication.getInstance().getNewNumberFormat(ride.getPayment().getProviderPay()));
        else
            holder.lblAmount.setText(Constants.Currency + " " + "0.00");
        StringTokenizer tk = new StringTokenizer(ride.getAssignedAt());
        String date = tk.nextToken();
        String time = tk.nextToken();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt;
        try {
            dt = sdf.parse(time);
            holder.lblTime.setText(sdfs.format(dt));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView lblTime, lblDistance, lblAmount;

        private MyViewHolder(View view) {
            super(view);

            lblTime = (TextView) view.findViewById(R.id.lblTime);
            lblDistance = (TextView) view.findViewById(R.id.lblDistance);
            lblAmount = (TextView) view.findViewById(R.id.lblAmount);
        }
    }
}