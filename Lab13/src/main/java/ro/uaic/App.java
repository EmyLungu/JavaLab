package ro.uaic;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        System.out.println("Default locale:");
        localeInfo(Locale.getDefault());

        System.out.println("Available locales:");
        Locale available[] =
            Locale.getAvailableLocales();
        for(Locale locale : available) {
            locale.getDisplayCountry() + "\t" +
                locale.getDisplayLanguage(locale));
        }
    }
}
