package com.wecode.letstalk.advisor;

import com.wecode.letstalk.configuration.Config;
import com.wecode.letstalk.repository.TimeFrameRepository;
import com.wecode.letstalk.repository.UserRepository;
import com.wecode.letstalk.utils.DateTimeUtil;

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
