package com.tranxitpro.partner.data.network.model;

import android.net.Uri;
import java.io.File;

public class Document {

    private int id;
    transient private String idVal;
    transient private File imgFile;
    private String name;
    private String type;

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }

    transient private Uri imgUri;
    private ProviderDocuments providerdocuments;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ProviderDocuments getProviderDocuments() {
        return this.providerdocuments;
    }

    public void setProviderDocuments(ProviderDocuments providerDocuments) {
        this.providerdocuments = providerDocuments;
    }

    public String getIdVal() {
        return idVal;
    }

    public void setIdVal(String idVal) {
        this.idVal = idVal;
    }

    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }
}
