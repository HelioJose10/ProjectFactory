package com.example.techhelm;
public class UserData {
    private static UserData instance;
    private int userId;

    private UserData() {

    }

    public static UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
