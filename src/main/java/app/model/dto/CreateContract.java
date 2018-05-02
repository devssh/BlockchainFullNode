package app.model.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.Arrays;

@AllArgsConstructor
@ToString
public class CreateContract {
    public final String name;
    public final String[] fields;

    CreateContract(CreateContract other) {
        this.name = other.name;
        this.fields = Arrays.stream(Arrays.copyOf(other.fields, other.fields.length))
                .map(x -> x.replaceAll(" ", "").replaceAll(",", ""))
                .filter(x -> x != null && !x.equals(""))
                .toArray(String[]::new);
    }
}
