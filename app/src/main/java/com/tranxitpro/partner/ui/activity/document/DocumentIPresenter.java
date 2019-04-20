package com.tranxitpro.partner.ui.activity.document;

import com.tranxitpro.partner.base.MvpPresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface DocumentIPresenter<V extends DocumentIView> extends MvpPresenter<V> {

    void getDocuments();

    void postUploadDocuments(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> files);

    void logout(HashMap<String, Object> obj);
}
