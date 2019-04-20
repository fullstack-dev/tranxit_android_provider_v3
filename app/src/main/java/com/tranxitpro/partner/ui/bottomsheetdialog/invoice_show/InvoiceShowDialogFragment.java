package com.tranxitpro.partner.ui.bottomsheetdialog.invoice_show;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tranxitpro.partner.MvpApplication;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseBottomSheetDialogFragment;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.data.network.model.Payment;
import com.tranxitpro.partner.data.network.model.ServiceType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tranxitpro.partner.MvpApplication.DATUM_history_detail;

public class InvoiceShowDialogFragment extends BaseBottomSheetDialogFragment {

    Unbinder unbinder;

    @BindView(R.id.btnClose)
    Button btnClose;
    @BindView(R.id.lblBookingid)
    TextView lblBookingid;
    @BindView(R.id.lblDistanceTravelled)
    TextView lblDistanceTravelled;
    @BindView(R.id.lblTimetaken)
    TextView lblTimetaken;
    @BindView(R.id.lblBasreFare)
    TextView lblBasreFare;
    @BindView(R.id.lblDistanceFare)
    TextView lblDistanceFare;
    @BindView(R.id.lblTax)
    TextView lblTax;
    @BindView(R.id.lblTips)
    TextView lblTips;
    @BindView(R.id.lblTotal)
    TextView lblTotal;
    @BindView(R.id.distance_container)
    LinearLayout distanceContainer;
    @BindView(R.id.lblTimeFare)
    TextView lblTimeFare;
    @BindView(R.id.time_container)
    LinearLayout timeContainer;
    @BindView(R.id.tips_layout)
    LinearLayout tipsLayout;
    @BindView(R.id.llAmountToBePaid)
    LinearLayout llAmountToBePaid;
    @BindView(R.id.lblPaid)
    TextView lblPaid;

    public InvoiceShowDialogFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_invoice_show_dialog;
    }

    @Override
    public void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        if (DATUM_history_detail != null) {
            lblBookingid.setText(DATUM_history_detail.getBookingId());
            lblDistanceTravelled.setText(DATUM_history_detail.getDistance() + "");
            lblTimetaken.setText(DATUM_history_detail.getTravelTime() + "");

            Payment payment = DATUM_history_detail.getPayment();
            if (payment != null) {
                lblBasreFare.setText((Constants.Currency + " " +
                        MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble(payment.getFixed() + ""))));
                lblTax.setText((Constants.Currency + " " +
                        MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble(payment.getTax() + ""))));
                if (payment.getTips() == 0 || payment.getTips() == 0.0)
                    tipsLayout.setVisibility(View.GONE);
                else {
                    tipsLayout.setVisibility(View.VISIBLE);
                    lblTips.setText((Constants.Currency + " " +
                            MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble(payment.getTips() + ""))));
                }
                Double pastTripTotal = payment.getTotal() + payment.getTips();
                lblTotal.setText((Constants.Currency + " " +
                        MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble(pastTripTotal + ""))));


                if (payment.getPayable() > 0) {
                    llAmountToBePaid.setVisibility(View.VISIBLE);
                    lblPaid.setText((Constants.Currency + " " +
                            MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble(payment.getPayable() + ""))));
                } else llAmountToBePaid.setVisibility(View.GONE);

                ServiceType serviceType = DATUM_history_detail.getServiceType();
                if (serviceType != null) {
                    String serviceCalculator = serviceType.getCalculator();
                    switch (serviceCalculator) {
                        case Constants.InvoiceFare.MIN:
                            distanceContainer.setVisibility(View.GONE);
                            timeContainer.setVisibility(View.GONE);
                            lblTimeFare.setText(Constants.Currency + " " +
                                    MvpApplication.getInstance().getNewNumberFormat(payment.getMinute()));
                            break;
                        case Constants.InvoiceFare.HOUR:
                            distanceContainer.setVisibility(View.GONE);
                            timeContainer.setVisibility(View.GONE);
                            lblTimeFare.setText(Constants.Currency + " " +
                                    MvpApplication.getInstance().getNewNumberFormat(payment.getHour()));
                            break;
                        case Constants.InvoiceFare.DISTANCE:
                            distanceContainer.setVisibility(View.GONE);
                            timeContainer.setVisibility(View.GONE);
                            lblDistanceFare.setText(Constants.Currency + " " +
                                    MvpApplication.getInstance().getNewNumberFormat(payment.getDistance()));
                            break;
                        case Constants.InvoiceFare.DISTANCEMIN:
                            distanceContainer.setVisibility(View.GONE);
                            timeContainer.setVisibility(View.GONE);
                            lblTimeFare.setText(Constants.Currency + " " +
                                    MvpApplication.getInstance().getNewNumberFormat(payment.getMinute()));
                            lblDistanceFare.setText(Constants.Currency + " " +
                                    MvpApplication.getInstance().getNewNumberFormat(payment.getDistance()));
                            break;
                        case Constants.InvoiceFare.DISTANCEHOUR:
                            distanceContainer.setVisibility(View.GONE);
                            timeContainer.setVisibility(View.GONE);
                            lblTimeFare.setText(Constants.Currency + " " +
                                    MvpApplication.getInstance().getNewNumberFormat(payment.getHour()));
                            lblDistanceFare.setText(Constants.Currency + " " +
                                    MvpApplication.getInstance().getNewNumberFormat(payment.getDistance()));
                            break;
                        default:
                            break;
                    }
                }
                lblDistanceFare.setText((Constants.Currency + " " +
                        MvpApplication.getInstance().getNewNumberFormat(Double.parseDouble(payment.getDistance() + ""))));
            }
        }
    }

    @OnClick(R.id.btnClose)
    public void onViewClicked() {
        dismissAllowingStateLoss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
