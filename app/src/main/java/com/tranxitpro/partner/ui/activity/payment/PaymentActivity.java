package com.tranxitpro.partner.ui.activity.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.data.network.model.Card;
import com.tranxitpro.partner.ui.activity.add_card.AddCardActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tranxitpro.partner.MvpApplication.isBraintree;
import static com.tranxitpro.partner.MvpApplication.isCard;
import static com.tranxitpro.partner.MvpApplication.isCash;
import static com.tranxitpro.partner.MvpApplication.isPaytm;
import static com.tranxitpro.partner.MvpApplication.isPayumoney;

public class PaymentActivity extends BaseActivity implements PaymentIView {

    public static final int PICK_PAYMENT_METHOD = 12;
    public static boolean isInvoiceCashToCard = false;
    @BindView(R.id.cash)
    TextView tvCash;
    @BindView(R.id.cards_rv)
    RecyclerView cardsRv;
    @BindView(R.id.llCardContainer)
    LinearLayout llCardContainer;
    @BindView(R.id.llCashContainer)
    LinearLayout llCashContainer;
    @BindView(R.id.braintree)
    TextView braintree;
    @BindView(R.id.payumoney)
    TextView payumoney;
    @BindView(R.id.paytm)
    TextView paytm;
    private List<Card> cardsList = new ArrayList<>();
    private PaymentPresenter<PaymentActivity> presenter = new PaymentPresenter<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setTitle(getString(R.string.payment));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean hideCash = extras.getBoolean("hideCash", false);
            tvCash.setVisibility(hideCash ? View.GONE : View.VISIBLE);
        }

        if (isPayumoney) {
            payumoney.setVisibility(View.VISIBLE);
        } else {
            payumoney.setVisibility(View.GONE);
        }

        if (isPaytm) {
            paytm.setVisibility(View.VISIBLE);
        } else {
            paytm.setVisibility(View.GONE);
        }

        if (isBraintree) {
            braintree.setVisibility(View.VISIBLE);
        } else {
            braintree.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoading();
        new Handler().postDelayed(() -> {
            if (isCard) {
                cardsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                cardsRv.setItemAnimator(new DefaultItemAnimator());
                presenter.card();
                llCardContainer.setVisibility(View.VISIBLE);
            } else {
                try {
                    hideLoading();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                llCardContainer.setVisibility(View.GONE);
            }

            if (isCash && !isInvoiceCashToCard)
                llCashContainer.setVisibility(View.VISIBLE);
            else llCashContainer.setVisibility(View.GONE);
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @OnClick({R.id.cash, R.id.braintree, R.id.paytm, R.id.payumoney})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cash:
                finishResult("CASH");
                break;

            default:
                break;
        }
    }

    public void finishResult(String mode) {
        Intent intent = new Intent();
        intent.putExtra("payment_mode", mode);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void showCardChangeAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.are_sure_you_want_to_change_default_card))
                .setPositiveButton(getResources().getString(R.string.change_card),
                        (dialog, which) -> {
                            startActivity(new Intent(this, AddCardActivity.class));
                        })
                .setNegativeButton(getResources().getString(R.string.no),
                        (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onSuccess(Object card) {
        Toast.makeText(activity(), R.string.card_deleted_successfully, Toast.LENGTH_SHORT).show();
        presenter.card();
    }

    @Override
    public void onSuccess(List<Card> cards) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        cardsList.clear();
        cardsList.addAll(cards);
        cardsRv.setAdapter(new CardAdapter(cardsList));
    }

    @Override
    public void onError(Throwable e) {
        onErrorBase(e);
    }

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

        private List<Card> list;

        private CardAdapter(List<Card> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_card, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Card item = list.get(position);
            holder.card.setText(getString(R.string.card_) + item.getLastFour());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private RelativeLayout itemView;
            private TextView card;
            private TextView changeText;

            MyViewHolder(View view) {
                super(view);
                itemView = view.findViewById(R.id.item_view);
                itemView.setOnClickListener(this);
                card = view.findViewById(R.id.card);
                changeText = view.findViewById(R.id.text_change);
                changeText.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Card card = list.get(getAdapterPosition());
                switch (view.getId()) {
                    case R.id.item_view:
                        Intent intent = new Intent();
                        intent.putExtra("payment_mode", "CARD");
                        intent.putExtra("card_id", card.getCardId());
                        intent.putExtra("card_last_four", card.getLastFour());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        break;

                    case R.id.text_change:
                        showCardChangeAlert();
                        break;

                    default:
                        break;
                }
            }
        }
    }
}
