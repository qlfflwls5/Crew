package com.example.crew.customClass;

public class MemberInfo {
    private String name;
    private String sex;
    private String phoneNumber;
    private String birthDay;
    public  String profileImageUrl;

    public MemberInfo(String name, String sex, String phoneNumber, String birthDay, String profileImageUrl){
        this.name = name;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.profileImageUrl = profileImageUrl;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public void setPhoneNumbere(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthDaye(){
        return this.birthDay;
    }
    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getsex(){
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProfileImageUrl() { return profileImageUrl;  }
    public void setProfileImageUrl(String profileImageUrl) {  this.profileImageUrl = profileImageUrl;  }
}
