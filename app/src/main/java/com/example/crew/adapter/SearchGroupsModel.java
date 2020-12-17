package com.example.crew.adapter;

//그룹 검색기능에 활용될 리사이클러뷰 정보
public class SearchGroupsModel {

    private String name;
    private String info;
    private String profileUrl;

    public SearchGroupsModel() {}

    public SearchGroupsModel(String name, String info, String profileUrl){
        this.name = name;
        this.info = info;
        this.profileUrl = profileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
