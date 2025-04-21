package main.utils;

import main.models.User;
import java.io.*;
import java.util.ArrayList;

public class FileUtils {

    private static final String USER_FILE = "users.txt";

    public static User authenticateUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String id = data[0];
                    String uname = data[1];
                    String pwd = data[2];
                    String role = data[3];

                    if (uname.equals(username) && pwd.equals(password)) {
                        return new User(id, uname, pwd, role);
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
            bw.write(user.getId() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getRole());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<User> readAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    users.add(new User(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void deleteUserById(String id) {
        ArrayList<User> users = readAllUsers();
        users.removeIf(u -> u.getId().equals(id));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User u : users) {
                bw.write(u.getId() + "," + u.getUsername() + "," + u.getPassword() + "," + u.getRole());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
