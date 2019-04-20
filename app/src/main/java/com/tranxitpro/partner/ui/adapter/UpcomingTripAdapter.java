package com.tranxitpro.partner.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.data.network.model.HistoryList;

import java.util.List;

public class UpcomingTripAdapter extends RecyclerView.Adapter<UpcomingTripAdapter.MyViewHolder> {

    private List<HistoryList> list;
    private Context context;

    private ClickListener clickListener;

    public UpcomingTripAdapter(List<HistoryList> list, Context con) {
        this.list = list;
        this.context = con;
    }

    public void setList(List<HistoryList> list) {
        this.list = list;
    }

    public void setClickListener(UpcomingTripAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_upcoming_trip,
                        parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HistoryList historyList = list.get(position);

        holder.lblDate.setText(historyList.getScheduleAt());
        holder.lblBookingid.setText(historyList.getBookingId());
        holder.lblSeviceName.setText(historyList.getServicetype().getName());

        Glide.with(context).load(historyList.getStaticMap()).
                apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background).
                        dontAnimate().error(R.drawable.ic_launcher_background).
                        diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.staticMap);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ClickListener {
        void redirectClick(HistoryList historyList, ImageView staticMap);

        void cancelRide(HistoryList historyList, int pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView itemView;
        private TextView lblDate, lblBookingid, lblSeviceName;
        private ImageView staticMap;
        private Button btnCancelRide;

        private MyViewHolder(View view) {
            super(view);

            itemView = view.findViewById(R.id.item_view);
            lblDate = view.findViewById(R.id.lblDate);
            lblBookingid = view.findViewById(R.id.lblBookingid);
            lblSeviceName = view.findViewById(R.id.lblSeviceName);
            staticMap = view.findViewById(R.id.static_map);

            btnCancelRide = view.findViewById(R.id.btnCancelRide);

            itemView.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.redirectClick(list.get(getAdapterPosition()), staticMap);
            });

            btnCancelRide.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.cancelRide(list.get(getAdapterPosition()), getAdapterPosition());
            });
        }
    }
}