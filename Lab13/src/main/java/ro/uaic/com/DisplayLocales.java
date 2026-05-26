package ro.uaic.com;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import ro.uaic.app.LocaleExplore;

/**
 * DisplayLocales
 */
public class DisplayLocales implements Command {
    public String execute(LocaleExplore app, String[] args) {
        String out = "";
        out += app.getMessages().getString("locales") + "\n";

        Locale available[] = Locale.getAvailableLocales();
        for (Locale loc : available) {
            if (loc.getCountry().isEmpty())
                continue;

            ResourceBundle rb = ResourceBundle.getBundle("res.Messages", loc);

            String loadedLang = rb.getLocale().getLanguage();
            String requestedLang = loc.getLanguage();

            boolean isExactMatch = loadedLang.equals(requestedLang);
            boolean isDefault = requestedLang.equals("en") && loadedLang.isEmpty();
            if (!isExactMatch && !isDefault)
                continue;

            String country = loc.getDisplayCountry(app.getLocale());
            String language = loc.getDisplayLanguage(app.getLocale());
            String tag = loc.toLanguageTag();

            out += "  " + country + " (" + language + ") [" + tag + "]" + "\n";
        }

        return out;
    }
}
