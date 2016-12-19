package com.example.letstalk.callCenter;

import com.example.letstalk.configuration.Config;
import com.example.letstalk.repository.TimeFrameRepository;
import com.example.letstalk.repository.UserRepository;
import com.example.letstalk.utils.DateTimeUtil;

import java.util.Date;
import java.util.List;

public class AdvisorSwitchingCenter {

    private TimeFrameRepository timeFrameRepository;

    private UserRepository userRepository;

    public AdvisorSwitchingCenter() {
        this.timeFrameRepository = new TimeFrameRepository(Config.CHILD_TIMEFRAMES);
        this.userRepository = new UserRepository(Config.CHILD_USERS);
    }

    public String getAvailableAdvisor(Date startDate) {
        String utcDate = DateTimeUtil.getUTCDateTime(startDate);
        List<String> busyAdvisors = timeFrameRepository.findByUserDate(utcDate);

        return "Kai" + Config.USER_SUFIX;
    }

    private String getRandomAdvisor(List<String> busyAdvisors){
        return null;
    }
}
