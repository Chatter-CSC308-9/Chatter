package main.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {
    @JsonProperty("learnerID")
    public long learnerID;
    @JsonProperty("graderID")
    public long graderID;
    @JsonProperty("projectName")
    public String projectName;
    @JsonProperty("projectTitle")
    public String projectTitle;
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
    @JsonProperty("isPaid")
    public Boolean isPaid;

    public int getCostInCents() {
        if (Boolean.TRUE.equals(hasUploadedMP3)) {
            return 50;
        } else if (Boolean.TRUE.equals(hasUploadedPNG)) {
            return 243542;
        } else if (Boolean.TRUE.equals(hasUploadedTXT)) {
            return 142924;
        } else /*if work.txt exists*/ {
            return 60;
        }
    }
}