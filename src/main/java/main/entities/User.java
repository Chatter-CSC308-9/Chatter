package main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class User {
    @JsonProperty("username")
    public String username;
    @JsonProperty("passwordplaintext")
    public String passwordplaintext;
    @JsonProperty("userID")
    public long userID;
    @JsonProperty("isGrader")
    public Boolean isGrader;
    @JsonProperty("email")
    public String email;
    @JsonProperty("emailIsHidden")
    public Boolean emailIsHidden;
    @JsonProperty("projects")
    public String[] projects;
    @JsonProperty("completedProjects")
    public String[] completedProjects;
    @JsonProperty("numProjects")
    public int numProjects;
    @JsonProperty("stripeId")
    public String stripeId;

    @Override
    public boolean equals(Object other){
        if (other == null){
            return false;
        }
        else{
            if (other instanceof User user){
                return user.userID == userID;
            }
            else{
                return false;
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }
}
