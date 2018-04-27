package app.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlockDetails {
    public final String nonce;
    public final String depth;
    public final String blockCreatedAt;
    public final String previousBlockSign;
    public final String merkleRoot;
    public final String difficulty;
    public final String relayedBy;
    public final String numberOfTransactions;
}
