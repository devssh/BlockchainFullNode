package app.utils;

import app.model.Block;
import app.model.Contract;
import app.model.Transaction;
import app.model.dto.Activation;
import app.model.dto.FullUserData;
import app.model.dto.LoginDetails;

import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static app.service.BlockManager.CreateAndVerifyContractsAndTransactions;
import static app.service.BlockManager.CreateBlockAndVerify;
import static app.service.KeyzManager.CreateAndStoreKey;
import static app.service.KeyzManager.CreateKey;
import static app.service.RegistrationManager.CreateRegistration;
import static app.service.UserManager.CreateUser;
import static app.utils.JsonUtils.FromJSON;
import static app.utils.JsonUtils.ToJSON;

public class FileUtil {
    public static final String MERKLE_FILENAME = "MERKLE.dat";
    public static final String KEYS_FILENAME = "KEYS.dat";
    public static final String USERS_FILENAME = "USERS.dat";
    public static final String REGISTERED_FILENAME = "RPU.dat";
    public static final String CODES_FILENAME = "ACTIVATION.dat";
    public static final String BLOCKS_FILENAME = "BLOCKS.dat";
    public static final String CONTRACTS_FILENAME = "CONTRACTS.dat";
    public static final String TRANSACTIONS_FILENAME = "TRANSACTIONS.dat";

    public static void InitServerLoadData() {
        ReadAllKeys();
        ReadAllUsers();
        ReadAllRegistrations();
        ReadAllBlocks();
        //Read all blocks must occur before read all transactions as it verifies
        ReadAllContractsAndTransactions();
        CreateAndStoreKey("Miner");
        System.out.println("Init Success");
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
            if (i == 0) {
                WriteLine(registrationValues[i], REGISTERED_FILENAME, true, false);
            } else {
                AppendLine(registrationValues[i], REGISTERED_FILENAME);

            }
        }

        String[] codesValues = codes.values().toArray(new String[0]);

        for (int i = 0; i < codesValues.length; i++) {
            if (i == 0) {
                WriteLine(codesValues[i], CODES_FILENAME, true, false);
            } else {
                AppendLine(codesValues[i], CODES_FILENAME);

            }
        }

    }

    public static void ReadAllBlocks() {
        try {
            Scanner scanner = new Scanner(new File(BLOCKS_FILENAME));
            String line = scanner.nextLine();
            CreateBlockAndVerify(line);

            while (true) {
                line = scanner.nextLine();
                CreateBlockAndVerify(line);
            }

        } catch (NoSuchElementException | FileNotFoundException ignored) {
        }
    }

    public static void ReadAllContractsAndTransactions() {
        try {
            Scanner scanner = new Scanner(new File(CONTRACTS_FILENAME));
            Scanner scanner0 = new Scanner(new File(TRANSACTIONS_FILENAME));
            String line = scanner.nextLine();
            String line0 = scanner0.nextLine();
            CreateAndVerifyContractsAndTransactions(line, line0);

            while (true) {
                line = scanner.nextLine();
                line0 = scanner0.nextLine();
                CreateAndVerifyContractsAndTransactions(line, line0);
            }

        } catch (NoSuchElementException | FileNotFoundException ignored) {
        }
    }

    public static void StoreBlockchainBlock(Block block) {
        AppendLine(ToJSON(block), BLOCKS_FILENAME);
    }

    public static void StoreContractsInBlock(List<Contract> contracts) {
        AppendLine(ToJSON(contracts.toArray()), CONTRACTS_FILENAME);
    }

    public static void StoreTransactionsInBlock(List<Transaction> transactions) {
        AppendLine(ToJSON(transactions.toArray()), TRANSACTIONS_FILENAME);
    }

    public static void StoreMerkleDataLog(String merkleData) {
        AppendLine(merkleData, MERKLE_FILENAME);
    }

}
