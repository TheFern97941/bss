package bss.core.utils;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class GoogleAuthenticatorUtils {
    public static final GoogleAuthenticator G_AUTH = new GoogleAuthenticator();

    public static boolean authorize(String key, int code) {
        return G_AUTH.authorize(key, code);
    }

    public static String newKey() {
        final GoogleAuthenticatorKey key = G_AUTH.createCredentials();
        return key.getKey();
    }
}
