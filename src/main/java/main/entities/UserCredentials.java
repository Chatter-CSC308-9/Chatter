package main.entities;

public class UserCredentials {
    public final String username;
    public final String plaintextPassword;
    public final String email;
    public final int userID;

    public UserCredentials(String username, String plaintextPassword, String email, int userID) {
        this.username = username;
        this.plaintextPassword = plaintextPassword;
        this.email = email;
        this.userID = userID;
    }
}
