package com.example.mobileapp.model;

public class User {
    public String Email;
    public String UserName;
    public String UserPass;
    public String UserPhone;
    public String CreateDate;
    public String UpdatedDate;
    public String UserId;

    public User() {
    }

    public User(String email, String userPass) {
        Email = email;
        UserPass = userPass;
    }

    public User(String email, String userName, String userPass, String userPhone, String createDate, String updatedDate, String userId) {
        Email = email;
        UserName = userName;
        UserPass = userPass;
        UserPhone = userPhone;
        CreateDate = createDate;
        UpdatedDate = updatedDate;
        UserId = userId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPass() {
        return UserPass;
    }

    public void setUserPass(String userPass) {
        UserPass = userPass;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
