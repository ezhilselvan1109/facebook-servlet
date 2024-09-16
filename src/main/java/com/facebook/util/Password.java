package com.facebook.util;

import org.mindrot.jbcrypt.BCrypt;

public class Password{

    // Method to hash the password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Method to verify the password (for login purposes)
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
