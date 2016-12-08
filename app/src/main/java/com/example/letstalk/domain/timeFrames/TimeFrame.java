package com.example.letstalk.domain.timeFrames;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.letstalk.utils.DateTimeUtil;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class TimeFrame implements Parcelable {

    private String startDateTime;

    private String endDateTime;

    private TimeFrameType type;

    private String bookedUserName;

    private TimeFrameStatus status;

    @SuppressWarnings("unused")
    public TimeFrame() {
        this.setStatus(TimeFrameStatus.UNCONFIRMED);
    }

    public TimeFrame(Date startDateTime, Date endDateTime, TimeFrameType timeFrameType, String bookedUserName) {
        this();
        this.setUTCStartDateTime(startDateTime);
        this.setUTCEndDateTime(endDateTime);
        this.setType(timeFrameType);
        this.setBookedUserName(bookedUserName);
    }

    public String getStartDateTime() {
        return this.startDateTime;
    }

    @Exclude
    public String getLocalStartDateTime() {
        String localTime = DateTimeUtil.getLocalTime(this.startDateTime);
        return localTime;
    }

    @Exclude
    public void setUTCStartDateTime(Date startDateTime) {
        String utcDate = DateTimeUtil.getUTCTime(startDateTime);
        this.startDateTime = utcDate;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return this.endDateTime;
    }

    @Exclude
    public String getLocalEndDateTime() {
        String localTime = DateTimeUtil.getLocalTime(this.endDateTime);
        return localTime;
    }

    @Exclude
    public void setUTCEndDateTime(Date endDateTime) {
        String utcDate = DateTimeUtil.getUTCTime(endDateTime);
        this.endDateTime = utcDate;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public TimeFrameType getType() {
        return this.type;
    }

    public void setType(TimeFrameType type) {
        this.type = type;
    }

    public String getBookedUserName() {
        return this.bookedUserName;
    }

    public void setBookedUserName(String bookedUserName) {
        this.bookedUserName = bookedUserName;
    }

    public TimeFrameStatus getStatus() {
        return this.status;
    }

    public void setStatus(TimeFrameStatus status) {
        this.status = status;
    }

    protected TimeFrame(Parcel in) {
        startDateTime = in.readString();
        endDateTime = in.readString();
        type = (TimeFrameType) in.readValue(TimeFrameType.class.getClassLoader());
        bookedUserName = in.readString();
        status = (TimeFrameStatus) in.readValue(TimeFrameStatus.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startDateTime);
        dest.writeString(endDateTime);
        dest.writeValue(type);
        dest.writeString(bookedUserName);
        dest.writeValue(status);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TimeFrame> CREATOR = new Parcelable.Creator<TimeFrame>() {
        @Override
        public TimeFrame createFromParcel(Parcel in) {
            return new TimeFrame(in);
        }

        @Override
        public TimeFrame[] newArray(int size) {
            return new TimeFrame[size];
        }
    };
}
