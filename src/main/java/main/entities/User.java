package main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty("numProjects")
    public int numProjects;
}
