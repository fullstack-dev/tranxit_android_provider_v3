package com.tranxitpro.partner.ui.activity.card;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import es.dmoral.toasty.Toasty;

public class CardActivity extends BaseActivity implements CardIView {

    @BindView(R.id.cards_rv)
    RecyclerView cardsRv;
    @BindView(R.id.llCardContainer)
    LinearLayout llCardContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    List<Card> cardsList = new ArrayList<>();
    private CardPresenter<CardActivity> presenter = new CardPresenter<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_card;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.card));
        CardAdapter adapter = new CardAdapter(cardsList);
        cardsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cardsRv.setAdapter(adapter);
    }

    @Override
    public void onSuccess(Object card) {
        hideLoading();
        Toasty.success(activity(), getString(R.string.card_deleted_successfully), Toast.LENGTH_SHORT).show();
        showLoading();
        presenter.card();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.card();
    }

    @Override
    public void onSuccess(List<Card> cards) {
        hideLoading();
        cardsList.clear();
        cardsList.addAll(cards);
        cardsRv.setAdapter(new CardAdapter(cardsList));
    }


    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @Override
    public void onSuccessChangeCard(Object card) {
        hideLoading();
        Toasty.success(this, card.toString(), Toast.LENGTH_SHORT).show();
        showLoading();
        presenter.card();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

        private List<Card> list;

        CardAdapter(List<Card> list) {
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
                card = view.findViewById(R.id.card);
                changeText = view.findViewById(R.id.text_change);
                changeText.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                Card card = list.get(position);
                switch (view.getId()) {
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
