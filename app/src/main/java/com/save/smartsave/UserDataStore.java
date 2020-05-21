package com.save.smartsave;

public class UserDataStore {
    String name,email,info;

    public UserDataStore(String name, String email, String info) {
        this.name = name;
        this.email = email;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getInfo() {
        return info;
    }
}
