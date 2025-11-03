package main.entities;

public class UserCredentials {
    public final String username;
    public final String plaintextPassword;
    public final int userID;

    public UserCredentials(String username, String plaintextPassword, int userID) {
        this.username = username;
        this.plaintextPassword = plaintextPassword;
        this.userID = userID;
    }
}
