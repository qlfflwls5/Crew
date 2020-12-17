package com.example.crew.customClass;

import java.util.Comparator;

public class GroupMembersInfo {
    private String name;
    private String position;
    private String profileUrl;
    private int positionIndex;





    public GroupMembersInfo(String name, String position, int positionIndex, String profileUrl){
        this.name = name;
        this.position = position;
        this.positionIndex = positionIndex;
        this.profileUrl = profileUrl;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public int getPositionIndex() { return positionIndex;  }
    public void setPositionIndex(int positionIndex) { this.positionIndex = positionIndex;  }

    public String getProfileUrl() { return profileUrl; }
    public void setProfileUrl(String profileUrl) { this.profileUrl = profileUrl; }
}
