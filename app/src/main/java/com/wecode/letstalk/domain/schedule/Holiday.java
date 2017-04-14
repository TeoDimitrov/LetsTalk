package com.wecode.letstalk.domain.schedule;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Holiday implements Parcelable {

    private Date startDate;

    private Date endDate;

    public Holiday() {
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    protected Holiday(Parcel in) {
        long tmpStartDate = in.readLong();
        startDate = tmpStartDate != -1 ? new Date(tmpStartDate) : null;
        long tmpEndDate = in.readLong();
        endDate = tmpEndDate != -1 ? new Date(tmpEndDate) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startDate != null ? startDate.getTime() : -1L);
        dest.writeLong(endDate != null ? endDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Holiday> CREATOR = new Parcelable.Creator<Holiday>() {
        @Override
        public Holiday createFromParcel(Parcel in) {
            return new Holiday(in);
        }

        @Override
        public Holiday[] newArray(int size) {
            return new Holiday[size];
        }
    };
}