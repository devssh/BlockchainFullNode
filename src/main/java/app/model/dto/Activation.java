package app.model.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Activation {
    public final String email;
    public final String activationCode;

    Activation(Activation other) {
        this.email = other.email;
        this.activationCode = other.activationCode;
    }
}
