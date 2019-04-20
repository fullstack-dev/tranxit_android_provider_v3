package com.tranxitpro.partner.ui.activity.summary;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tranxitpro.partner.MvpApplication;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.Utilities;
import com.tranxitpro.partner.data.network.model.Summary;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SummaryActivity extends BaseActivity implements SummaryIView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.card_layout)
    LinearLayout cardLayout;
    @BindView(R.id.rides_value)
    TextView ridesValue;
    @BindView(R.id.rides_card)
    CardView ridesCard;
    @BindView(R.id.revenue_value)
    TextView revenueValue;
    @BindView(R.id.revenue_card)
    CardView revenueCard;
    @BindView(R.id.scheduled_value)
    TextView scheduledValue;
    @BindView(R.id.scheduled_card)
    CardView scheduledCard;
    @BindView(R.id.canceled_value)
    TextView canceledValue;
    @BindView(R.id.canceled_card)
    CardView canceledCard;

    private SummaryPresenter presenter = new SummaryPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_summary;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.summary));
        presenter.getSummary();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSuccess(Summary summary) {

        Utilities.printV("onSuccess==>", summary.getCancelRides() + "");
        // summary.setRevenue(210);
        Animation slideUp = AnimationUtils.loadAnimation(activity(), R.anim.slide_up_slow);
        cardLayout.startAnimation(slideUp);
        cardLayout.setVisibility(View.VISIBLE);

        slideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Un Used
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    animateTextView(0, summary.getRides(), ridesValue);
                    animateTextView(0, summary.getCancelRides(), canceledValue);
                    animateTextView(0, summary.getScheduledRides(), scheduledValue);
                    animateTextViewFloat(0.0f, summary.getRevenue(), revenueValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Un Used
            }
        });

    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @SuppressLint("SetTextI18n")
    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(700);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            if (textview.getId() == R.id.revenue_value)
                textview.setText(Constants.Currency + " " + valueAnimator1.getAnimatedValue().toString());
            else
                textview.setText(valueAnimator1.getAnimatedValue().toString());
        });
        valueAnimator.start();

    }


    @SuppressLint("SetTextI18n")
    public void animateTextViewFloat(float initialValue, float finalValue, final TextView textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(initialValue, finalValue);
        valueAnimator.setDuration(700);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            if (textview.getId() == R.id.revenue_value)
                textview.setText(Constants.Currency + " " + MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble
                        (valueAnimator1.getAnimatedValue().toString())));
            else
                textview.setText(MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble
                        (valueAnimator1.getAnimatedValue().toString())));
        });
        valueAnimator.start();

    }

}
