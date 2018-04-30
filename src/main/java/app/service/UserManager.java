package app.service;

import app.model.dto.FullUserData;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UserManager {
    public static final ConcurrentMap<String, FullUserData> Users = new ConcurrentHashMap<>();
}
