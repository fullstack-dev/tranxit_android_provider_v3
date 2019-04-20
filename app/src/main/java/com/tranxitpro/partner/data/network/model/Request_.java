package com.tranxitpro.partner.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request_ {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("provider_id")
    @Expose
    private int providerId;
    @SerializedName("current_provider_id")
    @Expose
    private int currentProviderId;
    @SerializedName("service_type_id")
    @Expose
    private int serviceTypeId;
    @SerializedName("rental_hours")
    @Expose
    private Object rentalHours;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cancelled_by")
    @Expose
    private String cancelledBy;
    @SerializedName("cancel_reason")
    @Expose
    private Object cancelReason;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("paid")
    @Expose
    private int paid;
    @SerializedName("is_track")
    @Expose
    private String isTrack;
    @SerializedName("distance")
    @Expose
    private double distance;
    @SerializedName("travel_time")
    @Expose
    private Object travelTime;
    @SerializedName("s_address")
    @Expose
    private String sAddress;
    @SerializedName("s_latitude")
    @Expose
    private double sLatitude;
    @SerializedName("s_longitude")
    @Expose
    private double sLongitude;
    @SerializedName("d_address")
    @Expose
    private String dAddress;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("d_latitude")
    @Expose
    private double dLatitude;
    @SerializedName("track_distance")
    @Expose
    private double trackDistance;
    @SerializedName("track_latitude")
    @Expose
    private double trackLatitude;
    @SerializedName("track_longitude")
    @Expose
    private double trackLongitude;
    @SerializedName("d_longitude")
    @Expose
    private double dLongitude;
    @SerializedName("assigned_at")
    @Expose
    private String assignedAt;
    @SerializedName("schedule_at")
    @Expose
    private String scheduleAt;
    @SerializedName("started_at")
    @Expose
    private Object startedAt;
    @SerializedName("finished_at")
    @Expose
    private Object finishedAt;
    @SerializedName("user_rated")
    @Expose
    private int userRated;
    @SerializedName("provider_rated")
    @Expose
    private int providerRated;
    @SerializedName("use_wallet")
    @Expose
    private int useWallet;
    @SerializedName("surge")
    @Expose
    private int surge;
    @SerializedName("route_key")
    @Expose
    private String routeKey;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("is_scheduled")
    @Expose
    private String isScheduled;
    @SerializedName("user")
    @Expose
    private UserGetResponse user;
    @SerializedName("payment")
    @Expose
    private Payment payment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public int getCurrentProviderId() {
        return currentProviderId;
    }

    public void setCurrentProviderId(int currentProviderId) {
        this.currentProviderId = currentProviderId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Object getRentalHours() {
        return rentalHours;
    }

    public void setRentalHours(Object rentalHours) {
        this.rentalHours = rentalHours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public Object getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Object cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public String getIsTrack() {
        return isTrack;
    }

    public void setIsTrack(String isTrack) {
        this.isTrack = isTrack;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Object getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Object travelTime) {
        this.travelTime = travelTime;
    }

    public String getSAddress() {
        return sAddress;
    }

    public void setSAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public double getSLatitude() {
        return sLatitude;
    }

    public void setSLatitude(double sLatitude) {
        this.sLatitude = sLatitude;
    }

    public double getSLongitude() {
        return sLongitude;
    }

    public void setSLongitude(double sLongitude) {
        this.sLongitude = sLongitude;
    }

    public String getDAddress() {
        return dAddress;
    }

    public void setDAddress(String dAddress) {
        this.dAddress = dAddress;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }


    public double getDLatitude() {
        return dLatitude;
    }

    public void setDLatitude(double dLatitude) {
        this.dLatitude = dLatitude;
    }

    public double getTrackDistance() {
        return trackDistance;
    }

    public void setTrackDistance(double trackDistance) {
        this.trackDistance = trackDistance;
    }

    public double getTrackLatitude() {
        return trackLatitude;
    }

    public void setTrackLatitude(double trackLatitude) {
        this.trackLatitude = trackLatitude;
    }

    public double getTrackLongitude() {
        return trackLongitude;
    }

    public void setTrackLongitude(double trackLongitude) {
        this.trackLongitude = trackLongitude;
    }

    public double getDLongitude() {
        return dLongitude;
    }

    public void setDLongitude(double dLongitude) {
        this.dLongitude = dLongitude;
    }

    public String getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(String assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getScheduleAt() {
        return scheduleAt;
    }

    public void setScheduleAt(String scheduleAt) {
        this.scheduleAt = scheduleAt;
    }

    public Object getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Object startedAt) {
        this.startedAt = startedAt;
    }

    public Object getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Object finishedAt) {
        this.finishedAt = finishedAt;
    }

    public int getUserRated() {
        return userRated;
    }

    public void setUserRated(int userRated) {
        this.userRated = userRated;
    }

    public int getProviderRated() {
        return providerRated;
    }

    public void setProviderRated(int providerRated) {
        this.providerRated = providerRated;
    }

    public int getUseWallet() {
        return useWallet;
    }

    public void setUseWallet(int useWallet) {
        this.useWallet = useWallet;
    }

    public int getSurge() {
        return surge;
    }

    public void setSurge(int surge) {
        this.surge = surge;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserGetResponse getUser() {
        return user;
    }

    public void setUser(UserGetResponse user) {
        this.user = user;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(String isScheduled) {
        this.isScheduled = isScheduled;
    }
}
