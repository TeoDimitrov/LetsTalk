package com.wecode.letstalk.domain.schedule;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Schedule implements Parcelable {

    private List<WorkDay> workDays;

    private Holiday holiday;

    public Schedule() {
        this.setWorkDays(new ArrayList<WorkDay>());
        loadWorkDays();
    }

    public List<WorkDay> getWorkDays() {
        return this.workDays;
    }

    public void setWorkDays(List<WorkDay> workDays) {
        this.workDays = workDays;
    }

    public Holiday getHoliday() {
        return this.holiday;
    }

    public void setHoliday(Holiday holiday) {
        this.holiday = holiday;
    }

    private void loadWorkDays() {
        String[] dayNames = new String[]{"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.HOUR_OF_DAY, 9);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.HOUR_OF_DAY, 18);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.SECOND, 0);
        boolean isEnabled = true;
        for (int i = 0; i < dayNames.length; i++) {
            long id = i + 1;
            String dayName = dayNames[i];
            this.getWorkDays().add(new WorkDay(id, dayName, startDate.getTime(), endDate.getTime(), isEnabled));
        }
    }

    protected Schedule(Parcel in) {
        if (in.readByte() == 0x01) {
            workDays = new ArrayList<WorkDay>();
            in.readList(workDays, WorkDay.class.getClassLoader());
        } else {
            workDays = null;
        }
        holiday = (Holiday) in.readValue(Holiday.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (workDays == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(workDays);
        }
        dest.writeValue(holiday);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };
}
