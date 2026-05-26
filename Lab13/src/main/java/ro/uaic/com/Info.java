package ro.uaic.com;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import ro.uaic.app.LocaleExplore;

/**
 * Info
 */
public class Info implements Command {
    public String execute(LocaleExplore app, String[] args) {
        Locale targetLocale;
        if (args.length == 0) {
            targetLocale = app.getLocale();
        } else if (args.length == 1) {
            targetLocale = Locale.forLanguageTag(args[0]);
        } else {
            return app.getMessages().getString("invalid.params");
        }

        String out = "";

        String title = MessageFormat.format(
                app.getMessages().getString("info"),
                targetLocale.toString());
        out += title + "\n";

        try {
            // Country
            String countryDisplay = targetLocale.getDisplayCountry(app.getLocale());
            String countryNative = targetLocale.getDisplayCountry(targetLocale);

            out += "\t" + app.getMessages().getString("country") + ": ";
            out += countryDisplay + (countryNative.isEmpty() ? "" : " (" + countryNative + ")") + "\n";

            // Language
            String langDisplay = targetLocale.getDisplayLanguage(app.getLocale());
            String langNative = targetLocale.getDisplayLanguage(targetLocale);
            out += "\t" + app.getMessages().getString("language") + ": ";
            out += langDisplay + (langNative.isEmpty() ? "" : " (" + langNative + ")") + "\n";

            // Currency
            try {
                out += "\t" + app.getMessages().getString("currency") + ": ";
                Currency currency = Currency.getInstance(targetLocale);
                String currencyCode = currency.getCurrencyCode();
                String currencyName = currency.getDisplayName(app.getLocale());
                out += currencyCode + " (" + currencyName + ")\n";
            } catch (IllegalArgumentException e) {
                out += "N/A\n";
            }

            DateFormatSymbols symbols = DateFormatSymbols.getInstance(targetLocale);

            String[] rawWeekdays = symbols.getWeekdays();
            String[] weekdays = Arrays.stream(rawWeekdays)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
            out += "\t" + app.getMessages().getString("week") + ": ";
            out += String.join(", ", weekdays) + "\n";

            String[] rawMonths = symbols.getMonths();
            String[] months = Arrays.stream(rawMonths)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
            out += "\t" + app.getMessages().getString("months") + ": ";
            out += String.join(", ", months) + "\n";

            DateTimeFormatter appFormatter = DateTimeFormatter
                    .ofLocalizedDate(FormatStyle.FULL)
                    .withLocale(app.getLocale());

            DateTimeFormatter targetFormatter = DateTimeFormatter
                    .ofLocalizedDate(FormatStyle.FULL)
                    .withLocale(targetLocale);

            LocalDateTime today = LocalDateTime.now();
            out += "\t" + app.getMessages().getString("today") + ": ";
            out += today.format(appFormatter) + " (" + today.format(targetFormatter) + ")\n";

        } catch (Exception e) {
            out += app.getMessages().getString("invalid");
        }

        return out;
    }
}
