package com.wecode.letstalk.domain.schedule;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class WorkDay implements Parcelable {

    private long id;

    private String name;

    private Date startTime;

    private Date endTime;

    private boolean isEnabled;

    @SuppressWarnings("unused")
    public WorkDay() {
    }

    public WorkDay(long id, String name, Date startTime, Date endTime, boolean isEnabled) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isEnabled = isEnabled;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    protected WorkDay(Parcel in) {
        id = in.readLong();
        name = in.readString();
        long tmpStartTime = in.readLong();
        startTime = tmpStartTime != -1 ? new Date(tmpStartTime) : null;
        long tmpEndTime = in.readLong();
        endTime = tmpEndTime != -1 ? new Date(tmpEndTime) : null;
        isEnabled = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeLong(startTime != null ? startTime.getTime() : -1L);
        dest.writeLong(endTime != null ? endTime.getTime() : -1L);
        dest.writeByte((byte) (isEnabled ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WorkDay> CREATOR = new Parcelable.Creator<WorkDay>() {
        @Override
        public WorkDay createFromParcel(Parcel in) {
            return new WorkDay(in);
        }

        @Override
        public WorkDay[] newArray(int size) {
            return new WorkDay[size];
        }
    };
}