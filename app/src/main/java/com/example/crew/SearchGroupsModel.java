package com.example.crew;

//그룹 검색기능에 활용될 리사이클러뷰 정보
public class SearchGroupsModel {

    private String name;
    private String info;

    private SearchGroupsModel() {}

    private SearchGroupsModel(String name, String info){
        this.name = name;
        this.info = info;
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
}
