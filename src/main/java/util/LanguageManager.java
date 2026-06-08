package util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class LanguageManager {
    private static final LanguageManager INSTANCE = new LanguageManager();
    private ResourceBundle bundle;

    private LanguageManager() {
        setLanguage("en");
    }

    public static LanguageManager getInstance() {
        return INSTANCE;
    }

    public void setLanguage(String code) {
        Locale locale = "am".equalsIgnoreCase(code) ? Locale.forLanguageTag("am") : Locale.ENGLISH;
        try {
            bundle = ResourceBundle.getBundle("lang.messages", locale);
        } catch (MissingResourceException ex) {
            bundle = null;
        }
    }

    public String text(String key) {
        if (bundle == null) {
            return key;
        }
        try {
            return bundle.getString(key);
        } catch (MissingResourceException ex) {
            return key;
        }
    }
}
