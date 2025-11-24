package main.entities;

import java.util.Objects;

public class UserCredentials {
    public final String username;
    public final String passwordplaintext;
    public final long userID;
    public final boolean isGrader;

    public UserCredentials(String username, String plaintextPassword, long userID, boolean isGrader) {
        this.username = username;
        this.passwordplaintext = plaintextPassword;
        this.userID = userID;
        this.isGrader = isGrader;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        UserCredentials that = (UserCredentials) other;
        return username.equals(that.username) &&
                passwordplaintext.equals(that.passwordplaintext) &&
                userID == that.userID &&
                isGrader == that.isGrader;
    }


    @Override
    public int hashCode() {
        return Objects.hash(username, passwordplaintext, userID, isGrader);
    }
}
