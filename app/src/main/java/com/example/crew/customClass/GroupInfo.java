package com.example.crew.customClass;

public class GroupInfo {
    private String name;
    private String attr;
    private String info;
    private String groupProfileUrl;

    public GroupInfo(String name, String attr, String info, String groupProfileUrl){
        this.name = name;
        this.attr = attr;
        this.info = info;
        this.groupProfileUrl = groupProfileUrl;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getInfo(){
        return this.info;
    }
    public void setInfo(String info) {
        this.info = info;
    }

    public String getAttr(){ return this.attr; }
    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getGroupProfileUrl() { return groupProfileUrl;  }
    public void setGroupProfileUrl(String groupProfileUrl) { this.groupProfileUrl = groupProfileUrl;  }
}
