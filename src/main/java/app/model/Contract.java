package app.model;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class Contract {
    public final String name;
    public final Address address;
    public final String[] fields;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contract contract = (Contract) o;

        if (!name.equals(contract.name)) return false;
        if (!address.equals(contract.address)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(fields, contract.fields);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + Arrays.hashCode(fields);
        return result;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "name='" + name + '\'' +
                ", address=" + address +
                ", fields=" + Arrays.toString(fields) +
                '}';
    }
}
