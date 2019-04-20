package com.tranxitpro.partner.data.network.model;

public class TimerResponse {

    private int waitingStatus;
    private long waitingTime;

    public TimerResponse() {
    }

    public int getWaitingStatus() {
        return waitingStatus;
    }

    public void setWaitingStatus(int waitingStatus) {
        this.waitingStatus = waitingStatus;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }
}
