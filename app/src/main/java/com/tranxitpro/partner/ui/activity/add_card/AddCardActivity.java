package com.tranxitpro.partner.ui.activity.add_card;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.SharedHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class AddCardActivity extends BaseActivity implements AddCardIView {

    /**
     * Test card
     * 5200 8282 8282 8210
     * 4000 0566 5566 5556
     */
    @BindView(R.id.card_form)
    CardForm cardForm;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private AddCardPresenter<AddCardActivity> presenter = new AddCardPresenter<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_card;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.change_card_for_payments));
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .actionLabel(getString(R.string.add_card_details))
                .setup(this);
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        if (cardForm.getCardNumber().isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_card_number), Toast.LENGTH_SHORT).show();
            return;
        }
        if (cardForm.getExpirationMonth().isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_card_expiration_details), Toast.LENGTH_SHORT).show();
            return;
        }
        if (cardForm.getCvv().isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_card_cvv), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isDigitsOnly(cardForm.getExpirationMonth()) || !TextUtils.isDigitsOnly(cardForm.getExpirationYear())) {
            Toast.makeText(this, getString(R.string.please_enter_card_expiration_details), Toast.LENGTH_SHORT).show();
            return;
        }

        String cardNumber = cardForm.getCardNumber();
        int cardMonth = Integer.parseInt(cardForm.getExpirationMonth());
        int cardYear = Integer.parseInt(cardForm.getExpirationYear());
        String cardCvv = cardForm.getCvv();
        Log.d("CARD", "CardDetails Number: " + cardNumber + "Month: " + cardMonth + " Year: " + cardYear + " Cvv " + cardCvv);
        Card card = new Card(cardNumber, cardMonth, cardYear, cardCvv);
        card.setCurrency("usd");
        if (TextUtils.isEmpty(SharedHelper.getKey(this, Constants.SharedPref.STRIPE_PUBLISHABLE_KEY)))
            showAToast(getString(R.string.stripe_key_missing));
        else addCard(card);
    }

    @Override
    public void onSuccess(Object card) {
        hideLoading();
        Toast.makeText(this, getString(R.string.card_added), Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    private void addCard(Card card) {
        showLoading();
        Stripe stripe = new Stripe(this, SharedHelper.getKey(this, Constants.SharedPref.STRIPE_PUBLISHABLE_KEY));
        stripe.createToken(card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        hideLoading();
                        Log.d("CARD:", " " + token.getId());
                        Log.d("CARD:", " " + token.getCard().getLast4());
                        String stripeToken = token.getId();
                        showLoading();
                        presenter.addCard(stripeToken);
                    }

                    public void onError(Exception error) {
                        hideLoading();
                        Toasty.error(getApplicationContext(), error.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
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
}
