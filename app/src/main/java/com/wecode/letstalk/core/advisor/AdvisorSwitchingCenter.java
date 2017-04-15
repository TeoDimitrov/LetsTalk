package com.wecode.letstalk.core.advisor;

import com.google.firebase.database.DataSnapshot;
import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.domain.schedule.WorkDay;
import com.wecode.letstalk.domain.user.User;
import com.wecode.letstalk.utils.DateTimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AdvisorSwitchingCenter {

    private Set<User> workingAdvisors;

    public AdvisorSwitchingCenter() {
        workingAdvisors = new HashSet<>();
    }

    private void addAvailableAdvisors(User advisor) {
        this.workingAdvisors.add(advisor);
    }

    public Set<User> getWorkingAdvisors(DataSnapshot advisors, Date startBookingDate) {
        for (DataSnapshot dataSnapshot : advisors.getChildren()) {
            User advisor = dataSnapshot.getValue(User.class);
            if (isAdvisorWorking(advisor, startBookingDate)) {
                addAvailableAdvisors(advisor);
            }
        }

        return this.workingAdvisors;
    }

    private boolean isAdvisorWorking(User advisor, Date startBookingDate) {
        boolean isAvailable = false;
        if(isAdvisor(advisor)){
            Date utcStartBookingDate = DateTimeUtil.getUTCDateTimeFromDate(startBookingDate);
            long utcStartBookingDay = Long.parseLong(new SimpleDateFormat("u").format(utcStartBookingDate));
            for (WorkDay workDay : advisor.getSchedule().getWorkDays()) {
                if(workDay.getId() == utcStartBookingDay && workDay.isEnabled()){
                    if(this.isTimeSlotAvailable(workDay, startBookingDate)){
                        isAvailable = true;
                    }
                }
            }
        }

        return isAvailable;
    }

    private boolean isAdvisor(User advisor) {
        boolean isAdvisor = false;
        if (advisor.getRole().getName().equals(Config.ADVISOR_ROLE) && advisor.getSchedule() != null) {
            isAdvisor = true;
        }

        return isAdvisor;
    }

    private boolean isTimeSlotAvailable(WorkDay workDay, Date startBookingDate){
        boolean isTimeSlotAvailable = false;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String workDayStartTime = timeFormat.format(workDay.getStartTime());
        String workDayEndTime = timeFormat.format(workDay.getEndTime());
        String startBookingDateString = timeFormat.format(startBookingDate);
        Date convertedWorkDayStartTime = null;
        Date convertedWorkDayEndTime = null;
        Date convertedStartBookingTime = null;
        try {
            convertedWorkDayStartTime = timeFormat.parse(workDayStartTime);
            convertedWorkDayEndTime = timeFormat.parse(workDayEndTime);
            convertedStartBookingTime = timeFormat.parse(startBookingDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(convertedWorkDayStartTime.before(convertedStartBookingTime) && convertedWorkDayEndTime.after(convertedStartBookingTime)){
            isTimeSlotAvailable = true;
        }

        return isTimeSlotAvailable;
    }
}
