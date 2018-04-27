package app.service;

import app.model.Block;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class ContractManager {

    public static final ConcurrentMap<String, ConcurrentLinkedQueue<Block>> BLOCKCHAIN = new ConcurrentHashMap<>();

}
