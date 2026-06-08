package util;

import org.mindrot.jbcrypt.BCrypt;

public final class BCryptUtil {
    private BCryptUtil() {
    }

    public static String hash(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt(12));
    }

    public static boolean verify(String plainText, String hash) {
        try {
            return hash != null && BCrypt.checkpw(plainText, hash);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
