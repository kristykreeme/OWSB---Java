package utils;

import models.User;
import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String FILE_PATH = "users.txt";

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                users.add(User.fromFileFormat(line));
            }
        } catch (IOException e) {
            // If file doesn't exist, return empty list
        }
        return users;
    }

    public static void saveUser(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(user.toFileFormat());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isUsernameTaken(String username) {
        return loadUsers().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }
}
