package app.model;

import lombok.AllArgsConstructor;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@AllArgsConstructor
public class KeysView {
    public final ConcurrentMap<String, ConcurrentLinkedQueue<Keyz>> keys;
}
