package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static void writeToFile(String path, String content, boolean append) throws IOException {
        File file = new File(path);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));
        writer.write(content);
        writer.newLine();
        writer.close();
    }

    public static List<String> readLines(String path) throws IOException {
        File file = new File(path);
        List<String> lines = new ArrayList<>();

        if (!file.exists()) {
            return lines;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }

    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return java.util.Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return password; // fallback (not secure)
        }
    }

    public static boolean isUserIdExists(String path, String id) throws IOException {
        for (String line : readLines(path)) {
            String[] data = line.split(",");
            if (data.length > 0 && data[0].equals(id)) {
                return true;
            }
        }
        return false;
    }
}
