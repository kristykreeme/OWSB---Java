package utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {
    public static void log(String adminId, String action, String targetUserId) {
        try {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String log = time + " | Admin " + adminId + " | " + action + " | " + targetUserId;
            FileHandler.writeToFile(Constants.LOG_FILE, log, true);
        } catch (IOException e) {
            System.err.println("Audit log failed: " + e.getMessage());
        }
    }
}
