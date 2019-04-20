package com.tranxitpro.partner.ui.bottomsheetdialog.cancel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseBottomSheetDialogFragment;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.SharedHelper;
import com.tranxitpro.partner.data.network.model.CancelResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CancelDialogFragment extends BaseBottomSheetDialogFragment implements CancelDialogIView {


    @BindView(R.id.txtComments)
    EditText comments;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    Unbinder unbinder;
    @BindView(R.id.rcvReason)
    RecyclerView rcvReason;


    CancelDialogPresenter presenter;

    private List<CancelResponse> cancelResponseList = new ArrayList<>();
    private ReasonAdapter adapter;
    private int last_selected_location = -1;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_cancel;
    }

    @Override
    public void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        presenter = new CancelDialogPresenter();
        presenter.attachView(this);
        adapter = new ReasonAdapter(cancelResponseList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
                (getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvReason.setLayoutManager(mLayoutManager);
        rcvReason.setItemAnimator(new DefaultItemAnimator());
        rcvReason.setAdapter(adapter);

        presenter.getReasons();

    }


    @Override
    public void onSuccess(List<CancelResponse> response) {
        cancelResponseList.addAll(response);
        addCommonReason();
    }

    @Override
    public void onReasonError(Throwable e) {
        addCommonReason();
    }

    private void addCommonReason() {
        //Common reason added here
        CancelResponse commonReason = new CancelResponse();
        commonReason.setReason("Other Reason");
        commonReason.setType("USER");
        commonReason.setStatus(1);
        commonReason.setId(0);
        cancelResponseList.add(commonReason);
        if (cancelResponseList.size() == 1){
            last_selected_location = 0;
            comments.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }



    @OnClick(R.id.btnSubmit)
    public void onViewClicked() {
        if (last_selected_location == -1){
            Toast.makeText(getContext(), getString(R.string.invalid_selection), Toast.LENGTH_SHORT).show();
            return;
        }
        cancelRequest();
    }

    private void cancelRequest() {

        if (comments.getText().toString().isEmpty() && (last_selected_location == cancelResponseList.size() -1)) {
            Toast.makeText(getContext(), getString(R.string.please_enter_cancel_reason), Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", SharedHelper.getKey(getContext(), Constants.SharedPref.CANCEL_ID));
        map.put("cancel_reason", comments.getText().toString());
        showLoading();
        presenter.cancelRequest(map);
    }

    @Override
    public void onSuccessCancel(Object object) {
        dismissAllowingStateLoss();
        hideLoading();
        activity().sendBroadcast(new Intent("INTENT_FILTER"));
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if(e!= null)
        onErrorBase(e);
    }

    private class ReasonAdapter extends RecyclerView.Adapter<CancelDialogFragment.ReasonAdapter.MyViewHolder> {

        private List<CancelResponse> list;
        private Context mContext;


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            LinearLayout llItemView;
            TextView tvReason;
            CheckBox cbItem;

            MyViewHolder(View view) {
                super(view);
                llItemView = view.findViewById(R.id.llItemView);
                tvReason = view.findViewById(R.id.tvReason);
                cbItem = view.findViewById(R.id.cbItem);

                llItemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if(position == list.size() -1){
                    comments.setVisibility(View.VISIBLE);
                }else{
                    comments.setVisibility(View.GONE);
                }
                last_selected_location = position;
                notifyDataSetChanged();
            }
        }

        private ReasonAdapter(List<CancelResponse> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public CancelDialogFragment.ReasonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            return new CancelDialogFragment.ReasonAdapter.MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cancel_reasons_inflate, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull CancelDialogFragment.ReasonAdapter.MyViewHolder holder, int position) {

            CancelResponse data = list.get(position);
            holder.tvReason.setText(data.getReason());

            if (last_selected_location == position){
                holder.cbItem.setChecked(true);
            }else{
                holder.cbItem.setChecked(false);
            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
