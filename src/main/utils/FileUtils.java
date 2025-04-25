package utils;

import models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String USER_FILE = "users.txt";

    public static User authenticateUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    String id = data[0];
                    String uname = data[1];
                    String pwd = data[2];
                    String email = data[3];
                    String contact = data[4];
                    String role = data[5];

                    if (uname.equals(username) && pwd.equals(password)) {
                        return new User(id, uname, pwd, email, contact, role);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addUser(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            bw.write(user.getId() + "," + user.getUsername() + "," + user.getPassword() + "," +
                    user.getEmail() + "," + user.getContactNumber() + "," + user.getRole());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> readAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    users.add(new User(data[0], data[1], data[2], data[3], data[4], data[5]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}
