package com.tranxitpro.partner.ui.bottomsheetdialog.rating;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tranxitpro.partner.BuildConfig;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseFragment;
import com.tranxitpro.partner.data.network.model.Rating;
import com.tranxitpro.partner.data.network.model.Request_;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tranxitpro.partner.MvpApplication.DATUM;

public class RatingDialogFragment extends BaseFragment implements RatingDialogIView {

    @BindView(R.id.rate_with_txt)
    TextView rateWithTxt;
    @BindView(R.id.user_img)
    ImageView userImg;
    @BindView(R.id.user_rating)
    RatingBar userRating;
    @BindView(R.id.comments)
    EditText comments;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    Unbinder unbinder;

    RatingDialogPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_rating;
    }

    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        // setCancelable(false);
        presenter = new RatingDialogPresenter();
        presenter.attachView(this);
        init();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void init() {

        Request_ data = DATUM;

        try {
            rateWithTxt.setText(getString(R.string.rate_your_trip) + " " +
                    data.getUser().getFirstName() + " " + data.getUser().getLastName());
            userRating.setRating(Float.parseFloat(data.getUser().getRating()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (data.getUser().getPicture() != null)
                Glide.with(activity()).load(BuildConfig.BASE_IMAGE_URL +
                        data.getUser().getPicture()).
                        apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder).
                                dontAnimate().error(R.drawable.ic_user_placeholder)).into(userImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnSubmit)
    public void onViewClicked() {
        if (DATUM != null) {
            Request_ datum = DATUM;
            HashMap<String, Object> map = new HashMap<>();
            map.put("rating", Math.round(userRating.getRating()));
            map.put("comment", comments.getText().toString());
            showLoading();
            presenter.rate(map, datum.getId());

        }
    }

    @Override
    public void onSuccess(Rating rating) {
        hideLoading();
        activity().sendBroadcast(new Intent("INTENT_FILTER"));
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null) try {
            onErrorBase(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /*@Override
    public void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        setCancelable(false);
        presenter = new RatingDialogPresenter();
        presenter.attachView(this);
        init();

    }

    private void init() {

        Request_ data = DATUM;

        rateWithTxt.setText(getString(R.string.rate_your_trip)+ " " +
                data.getUser().getFirstName() + " " + data.getUser().getLastName());
        userRating.setRating(Float.parseFloat(data.getUser().getRating()));

        if (data.getUser().getPicture() != null)
            Glide.with(activity()).load(BuildConfig.BASE_IMAGE_URL +
                    data.getUser().getPicture()).
                    apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder).
                    dontAnimate().error(R.drawable.ic_user_placeholder)).into(userImg);
    }

    @OnClick(R.ID.btnSubmit)
    public void onViewClicked() {
        if (DATUM != null) {
            Request_ datum = DATUM;
            HashMap<String, Object> map = new HashMap<>();
            map.put("rating", Math.round(userRating.getRating()));
            map.put("comment", comments.getText().toString());
            showLoading();
            presenter.rate(map, datum.getId());

        }
    }

    @Override
    public void onSuccess(Rating rating) {
        dismissAllowingStateLoss();
        hideLoading();
        activity().sendBroadcast(new Intent("INTENT_FILTER"));
    }

    @Override
    public void onError(Throwable e) {

    }*/
}
