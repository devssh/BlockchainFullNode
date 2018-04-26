package app.model;

import java.util.Arrays;

public class Contract {
    public String name="abc";
    public String[] names={"hi","hello"};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contract contract = (Contract) o;

        if (name != null ? !name.equals(contract.name) : contract.name != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(names, contract.names);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(names);
        return result;
    }
}
