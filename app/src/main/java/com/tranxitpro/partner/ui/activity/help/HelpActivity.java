package com.tranxitpro.partner.ui.activity.help;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tranxitpro.partner.BuildConfig;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.data.network.model.Help;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpActivity extends BaseActivity implements
        HelpIView {


    private static final int REQUEST_CALL = 1;
    HelpPresenter presenter = new HelpPresenter();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imgCall)
    ImageView imgCall;
    @BindView(R.id.imgMail)
    ImageView imgMail;
    @BindView(R.id.imgWeb)
    ImageView imgWeb;
    Help help;

    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.help));
        presenter.getHelp();
        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(TELEPHONY_SERVICE);
        Objects.requireNonNull(telephonyManager).listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

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
    public void onSuccess(Help helpDetails) {
        help = helpDetails;
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }


    @OnClick({R.id.imgCall, R.id.imgMail, R.id.imgWeb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgCall:
                makeCall();
                break;
            case R.id.imgMail:
                String to = help.getContactEmail();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "-" + getString(R.string.help));
                intent.putExtra(Intent.EXTRA_TEXT, "Hello team");
                startActivity(Intent.createChooser(intent, "Send Email"));
                break;
            case R.id.imgWeb:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.BASE_URL));
                startActivity(browserIntent);
                break;
        }
    }

    private void makeCall() {
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall(help.getContactNumber());
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall(help.getContactNumber());
            } else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_CALL);
            }
        }
    }

    private void phoneCall(String contactNumber) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + contactNumber));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (permissions.length == 0) {
            return;
        }
        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if (!allPermissionsGranted) {
            boolean somePermissionsForeverDenied = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    //denied
                    Log.e("denied", permission);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", permission);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("You have forcefully denied some of the required permissions " +
                                "for this action. Please open settings, go to permissions and allow them.")
                        .setPositiveButton("Settings", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        } else {
            switch (requestCode) {
                case REQUEST_CALL:
                    allPermissionsGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    break;
            }
            if (allPermissionsGranted) {
                phoneCall(help.getContactNumber());
            } else {
                Toast.makeText(this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {
        String LOG_TAG = "LOGGING 123";
        private boolean isPhoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }
            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // ACTIVE
                Log.i(LOG_TAG, "OFFHOOK");
                isPhoneCalling = true;
            }
            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");
                if (isPhoneCalling) {
                    Log.i(LOG_TAG, "restart app");
                    isPhoneCalling = false;
                }
            }
        }
    }

    /*void makeCall(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    private boolean hasCallPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE);
    }

    @AfterPermissionGranted(Constants.RC_CALL_PHONE)
    public void callTask() {
        if (hasCallPermission()) {
            makeCall(DATUM_history_detail.getUser().getMobile());
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this, getString(R.string.call_permission),
                    Constants.RC_CALL_PHONE,
                    Manifest.permission.CALL_PHONE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }*/
}
