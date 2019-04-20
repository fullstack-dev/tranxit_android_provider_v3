package com.tranxitpro.partner.ui.fragment.offline;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseFragment;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.swipe_button.SwipeButton;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static com.tranxitpro.partner.common.fcm.MyFireBaseMessagingService.INTENT_FILTER;

public class OfflineFragment extends BaseFragment implements OfflineIView {

    private OfflinePresenter presenter = new OfflinePresenter();

    @BindView(R.id.menu_img)
    ImageView menuImg;
    @BindView(R.id.tvApprovalDesc)
    TextView tvApprovalDesc;
    @BindView(R.id.swipeBtnEnabled)
    SwipeButton swipeBtnEnabled;

    private DrawerLayout drawer;
    private Context context;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_offline;
    }

    @Override
    public View initView(View view) {
        ButterKnife.bind(this, view);
        this.context = getContext();
        presenter.attachView(this);
        drawer = activity().findViewById(R.id.drawer_layout);
        String s = getArguments().getString("status");
        if (!TextUtils.isEmpty(s))
            if (s.equalsIgnoreCase(Constants.User.Account.ONBOARDING))
                tvApprovalDesc.setVisibility(View.VISIBLE);
            else if (s.equalsIgnoreCase(Constants.User.Account.BANNED)) {
                tvApprovalDesc.setVisibility(View.VISIBLE);
                tvApprovalDesc.setText(getString(R.string.banned_desc));
            } else if (s.equalsIgnoreCase(Constants.User.Account.BALANCE)) {
                tvApprovalDesc.setVisibility(View.VISIBLE);
                tvApprovalDesc.setText(getString(R.string.low_balance));
                swipeBtnEnabled.setVisibility(View.INVISIBLE);
            }

        swipeBtnEnabled.setOnStateChangeListener(active -> {
            if (active) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("service_status", Constants.User.Service.ACTIVE);
                presenter.providerAvailable(map);
            }
        });
        return view;
    }

    @OnClick({R.id.menu_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_img:
                drawer.openDrawer(Gravity.START);
                break;
        }
    }

    @Override
    public void onSuccess(Object object) {
        try {
            JSONObject jsonObj = new JSONObject(new Gson().toJson(object));
            if (jsonObj.has("error"))
                Toasty.error(activity(), jsonObj.optString("error"), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(INTENT_FILTER));
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        swipeBtnEnabled.toggleState();
        if (e != null) try {
            onErrorBase(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
