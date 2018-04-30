package app.model.dto;

public class LoginSession {
    public final String email;
    public final String sessionToken;

    LoginSession(LoginSession other) {
        this.email = other.email;
        this.sessionToken = other.sessionToken;
    }
}
