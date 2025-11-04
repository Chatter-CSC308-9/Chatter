package main.entities;

public class UserCredentials {
    public final String username;
    public final String plaintextPassword;
    public final int userID;
    public final boolean isGrader;

    public UserCredentials(String username, String plaintextPassword, int userID, boolean isGrader) {
        this.username = username;
        this.plaintextPassword = plaintextPassword;
        this.userID = userID;
        this.isGrader = isGrader;
    }
}
