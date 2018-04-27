package app.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Address {
    public final String blockDepth;
    public final String transactionDepth;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (!blockDepth.equals(address.blockDepth)) return false;
        return transactionDepth.equals(address.transactionDepth);
    }

    @Override
    public int hashCode() {
        int result = blockDepth.hashCode();
        result = 31 * result + transactionDepth.hashCode();
        return result;
    }
}
