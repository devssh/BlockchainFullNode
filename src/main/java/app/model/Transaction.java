package app.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Transaction {
    public final Address address;
    public final Address contractAddress;
    public final String contractName;
    public final String[] values;
}
