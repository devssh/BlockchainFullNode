package app.service;

import app.model.dto.LoginDetails;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RegistrationManager {
    public static final ConcurrentMap<String, LoginDetails> RegistrationPendingUsers = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, String> RegistrationCodes = new ConcurrentHashMap<>();
}
