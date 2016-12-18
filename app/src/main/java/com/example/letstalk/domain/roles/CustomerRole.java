package com.example.letstalk.domain.roles;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CustomerRole extends Role {

    @Exclude
    public static final String DEFAULT_ROLE_NAME = "CustomerRole";

    public CustomerRole() {
        super(DEFAULT_ROLE_NAME);
    }
}
