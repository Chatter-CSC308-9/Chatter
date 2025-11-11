package main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Project {
    @JsonProperty("learnerID")
    public long learnerID;
    @JsonProperty("graderID")
    public long graderID;
    @JsonProperty("projectName")
    public String projectName;
    @JsonProperty("language")
    public String language;
    @JsonProperty("submitted")
    public Boolean submitted;
    @JsonProperty("claimed")
    public Boolean claimed;
    @JsonProperty("graded")
    public Boolean graded;
    @JsonProperty("hasUploadedTXT")
    public Boolean hasUploadedTXT;
    @JsonProperty("hasUploadedMP3")
    public Boolean hasUploadedMP3;
    @JsonProperty("hasUploadedPNG")
    public Boolean hasUploadedPNG;
}