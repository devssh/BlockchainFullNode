package app.utils;

import app.model.dto.Activation;
import app.model.dto.FullUserData;
import app.model.dto.LoginDetails;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static app.service.KeyzManager.CreateKey;
import static app.service.RegistrationManager.CreateRegistration;
import static app.service.UserManager.CreateUser;
import static app.utils.JsonUtils.FromJSON;
import static app.utils.JsonUtils.ToJSON;

public class FileUtil {
    public static final String KEYS_FILENAME = "KEYS.dat";
    public static final String USERS_FILENAME = "USERS.dat";
    public static final String REGISTERED_FILENAME = "RPU.dat";
    public static final String CODES_FILENAME = "ACTIVATION.dat";

    public static void InitServerLoadData() {
        ReadAllKeys();
        ReadAllUsers();
        ReadAllRegistrations();
    }

    public static void AppendLine(String block, String fileName) {
        WriteLine(block, fileName, true, true);
    }

    public static void WriteLine(String block, String fileName, boolean newline, boolean append) {
        try {
            Writer output = new BufferedWriter(new FileWriter(fileName, append));
            output.append(block + (newline ? "\n" : ""));
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void StoreKey(String block) {
        AppendLine(block, KEYS_FILENAME);
    }

    public static void ReadAllKeys() {
        try {
            Scanner scanner = new Scanner(new File(KEYS_FILENAME));
            String line = scanner.nextLine();

            CreateKey(line);
            while (true) {
                line = scanner.nextLine();
                CreateKey(line);
            }

        } catch (NoSuchElementException | FileNotFoundException ignored) {
        }
    }


    public static void StoreUser(FullUserData user) {
        AppendLine(ToJSON(user), USERS_FILENAME);
    }

    public static void ReadAllUsers() {
        try {
            Scanner scanner = new Scanner(new File(USERS_FILENAME));
            String line = scanner.nextLine();

            CreateUser(line);
            while (true) {
                line = scanner.nextLine();
                CreateUser(line);
            }

        } catch (NoSuchElementException | FileNotFoundException ignored) {
        }
    }

    public static Activation StoreRegistration(LoginDetails loginDetails, String activationCode) {
        AppendLine(ToJSON(loginDetails), REGISTERED_FILENAME);
        Activation activation = new Activation(loginDetails.email, activationCode);
        AppendLine(ToJSON(activation), CODES_FILENAME);
        return activation;
    }

    public static void ReadAllRegistrations() {
        ConcurrentMap<String, String> registrations = new ConcurrentHashMap<>();
        ConcurrentMap<String, String> codes = new ConcurrentHashMap<>();
        try {
            Scanner scanner = new Scanner(new File(REGISTERED_FILENAME));
            Scanner scanner2 = new Scanner(new File(CODES_FILENAME));
            String line = scanner.nextLine();
            String line2 = scanner2.nextLine();
            registrations.putIfAbsent(FromJSON(line, LoginDetails.class).email, line);
            codes.putIfAbsent(FromJSON(line2, Activation.class).email, line2);

            while (true) {
                line = scanner.nextLine();
                line2 = scanner2.nextLine();
                registrations.putIfAbsent(FromJSON(line, LoginDetails.class).email, line);
                codes.putIfAbsent(FromJSON(line2, Activation.class).email, line2);
            }

        } catch (NoSuchElementException | FileNotFoundException ignored) {
        }
        for (String email : registrations.keySet()) {
            try {
                CreateRegistration(registrations.get(email), codes.get(email));
            } catch (IllegalArgumentException e) {
                registrations.remove(email);
                codes.remove(email);
            }
        }

        String[] registrationValues = registrations.values().toArray(new String[0]);

        for (int i = 0; i < registrationValues.length; i++) {
            if (i==0) {
                WriteLine(registrationValues[i], REGISTERED_FILENAME, true, false);
            } else {
                AppendLine(registrationValues[i], REGISTERED_FILENAME);

            }
        }

        String[] codesValues = codes.values().toArray(new String[0]);

        for (int i = 0; i < codesValues.length; i++) {
            if (i==0) {
                WriteLine(codesValues[i], CODES_FILENAME, true, false);
            } else {
                AppendLine(codesValues[i], CODES_FILENAME);

            }
        }

    }


}
