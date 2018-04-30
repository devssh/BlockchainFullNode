package app.model.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FullUserData {
    public final String email;
    public final String password;
    public String sessionToken;
    public String validUntil;

    FullUserData(FullUserData other) {
        this.email = other.email;
        this.password = other.password;
        this.sessionToken = other.sessionToken;
        this.validUntil = other.validUntil;
    }
}
