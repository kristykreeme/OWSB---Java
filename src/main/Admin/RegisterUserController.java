package Admin;

import models.User;
import utils.FileHandler;

import java.io.IOException;

public class RegisterUserController {
    private static final String FILE_PATH = "users.txt";

    public String registerUser(User user) {
        try {
            if (FileHandler.isUserIdExists(FILE_PATH, user.getId())) {
                return "User ID already exists.";
            }
            FileHandler.writeToFile(FILE_PATH, user.toFileFormat(), true);
            return "User registered successfully.";
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
