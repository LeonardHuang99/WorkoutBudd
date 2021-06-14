package com.indestructibles.workoutbudd.Matches;

public class MatchesObject {

    private String userId;
    private String Name;
    private String profileImageUrl;

    public MatchesObject (String userId, String Name, String profileImageUrl){
        this.userId = userId;
        this.Name = Name;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getName(){
        return Name;
    }
    public void setName(String Name){
        this.Name = Name;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

}
