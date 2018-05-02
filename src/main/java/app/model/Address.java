package app.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
public class Address {
    public final Integer blockDepth;
    public final Integer transactionDepth;

}
