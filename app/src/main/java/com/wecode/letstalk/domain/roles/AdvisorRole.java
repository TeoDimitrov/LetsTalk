package com.wecode.letstalk.domain.roles;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AdvisorRole extends Role {

    @Exclude
    public static final String DEFAULT_ROLE_NAME = "AdvisorRole";

    public AdvisorRole() {
        super(DEFAULT_ROLE_NAME);
    }
}
