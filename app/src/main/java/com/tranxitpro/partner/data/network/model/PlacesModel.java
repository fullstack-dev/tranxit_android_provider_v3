package com.tranxitpro.partner.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lenovo on 24-02-2018.
 */

public class PlacesModel {

    @Expose
    @SerializedName("results")
    private List<ResultsEntity> results;

    public List<ResultsEntity> getResults() {
        return results;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public static class ResultsEntity {
        @Expose
        @SerializedName("vicinity")
        private String vicinity;
        @Expose
        @SerializedName("types")
        private List<String> types;
        @Expose
        @SerializedName("scope")
        private String scope;
        @Expose
        @SerializedName("reference")
        private String reference;
        @Expose
        @SerializedName("rating")
        private double rating;
        @Expose
        @SerializedName("price_level")
        private int priceLevel;
        @Expose
        @SerializedName("place_id")
        private String placeId;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private String id;
        @Expose
        @SerializedName("icon")
        private String icon;

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public int getPriceLevel() {
            return priceLevel;
        }

        public void setPriceLevel(int priceLevel) {
            this.priceLevel = priceLevel;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
