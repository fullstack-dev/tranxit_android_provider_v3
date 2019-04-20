package com.tranxitpro.partner.ui.fragment.past;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tranxitpro.partner.MvpApplication;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.base.BaseFragment;
import com.tranxitpro.partner.common.Utilities;
import com.tranxitpro.partner.data.network.model.HistoryList;
import com.tranxitpro.partner.ui.activity.past_detail.PastTripDetailActivity;
import com.tranxitpro.partner.ui.adapter.PastTripAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PastTripFragment extends BaseFragment implements PastTripIView, PastTripAdapter.ClickListener {

    @BindView(R.id.past_trip_rv)
    RecyclerView pastTripRv;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    Unbinder unbinder;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_error)
    TextView error;
    private List<HistoryList> list = new ArrayList<>();
    private PastTripPresenter presenter = new PastTripPresenter();
    private Context context;

    public PastTripFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_past_trip;
    }

    @Override
    public View initView(View view) {

        unbinder = ButterKnife.bind(this, view);
        this.context = getContext();
        presenter.attachView(this);

        error.setText(getString(R.string.no_past_found));
        pastTripRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        pastTripRv.setItemAnimator(new DefaultItemAnimator());
        pastTripRv.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);
        presenter.getHistory();
        return view;
    }

    @Override
    public void onSuccess(List<HistoryList> historyList) {

        Utilities.printV("SIZE", historyList.size() + "");
        progressBar.setVisibility(View.GONE);
        list.clear();
        list.addAll(historyList);
        loadAdapter();
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        progressBar.setVisibility(View.GONE);
        if (e != null) try {
            onErrorBase(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void loadAdapter() {
        if (list.size() > 0) {
            PastTripAdapter adapter = new PastTripAdapter(list, context);
            adapter.setClickListener(this);
            pastTripRv.setAdapter(adapter);
            pastTripRv.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        } else {
            pastTripRv.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void redirectClick(HistoryList historyList, ImageView staticMap) {
        MvpApplication.DATUM_history = historyList;
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity(), staticMap,
                        ViewCompat.getTransitionName(staticMap));
        startActivity(new Intent(context, PastTripDetailActivity.class), options.toBundle());
    }
}
