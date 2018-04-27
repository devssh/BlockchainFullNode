package app.utils;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static app.service.KeyzManager.CreateKey;

public class FileUtil {

    public static final String KEYS_FILENAME = "KEYS.dat";

    public static void AppendLine(String block, String fileName) {
        AppendLine(block, fileName, true, true);
    }

    public static void AppendLine(String block, String fileName, boolean newline, boolean append) {
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


}
