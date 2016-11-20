package com.example.letstalk.domain.roles;

public class Role {

    private String name;

    protected Role(){}

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
