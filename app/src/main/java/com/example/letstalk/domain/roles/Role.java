package com.example.letstalk.domain.roles;

/**
 * Created by teodo on 11/10/2016.
 */
public abstract class Role {

    private String name;

    protected Role(String name) {
        this.setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
