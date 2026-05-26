package ro.uaic.com;

import java.util.Locale;

import ro.uaic.app.LocaleExplore;

/**
 * SetLocale
 */
public class SetLocale implements Command {
    public String execute(LocaleExplore app, String[] args) {
        if (args.length != 1) {
            return app.getMessages().getString("invalid.params");
        }
        String languageTag = args[0];
        Locale newLocale = Locale.forLanguageTag(languageTag);

        if (newLocale.toLanguageTag().equals("und")) {
            return app.getMessages().getString("invalid.tag");
        }

        app.setLocale(newLocale);

        String setMessage = app.getMessages().getString("locale.set");
        String message = java.text.MessageFormat.format(
                setMessage,
                newLocale.getDisplayName(newLocale));

        return message;
    }
}
