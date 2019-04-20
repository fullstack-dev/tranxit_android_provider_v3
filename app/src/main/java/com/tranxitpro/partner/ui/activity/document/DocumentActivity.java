package com.tranxitpro.partner.ui.activity.document;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tranxitpro.partner.BuildConfig;
import com.tranxitpro.partner.R;
import com.tranxitpro.partner.base.BaseActivity;
import com.tranxitpro.partner.common.Constants;
import com.tranxitpro.partner.common.SharedHelper;
import com.tranxitpro.partner.common.Utilities;
import com.tranxitpro.partner.data.network.model.Document;
import com.tranxitpro.partner.data.network.model.DriverDocumentResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.tranxitpro.partner.common.Constants.MULTIPLE_PERMISSION;
import static com.tranxitpro.partner.common.Constants.RC_MULTIPLE_PERMISSION_CODE;

public class DocumentActivity extends BaseActivity implements DocumentIView, EasyPermissions.PermissionCallbacks {

    public static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 123;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvDocuments)
    RecyclerView rvDocuments;
    @BindView(R.id.tvNoDocument)
    TextView tvNoDocument;
    private DocumentPresenter presenter = new DocumentPresenter();
    private List<Document> documents;
    private DocumentAdapter mAdapter;
    private boolean isFromSettings = false;
    private int adapterPos;
    private String setting;

    @Override
    public int getLayoutId() {
        return R.layout.activity_document;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.walletTransactions));
        try {
            setting = getIntent().getStringExtra("setting");
            isFromSettings = getIntent().getExtras().getBoolean("isFromSettings");
        } catch (Exception e) {
            e.printStackTrace();
            isFromSettings = false;
        }
        documents = new ArrayList<>();
        showLoading();
        presenter.getDocuments();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (setting != null && !setting.equalsIgnoreCase("") &&
                        setting.equalsIgnoreCase("isClick")) {
                    onBackPressed();
                } else {
                    showPopUp();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showPopUp() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DocumentActivity.this);

        alertDialogBuilder
                .setMessage(getString(R.string.log_out_title))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), (dialog, id) -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", SharedHelper.getKey(activity(),
                            Constants.SharedPref.USER_ID) + "");
                    presenter.logout(map);
                }).setNegativeButton(getString(R.string.cancel), (dialog, id) -> {
            String user_id = SharedHelper.getKey(activity(), Constants.SharedPref.USER_ID);
            Utilities.printV("USER_ID===>", user_id);
            dialog.cancel();
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @OnClick(R.id.btProceed)
    public void onViewClicked() {
        showLoading();
        if (isFromSettings) {
            boolean canHitApi = false;
            for (Document document : documents)
                if (!TextUtils.isEmpty(document.getIdVal()) || document.getImgFile() != null)
                    canHitApi = true;
            if (canHitApi) {
                Map<String, RequestBody> params = new HashMap<>();
                List<MultipartBody.Part> file = new ArrayList<>();
                for (int i = 0; i < documents.size(); i++)
                    if (documents.get(i).getImgFile() != null) {
                        params.put("id[" + params.size() + "]", toRequestBody(String.valueOf(documents.get(i).getId())));
                        file.add(MultipartBody.Part.createFormData("document[" + file.size() + "]", documents.get(i).getImgFile().getName(),
                                RequestBody.create(MediaType.parse("image/*"), documents.get(i).getImgFile())));
                    }
                presenter.postUploadDocuments(params, file);
            } else {
                hideLoading();
                Toasty.error(this, getString(R.string.no_document_changed), Toast.LENGTH_SHORT, true).show();
            }
        } else {
            boolean canHitApi = true;
            for (Document document : documents)
                if (TextUtils.isEmpty(document.getIdVal()) || document.getImgFile() == null)
                    canHitApi = false;

            if (canHitApi) {
                Map<String, RequestBody> params = new HashMap<>();
                List<MultipartBody.Part> file = new ArrayList<>();
                for (int i = 0; i < documents.size(); i++)
                    if (documents.get(adapterPos).getImgFile() != null) {
                        params.put("id[" + i + "]", toRequestBody(String.valueOf(documents.get(i).getId())));
                        if (documents.get(i).getImgFile() != null) {
                            file.add(MultipartBody.Part.createFormData("document[" + i + "]",
                                    documents.get(i).getImgFile().getName(),
                                    RequestBody.create(MediaType.parse("image/*"), documents.get(i).getImgFile())));
                        }
                    }
                presenter.postUploadDocuments(params, file);
            } else {
                hideLoading();
                Toasty.error(this, getString(R.string.add_all_documents), Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    @Override
    public void onSuccess(DriverDocumentResponse response) {
        documents = response.getDocuments();
        mAdapter = new DocumentAdapter(documents);
        if (documents.size() > 0) {
            rvDocuments.setLayoutManager(new LinearLayoutManager(activity(), LinearLayoutManager.VERTICAL, false));
            rvDocuments.setAdapter(mAdapter);
            tvNoDocument.setVisibility(View.INVISIBLE);
            rvDocuments.setVisibility(View.VISIBLE);
        } else {
            tvNoDocument.setVisibility(View.VISIBLE);
            rvDocuments.setVisibility(View.INVISIBLE);
        }
        hideLoading();
    }

    @Override
    public void onDocumentSuccess(DriverDocumentResponse response) {
        if (!isFromSettings) finish();
        else {
            documents = response.getDocuments();
            mAdapter.setDocuments(documents);
            mAdapter.notifyDataSetChanged();
        }
        hideLoading();
    }

    @AfterPermissionGranted(RC_MULTIPLE_PERMISSION_CODE)
    public void MultiplePermissionTask(int pos) {
        this.adapterPos = pos;
        if (hasMultiplePermission()) pickImage();
        else EasyPermissions.requestPermissions(
                this, getString(R.string.please_accept_permission),
                RC_MULTIPLE_PERMISSION_CODE,
                MULTIPLE_PERMISSION);
    }

    private boolean hasMultiplePermission() {
        return EasyPermissions.hasPermissions(this, MULTIPLE_PERMISSION);
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @Override
    public void onSuccessLogout(Object object) {
        Utilities.LogoutApp(activity(), "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            EasyImage.handleActivityResult(requestCode, resultCode, data, DocumentActivity.this, new DefaultCallback() {
                        @Override
                        public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                            setImageToView(imageFiles.get(0));
                        }
                    });
        } catch (Exception e) {
            Toasty.error(this, getString(R.string.invalid_img_file), Toast.LENGTH_SHORT, true).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (permissions.length == 0) return;
        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) for (int grantResult : grantResults)
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setTitle("Permissions Required")
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
                case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0) {
                        boolean permission1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean permission2 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        if (permission1 && permission2) pickImage();
                        else
                            Toast.makeText(getApplicationContext(), "Please give permission", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private void setImageToView(@NonNull File file) {
        try {
            File compressedImageFile = new Compressor(this).compressToFile(file);
            documents.get(adapterPos).setImgFile(compressedImageFile);
            mAdapter.setDocuments(documents);
            documents.get(adapterPos).setIdVal("id[" + adapterPos + "]");
            mAdapter.notifyItemChanged(adapterPos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {

        private List<Document> mDocuments;
        private Context mContext;

        DocumentAdapter(List<Document> documents) {
            this.mDocuments = documents;
        }

        void setDocuments(List<Document> mDocuments) {
            this.mDocuments = mDocuments;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_document, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.tvDocTitle.setText(mDocuments.get(position).getName());
            if (mDocuments.get(position).getImgFile() != null) {
                Glide.with(activity())
                        .load(Uri.fromFile(mDocuments.get(position).getImgFile()))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_document_placeholder).
                                diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .dontAnimate().error(R.drawable.ic_document_placeholder))
                        .into(holder.ivDocument);
                holder.btAddDocument.setText(mContext.getText(R.string.edit));
            } else if (mDocuments.get(position).getProviderDocuments() != null) {
                Glide.with(activity())
                        .load(BuildConfig.BASE_IMAGE_URL + mDocuments.get(position).getProviderDocuments().getUrl())
                        .apply(RequestOptions.placeholderOf(null).diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).dontAnimate())
                        .into(holder.ivDocument);
                holder.btAddDocument.setText(mContext.getText(R.string.edit));
            }
        }

        @Override
        public int getItemCount() {
            return mDocuments.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tvDocTitle)
            TextView tvDocTitle;
            @BindView(R.id.ivDocument)
            ImageView ivDocument;
            @BindView(R.id.btAddDocument)
            Button btAddDocument;

            MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, itemView);
            }

            @OnClick(R.id.btAddDocument)
            public void onViewClicked() {
                MultiplePermissionTask(getAdapterPosition());
            }
        }
    }
}